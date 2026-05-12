# \_trigger_dag()

## 📌 함수 개요

- DagRun 생성 직전의 검증과 생성 실행을 담당

```
최신 DAG 조회
→ logical_date 검증
→ data_interval 계산
→ run_id 생성
→ 중복 DagRun 확인
→ conf 파싱
→ dag.create_dagrun(...)
→ DagRun 반환
```

## 📌 소스 코드

```python
@provide_session
def _trigger_dag(
    dag_id: str,
    dag_bag: DBDagBag,
    *,
    triggered_by: DagRunTriggeredByType,
    run_type: DagRunType = DagRunType.MANUAL,
    triggering_user_name: str | None = None,
    run_after: datetime | None = None,
    run_id: str | None = None,
    conf: dict | str | None = None,
    logical_date: datetime | None = None,
    replace_microseconds: bool = True,
    note: str | None = None,
    partition_key: str | None = None,
    session: Session = NEW_SESSION,
) -> DagRun | None:
    """
    Triggers Dag run.

    :param dag_id: Dag ID
    :param dag_bag: Dag Bag model
    :param triggered_by: the entity which triggers the dag_run
    :param run_type: the type of dag run (default: MANUAL)
    :param triggering_user_name: the user name who triggers the dag_run
    :param run_after: the datetime before which dag cannot run
    :param run_id: ID of the run
    :param conf: configuration
    :param logical_date: logical date of the run
    :param replace_microseconds: whether microseconds should be zeroed
    :return: list of triggered dags
    """
    if (dag := dag_bag.get_latest_version_of_dag(dag_id, session=session)) is None:
        raise DagNotFound(f"Dag id {dag_id} not found")

    run_after = run_after or timezone.coerce_datetime(timezone.utcnow())
    coerced_logical_date: datetime | None = None
    if logical_date:
        if not timezone.is_localized(logical_date):
            raise ValueError("The logical date should be localized")

        if replace_microseconds:
            logical_date = logical_date.replace(microsecond=0)

        if dag.default_args and "start_date" in dag.default_args:
            min_dag_start_date = dag.default_args["start_date"]
            if min_dag_start_date and logical_date < min_dag_start_date:
                raise ValueError(
                    f"Logical date [{logical_date.isoformat()}] should be >= start_date "
                    f"[{min_dag_start_date.isoformat()}] from Dag's default_args"
                )
        coerced_logical_date = timezone.coerce_datetime(logical_date)
        data_interval: DataInterval | None = dag.timetable.infer_manual_data_interval(
            run_after=timezone.coerce_datetime(run_after)
        )
    else:
        data_interval = None

    run_id = run_id or dag.timetable.generate_run_id(
        run_type=run_type,
        run_after=timezone.coerce_datetime(run_after),
        data_interval=data_interval,
    )

    # This intentionally does not use 'session' in the current scope because it
    # may be rolled back when this function exits with an exception (due to how
    # provide_session is implemented). This would make the DagRun object in the
    # DagRunAlreadyExists expire and unusable.
    if dag_run := DagRun.find_duplicate(dag_id=dag_id, run_id=run_id):
        raise DagRunAlreadyExists(dag_run)

    run_conf = None
    if conf:
        run_conf = conf if isinstance(conf, dict) else json.loads(conf)
    dag_run = dag.create_dagrun(
        run_id=run_id,
        logical_date=coerced_logical_date,
        data_interval=data_interval,
        run_after=run_after,
        conf=run_conf,
        run_type=run_type,
        triggered_by=triggered_by,
        triggering_user_name=triggering_user_name,
        note=note,
        state=DagRunState.QUEUED,
        partition_key=partition_key,
        session=session,
    )

    return dag_run

```

- `if (dag := dag_bag.get_latest_version_of_dag(dag_id, session=session)) is None:`
  - `dag_id`에 해당하는 최신 Dag를 가져옴
  - 없다면 DagNotFound를 발생시킴
- `run_after = run_after or timezone.coerce_datetime(timezone.utcnow())`
  - `run_after`가 없다면 현재 UTC 시간을 사용
  - `run_after` : 이 시각 이후에 실행 가능한 DagRun
- `coerced_logical_date: datetime | None = None
if logical_date:` - `logical_date` 검증 및 보정 - timezone 포함 여부 확인 → 없다면 에러 - `replace_microseconds=True`라면 microsecond 제거 - `logical_date`가 DAG의 `start_date`보다 과거면 실행을 막음 - manual trigger에 맞는 `data_interval`을 계산
- `run_id` 생성 : 사용자가 `run_id`를 직접 주지 않았다면 Airflow가 자동 생성
- 중복 DagRun 확인 → 같은 `dag_id`, 같은 `run_id`의 DagRun이 이미 있으면 에러
- `conf` 처리
  ```python
  run_conf = None
  if conf:
      run_conf = conf if isinstance(conf, dict) else json.loads(conf)
  ```
  | 입력 `conf` | `run_conf`                      |
  | ----------- | ------------------------------- |
  | `None`      | falsy → ∴ `None`                |
  | `{}`        | falsy → ∴ `None`                |
  | `"”`        | falsy → ∴ `None`                |
  | `'{}'`      | `json.loads('{}')` → ∴ `{}`     |
  | `'[1]'`     | `json.loads('[1]')` → ∴ `[1]`   |
  | `'"str"'`   | `json.loads('"str"')` → `"str"` |
  | `'1'`       | `json.loads('1')` → `1`         |
  - `json.loads(conf)에서 JSON 파싱만 하고, 결과가 dict인지 확인하지 않음`
  - 따라서 REST API 기준으로는 허용되지 않아야 할 값도 CLI 경로에서는 통과 가능
- `dag_run = dag.create_dagrun(…)`
  - DagRun 생성할 때 `conf=run_conf`로 그대로 DagRun의 conf로 저장됨

## 📌 문제가 되는 이유

### 1. 일관성 문제 → 큰 문제는 되지 않음

- `{}`, `‘{}’` 둘 다 빈 JSON object를 의미함
- 그러나 conf가 `{}`일 때는 run_conf가 `None`이 되고, conf가 `‘{}’`일 때는 run_conf가 `{}`로 됨
- 다만 많은 Dag 코드는 방어적으로 `conf = dag_run.conf or {}` 와 같이 작성되므로, `None`과 `{}`는 결과적으로 `{}`로 같음

### 2. `dict`가 아닌 값이 저장되는 것 → 주요 문제

- `--conf '[1]', --conf '"str"', --conf '1'`
- JSON 문법상 유효하지만, configuration object는 아님
- 현재 로직은 문자열이면 그냥 `json.loads()`만 수행
- 그런데 `json.loads()`는 JSON object만 반환하지 않음
  ```python
  json.loads('{}')      # dict
  json.loads('[1]')     # list
  json.loads('"str"')   # str
  json.loads('1')       # int
  json.loads('null')    # None
  ```
- 즉 JSON으로 유효하지만 설정 객체로는 부적절한 값이 `DagRun.conf`에 들어갈 수 있음

## 📌 dict인지 확인해야 하는 이유

### 1. `dag_run.conf.get(...)` 패턴이 깨짐

- Airflow DAG 코드나 템플릿에서는 `dag_run.conf`를 보통 dict처럼 사용함
  - `value = dag_run.conf.get("target_date")`
- 그런데 conf가 `list`, `string`으로 들어가면 런타임 에러 발생

### 2. `params` 병합과 충돌 가능

- Airflow에서는 DAG params와 DagRun conf를 병합하거나 참조하는 흐름이 있음
- 이때 `conf`는 key-value mapping이라는 전제가 강함

### 3. REST API와 CLI의 계약이 달라짐

- REST API 쪽은 이미 `conf`를 dict로 다루고 있음
  - `conf: dict | None`
  - object 또는 None
- 그러나 CLI에서는 다음이 통과함
  - `array, string, number`
- 같은 기능인데 진입 경로에 따라 정책이 달라짐
  | 진입 경로 | `conf='[1]'` |
  | --------- | ------------ |
  | REST API  | 거부         |
  | CLI       | 통과 가능    |
- API/CLI consistency 버그

## 📌 수정 방향

- 현재 코드의 문제 : truthiness로 conf 존재 여부 판단 + json.loads 결과 타입 미검증
  ```python
  run_conf = None
  if conf:
      run_conf = conf if isinstance(conf, dict) else json.loads(conf)
  ```
- 수정 방향
  ```python
  run_conf = None

  if conf is not None:
      if isinstance(conf, str):
          if conf == "":
              raise ValueError("conf must be a valid JSON object")
          run_conf = json.loads(conf)
      else:
          run_conf = conf

      if run_conf is not None and not isinstance(run_conf, dict):
          raise ValueError("conf must be a JSON object")
  ```
  | 입력            | 기대 동작 |
  | --------------- | --------- |
  | 생략            | 허용      |
  | `None` / `null` | 허용      |
  | `{}`            | 허용      |
  | `'{}'`          | 허용      |
  | `'[1]'`         | 거부      |
  | `'"str"'`       | 거부      |
  | `'1'`           | 거부      |
  | `''`            | 거부      |
- 이 방식도 생략됨과 명시적 None을 완전히 구분하기 어려움
- 따라서 센티널 객체 패턴을 사용하면 더 명확해짐
- Airflow에는 이를 위한 센티널이 정의되어 있고 사용되고 있음
  ```bash
  class ArgNotSet:
  """Sentinel type for annotations, useful when None is not viable."""

  NOTSET = ArgNotSet()
  ```
