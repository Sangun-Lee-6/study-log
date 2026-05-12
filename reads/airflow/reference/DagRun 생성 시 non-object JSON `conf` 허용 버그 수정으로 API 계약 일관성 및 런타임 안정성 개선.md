# DagRun 생성 시 non-object JSON `conf` 허용 버그 수정으로 API 계약 일관성 및 런타임 안정성 개선

## 📌 PR 요약

> `airflow dags trigger --conf`가 “유효한 JSON이면 아무거나 허용”하던 문제를 고쳐서, REST API와 동일하게 JSON object 또는 null만 허용하도록 검증을 강화한 변경
> 링크 : https://github.com/apache/airflow/pull/66617

### 🔹 문제 상황

- 기존 CLI는 `--conf`에 들어온 값이 유효한 JSON이면 받아들임
  - 예를 들면 아래 값들이 모두 통과할 수 있었음
  ```bash
  airflow dags trigger example --conf='[1]'
  airflow dags trigger example --conf='"value"'
  airflow dags trigger example --conf='1'
  airflow dags trigger example --conf='false'
  ```
- 그러나 이 값들은 파이썬 기준으로 보면 각각 다른 타입
  | 입력 | 파싱 결과 | 타입 |
  | ----------- | --------- | ---- |
  | `'[1]'` | `[1]` | list |
  | `'"value"'` | `"value"` | str |
  | `'1'` | `1` | int |
  | `'false'` | `False` | bool |
- 문제는 Airflow 내부나 사용자 Dag 코드에서 dag_run.conf를 보통 딕셔너리처럼 접근함

  ```python
  dag_run.conf.get("key")
  ```

  - 그러나 dag_run.conf가 list, string, number, boolean이면 `.get()`이 없어서 런타임 에러가 날 수 있음

### 🔹 REST API와 비교

- Airflow REST API에서는 conf를 다음과 같이 제한함
  ```python
  dict | None
  ```
- 그러나 CLI는 더 느슨하게 동작해서 배열, 문자열, 숫자, boolean까지 허용함
- 따라서 이 PR의 목적 : “CLI의 `--conf` 검증 규칙을 REST API와 맞추기”

### 🔹 기존 코드의 문제

- `if conf:` 로는 conf의 의미를 정확하게 구분하기 어려웠음
  ```python
  run_conf = None
  if conf:
      run_conf = conf if isinstance(conf, dict) else json.loads(conf)
  ```
  | 케이스      | 예시                | 기존 코드에서 애매한 이유             |
  | ----------- | ------------------- | ------------------------------------- |
  | conf 미전달 | 없음                | 기본값인지 명확히 구분 필요           |
  | 빈 객체     | `{}`                | falsy 값이라 처리 흐름이 꼬일 수 있음 |
  | JSON null   | `null`              | 명시적으로 null을 준 것인지 구분 필요 |
  | 잘못된 JSON | `{invalid}`         | 파싱 실패 처리 필요                   |
  | 잘못된 타입 | `[1]`, `"str"`, `1` | JSON은 맞지만 conf 타입으로는 부적절  |
- 즉, 기존 코드는 JSON 파싱은 했지만, 파싱 결과 타입 검증은 충분히 하지 않음

### 🔹 PR의 해결 방식

1. Step 1 : 입력 여부 구분
   - conf가 아얘 생략된 것인지, 사용자가 명시적으로 값을 준 것인지 구분
   - 이를 위해 Airflow의 기존 패턴인 `NOTSET` 센티널을 사용
   - 이로 인해 다음을 구분할 수 있음
     - conf is NOTSET → 사용자가 conf를 제공하지 않음
     - conf is None → 사용자가 명시적으로 null을 제공
   - `None` 만으로는 `값이 없음`과 `명시적 null`을 구분하기 어렵기 때문에 센티널이 필요
2. JSON 파싱 후 타입 검증
   - 문자열 입력은 `json.loads()`로 파싱함
   - 그 다음 파싱 결과가 `dict`, `None` 둘 중 하나인지 확인
   - 이로 인해 list, string, number, boolean은 거부

### 🔹 변경 후 동작

| 입력                        | 결과                |
| --------------------------- | ------------------- |
| `--conf` 생략               | 기존 기본 동작 유지 |
| `--conf='{}'`               | 허용                |
| `--conf='{"key": "value"}'` | 허용                |
| `--conf='null'`             | 허용                |
| `--conf='{invalid}'`        | 거부                |
| `--conf='[1]'`              | 거부                |
| `--conf='"value"'`          | 거부                |
| `--conf='1'`                | 거부                |
| `--conf='false'`            | 거부                |

### 🔹 테스트로 보강한 것

- CLI 테스트는 다음 케이스를 검증

| 테스트 대상                 | 목적                                             |
| --------------------------- | ------------------------------------------------ |
| `{}`                        | 빈 object가 정상 허용되는지                      |
| `null`                      | 명시적 JSON null이 허용되는지                    |
| invalid JSON                | 깨진 JSON이 거부되는지                           |
| array/string/number/boolean | JSON은 맞지만 object/null이 아닌 값이 거부되는지 |

### 🔹 PR 후보로서의 평가

- 센티널 패턴 이해를 보여줄 수 있음
- API/CLI 일관성 버그로 설명하기 쉬움
- 작업 범위가 작음
- 리뷰어가 받아들이기 쉬움
- 운영 안정성과 직접 연결됨

---

## 📌 주요 함수

### 🔹 dag_trigger(args)

- [함수 분석](<./dag_trigger(args).md>)
- `airflow-core/src/airflow/cli/commands/dag_command.py:75`
- `airflow dags trigger`의 CLI 엔트리포인트
- `args.conf`를 검증하지 않고 아래처럼 넘김
  - `api_client.trigger_dag(..., conf=args.conf)`
- 현재 빈 문자열과 생략이 구분 없이 넘어가고 있음

### 🔹 Client.trigger_dag(...)

- `airflow-core/src/airflow/api/client/local_client.py:38`
- CLI가 현재 사용하는 로컬 API client
- 이 함수도 직접 DagRun을 만들지 않고 공통 함수로 넘김
- `conf`가 여기서도 검증 없이 그대로 `airflow.api.common.trigger_dag.trigger_dag()`로 넘김

### 🔹 trigger_dag(...)

- `airflow-core/src/airflow/api/common/trigger_dag.py:134`
- Dag 존재 여부, 실행 가능 run type을 검증한 뒤, 실제 처리는 `_trigger_dag()`로 넘기는 함수
- 이 함수에서도 `conf`를 검증하지 않고 `_trigger_dag()`로 넘김
- `conf: dict | str | None = None`로 받음

### 🔹 _trigger_dag(...)

- [함수 분석](<./_trigger_dag().md>)
- `airflow-core/src/airflow/api/common/trigger_dag.py:42`
- 실제 문제의 중심
- `_trigger_dag()`는 실제로 **DagRun을 생성하는 핵심 함수**
- 현재 `--conf` 검증 이슈에서 가장 중요한 부분은 이 코드

  ```python
  run_conf = None
  if conf:
      run_conf = conf if isinstance(conf, dict) else json.loads(conf)
  ```

  - `if conf:` 때문에 `None`, `""`, `{}` 같은 값의 의미가 흐려짐
  - `json.loads(conf)` 결과가 `dict`인지 확인하지 않음
  - 그래서 `-conf '[1]'`, `-conf '"str"'`, `-conf '1'` 같은 JSON 값도 통과 가능

### 🔹 public REST API

- `airflow-core/src/airflow/api_fastapi/core_api/datamodels/dag_run.py:141`

```python
conf: dict | None = Field(default_factory=dict)
```

- public API에서는 object 또는 null만 받음
- CLI 동작을 이 기준에 맞추는게 PR 설명의 근거

---

## 📌 센티널 선택지 정리

### 🔹 Airflow 내 private sentinel 선례

- `NOTSET`을 import하지 않고 함수 내부에서 private sentinel을 만드는 패턴도 있음
- 예시 : `airflow-core/src/airflow/api_fastapi/auth/tokens.py:545`
  ```python
  sentinel = object()
  secret_key = conf.get(section, key, fallback=sentinel)
  ```

### 🔹 `NOTSET` 사용

- Airflow에서 이미 쓰는 공식적인 “인자 생략” 센티널
- 의도가 잘 드러나고 Airflow 코드 스타일과 맞음

### 🔹 결정 : NOTSET 사용

- `trigger_dag.py`는 여러 곳에서 사용되는 함수이므로, import를 감안해도 private sentinel보다 기존 `NOTSET` 스타일이 유지보수에 더 유리하다고 생각함
