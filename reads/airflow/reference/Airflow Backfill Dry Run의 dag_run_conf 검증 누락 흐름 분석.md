# Airflow Backfill Dry Run의 dag_run_conf 검증 누락 흐름 분석

> 파일 경로 : airflow-core/src/airflow/models/backfill.py

## 📌 _validate_backfill_params()

- Backfill 실행 전에 요청이 안전하고 유효한지 검증하는 함수

```python
def _validate_backfill_params(
    dag: SerializedDAG, # Backfill 대상 Dag(DB에 저장된 SerializedDAG)
    reverse: bool, # Backfill을 역순으로 실행할지 여부
    from_date: datetime, # Backfill 시작 날짜
    to_date: datetime, # Backfill 종료 날짜
    reprocess_behavior: ReprocessBehavior | None, # 기존 DagRun이 있을 때 다시 처리할 방식
    dag_run_conf: dict | None = None, # Backfill로 생성되는 DagRun에 전달할 설정값
) -> None:
# 정상이라면 아무것도 반환하지 않고 통과
    depends_on_past = any(x.depends_on_past for x in dag.tasks) # Dag의 Task 중에 이전 실행 결과에 의존하는 Task가 있는지 확인
    if depends_on_past:
        if reverse is True: # depends_on_past=True인데 reverse 실행이면 차단
            raise InvalidBackfillDirection(
                "Backfill cannot be run in reverse when the Dag has tasks where depends_on_past=True."
            )
        if reprocess_behavior in (None, ReprocessBehavior.NONE): # depends_on_past=True인데 재처리 방식이 없으면 차단
            raise InvalidReprocessBehavior(
                "Dag has tasks for which depends_on_past=True. "
                "You must set reprocess behavior to reprocess completed or reprocess failed."
            )
    current_time = timezone.utcnow() # 현재 UTC 시간을 가져옴
    if from_date >= current_time and to_date >= current_time: # 미래 날짜 Backfill 차단
        raise InvalidBackfillDate("Backfill cannot be executed for future dates.")
    if dag_run_conf is not None: # dag_run_conf가 있다면 Params validation 수행
        try:
            dag.params.deep_merge(dag_run_conf).validate()
        except ValueError as e:
            raise InvalidBackfillConf(str(e)) from e

```

### 🔹 검증 항목

- 검증 항목 1 : `depends_on_past=True`인 Task가 있는가?
  - → 과거 실행 순서가 중요한 Dag인지 확인
- 검증 항목 2 : 미래 날짜에 대한 Backfill인가?
  - 미래의 날짜는 Backfill하지 못하게 차단
- 검증 항목 3 : `dag_run_conf`가 Dag Params 규칙에 맞는가?
  - 잘못된 conf로 DagRun이 생성되지 않도록 차단

---

### 🔹 `depends_on_past=True` Task

- 즉, 이전 실행 결과에 의존하는 Task
  - → 이전 날짜의 실행이 성공해야 다음 날짜 실행이 의미가 있음, 따라서 실행 순서가 중요함
- 따라서 `depends_on_past=True`인데 reverse 실행이면 차단
  - reverse=True면 Backfill을 가장 최근 날짜부터 실행한다는 의미
  - 그러나 이전 실행 결과에 의존하는 Task는 과거 날짜부터 실행하는 설정이므로 맞지 않으므로 예외 처리

---

### 🔹 `depends_on_past=True`인데 재처리 방식이 없으면 차단

- Backfill을 할 때 이미 기존 DagRun이 있을 수 있고, 기존 실행을 어떻게 다룰지 정해야 함

| 값          | 의미                          |
| ----------- | ----------------------------- |
| `NONE`      | 기존 DagRun은 재처리하지 않음 |
| `FAILED`    | 실패한 DagRun만 재처리        |
| `COMPLETED` | 완료된 DagRun도 재처리        |

- 그런데 `depends_on_past=True`이면 실행 흐름이 이전 날짜에 의존하므로, 재처리 방식이 없거나 `NONE`이면 차단

---

### 🔹 DAG Params와 `dag_run_conf`를 병합 후 검증

- `dag.params` : Dag에 정의된 기본 Params
- `deep_merge(dag_run_conf)` : 기본 Params와 사용자가 넘긴 dag_run_conf를 병합
- `validate()` : Params schema에 맞는지 검증
  - → 여기서 에러가 발생하면 `InvalidBackfillConf` 발생
- dry-run에는 이 부분에 대한 로직이 없는 상태
  - `dag_run_conf` validation 로직이 없음

---

## 📌 _create_backfill()

---

- 특정 Dag에 대해 Backfill 실행 가능 여부 검증 후, Backfill Job 레코드를 만들고, 해당 날짜 범위에 필요한 Backfill DagRun들을 생성
  1. dag_id로 Serialized Dag 조회
  2. Backfill 가능한 Dag인지 검증
  3. 이미 실행 중인 Backfill이 있는지 확인
  4. Backfill 요청 파라미터 검증
  5. Backfill 레코드와 DagRun들을 생성

---

### 🔹 함수 시그니처

- `*` 가 있으므로 모든 인자는 keyword-onyly argument
  - → 인자가 많은 함수에서 위치 기반 호출을 하면 실수가 나올 수 있으므로

| 파라미터                | 의미                                         |
| ----------------------- | -------------------------------------------- |
| `dag_id`                | Backfill 대상 Dag ID                         |
| `from_date`             | Backfill 시작 logical date                   |
| `to_date`               | Backfill 종료 logical date                   |
| `max_active_runs`       | 이 Backfill에서 동시에 실행할 DagRun 수 제한 |
| `reverse`               | 최신 날짜부터 역순으로 Backfill할지 여부     |
| `dag_run_conf`          | Backfill DagRun에 전달할 conf                |
| `triggering_user_name`  | 누가 Backfill을 실행했는지                   |
| `reprocess_behavior`    | 기존 DagRun 재처리 정책                      |
| `run_on_latest_version` | 최신 DAG bundle/version으로 실행할지 여부    |

- 반환값 : 생성된 Backfill 객체

---

### 🔹 Backfill 파라미터 검증

- `_validate_backfill_params(...)`
- 여기서는 `dag_run_conf`를 넘기므로 validation이 수행됨
- 그러나 dry-run에서는 같은 validation을 수행하면서 dag_run_conf를 넘기지 않아서 validation parity(서로 다른 실행 경로에서도 같은 입력값에 대해 같은 검증 규칙을 적용해야 함)가 깨짐

---

## 📌 _do_dry_run()

---

### 🔹 _validate_backfill_params()를 호출하지만 dag_run_conf를 넘기지 않음

- 현재 dry-run 경로에서 호출 흐름
  ```
  _do_dry_run()
    → _validate_backfill_params(...)
    → dag_run_conf 기본값 None
    → dag_run_conf validation 로직 미실행
  ```
- 따라서 `_validate_backfill_params()` 안의 이 코드는 실행되지 않음
  ```python
  if dag_run_conf is not None:
      try:
          dag.params.deep_merge(dag_run_conf).validate()
      except ValueError as e:
          raise InvalidBackfillConf(str(e)) from e
  ```

---

### 🔹 `_do_dry_run()` 함수 자체에도 `dag_run_conf` 파라미터가 없음

- 따라서 `_do_dry_run()`의 시그니처도 바꿔야함

---
