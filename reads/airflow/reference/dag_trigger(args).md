# dag_trigger(args)

## 📌 함수 설명

- 사용자가 CLI에서 실행한 아래 명령을 실제 DagRun 생성 요청으로 바꾸는 함수
  ```bash
  airflow dags trigger <dag_id> --conf ...
  ```

  - CLI 입력값을 받아 현재 API client에 trigger_dag() 요청을 위임하고, 결과를 콘솔에 출력

## 📌 소스 코드

```python
@cli_utils.action_cli
@providers_configuration_loaded
def dag_trigger(args) -> None:
    """Create a dag run for the specified dag."""
    api_client = get_current_api_client()
    try:
        user = getuser()
    except AirflowConfigException as e:
        log.warning("Failed to get user name from os: %s, not setting the triggering user", e)
        user = None
    try:
        message = api_client.trigger_dag(
            dag_id=args.dag_id,
            run_id=args.run_id,
            conf=args.conf,
            logical_date=args.logical_date,
            triggering_user_name=user,
            replace_microseconds=args.replace_microseconds,
        )
        AirflowConsole().print_as(
            data=[message] if message is not None else [],
            output=args.output,
        )
    except OSError as err:
        raise AirflowException(err)

```

- `@cli_utils.action_cli`
  - Airflow CLI 명령 실행 시 공통 처리(ex. 예외 처리, 로깅)를 붙이는 데코레이터
- `@providers_configuration_loaded`
  - Provider 관련 설정을 로드한 뒤 함수를 실행하도록 하는 데코레이터
- `api_client = get_current_api_client()`
  - 현재 설정된 Airflow API client를 가져옴(이 client가 trigger_dag()를 수행할 것)
  - Airflow API client : 보통 CLI 프로세스 안에서 만들어진 Python client 객체
  - CLI가 직접 DagRun을 만들지 않고, API client에게 책임을 위임하는 것
- `user = getuser()`
  - 실행한 OS 사용자를 가져옴
- `message = api_client.trigger_dag(…)`
  - DagRun 생성 요청을 API client에게 위임
  - `dag_id=args.dag_id`: 실행할 Dag ID
  - `run_id=args.run_id` : 사용자가 지정한 DagRun ID
  - `conf=args.conf`: `--conf`로 전달한 JSON 문자열
  - `logical_date=args.logical_date`: DagRun의 logical date
  - `triggering_user_name=user`: CLI를 실행한 OS 사용자명
  - `replace_microseconds=args.replace_microseconds`: logical date의 microsecond 처리 여부
- `conf=args.conf`
  - 이 함수는 args.conf를 여기서 검증하지 않음
  - `--conf '[1]'`, `--conf '"str"'`, `--conf '1'` 같은 값이 들어와도 이 단계에서는 그대로 다음 계층으로 넘어감
- `AirflowConsole().print_as(…)`
  - 결과 메세지를 콘솔에 출력

## 📌 이 함수에서 중요한 포인트

### 1. 이 함수는 직접 DagRun을 만들지 않음

- dag_trigger()는 DagRun 생성 로직을 직접 수행하지 않음
- Airflow api client에게 trigger_dag() 요청을 통해, DagRun 생성 책임을 위임

### 2. `conf` 검증이 없음

- `args.conf`가 생략된건지, 빈 문자열인지, 숫자 JSON 문자열인지 등 판단하지 않고 `trigger_dag()`에 인자로 넘김
- 따라서 `conf` 타입 정책을 맞추려면, 이 함수에서 검증하거나, `trigger_dag()` 에서 검증해야함

### 3. `args.conf is not None` 관점이 중요

- CLI에서 `--conf`가 아예 생략된 것과 사용자가 명시적으로 빈 값을 넣은 것은 다름
- `None`, `""`, `{}` 같은 값이 모두 의도와 다르게 섞일 수 있기 때문
