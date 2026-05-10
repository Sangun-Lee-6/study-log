# Python Design Patterns

> 링크 : https://python-patterns.guide/

우선 아래 7개 패턴을 먼저 학습

> 1. Composition Over Inheritance
> 2. Sentinel Object Pattern
> 3. Factory Method / callable factories
> 4. Iterator Pattern
> 5. Decorator Pattern
> 6. Adapter Pattern
> 7. Global Object / Import-time I/O

### 디자인패턴 설명

---

| 우선순위      | 봐야 할 주제                                       | 이유                                                                                               |
| ------------- | -------------------------------------------------- | -------------------------------------------------------------------------------------------------- |
| **필수**      | Composition Over Inheritance                       | Airflow 같은 대형 코드베이스에서 상속 남용을 피하고 조합으로 확장하는 사고가 중요                  |
| **필수**      | Sentinel Object Pattern                            | `None`과 “값이 제공되지 않음”을 구분해야 하는 API/CLI/validation 코드에서 자주 중요                |
| **필수**      | Factory Method / callable factories                | Airflow operator, hook, executor, parser, client 생성 구조를 읽는 데 도움                          |
| **필수**      | Iterator Pattern                                   | DAG, TaskInstance, DagRun, collection response, file scan, manifest 처리에서 반복 구조를 자주 다룸 |
| **필수**      | Decorator Pattern                                  | Python decorator와 wrapper 구조를 구분해서 이해해야 함                                             |
| **강력 추천** | Adapter Pattern                                    | 외부 API, DB, CLI, HTTP client, storage client를 내부 인터페이스에 맞추는 사고에 중요              |
| **강력 추천** | Global Object / Import-time I/O                    | Airflow DAG 파싱, plugin import, test 안정성에서 매우 중요                                         |
| **선택**      | Builder Pattern                                    | 복잡한 config/test fixture 생성 시 유용하지만 당장 필수는 아님                                     |
| **나중**      | Bridge, Composite, Flyweight, Prototype, Singleton | 개념은 알면 좋지만 지금 목표 대비 우선순위 낮음                                                    |

---

### 1. Composition Over Inheritance

- Airflow, 데이터 파이프라인, 백엔드에서 중요한 질문
  - “이걸 상속으로 해결할 문제인가, 조합으로 해결할 문제인가?”
- 예를 들어 Airflow 기여에서 `Backfill`, `DagRun`, `API response model`, `CLI operation`을 다룰 때 상속 구조를 무리하게 늘리기보다 작은 함수, 명확한 모델, 명시적 의존성으로 분리하는 감각이 중요함

---

### 2. Sentinel Object Pattern

- 데이터 엔지니어링 코드에서 자주 마주치는 상황
  ```python
  def load_data(start_date=None):
      ...
  ```
- 여기서 `None`이 의미하는 게 애매함
  - 사용자가 값을 안 줬다는 뜻인가?
  - 명시적으로 `None`을 준 것인가?
  - 기본값을 쓰라는 뜻인가?
  - 전체 기간을 의미하는가?
- Airflow의 API/CLI/validation 코드에서도 이런 구분이 중요
- 특히 `dag_run_conf`, optional config, dry-run parameter 같은 곳에서 없음과 빈 값과 명시적 null을 구분해야 함

---

### 3. Factory Method / Callable Factories

- 코드 안에서 구체 클래스를 직접 넣기보다 생성 책임을 외부에서 주입
- 예시
  ```python
  def create_client(client_factory):
      return client_factory()
  ```
- 이 패턴을 알면 다음을 더 잘 이해할 수 있음
  - 테스트에서 fake client 주입
  - DB session 생성 분리
  - API client 추상화
  - Hook / Operator / Executor 생성 흐름
  - Airflow 내부 dependency injection 스타일

---

### 4. Iterator Pattern

- 데이터 엔지니어링을 하다보면 반복 가능한 구조가 많이 발생함
  - DagRun candidates
  - API pagination
  - backfill 대상 날짜 목록
  - …
- 좋은 파이썬 코드는 리스트를 무조건 메모리에 다 올리는 게 아니라, 필요한 경우 iterator/generator로 흐름을 만드는 것
  ```python
  def iter_manifest_paths(root):
      for path in root.glob("**/manifest.json"):
          yield path
  ```
- 이런 사고가 생기면 파이프라인 코드가 더 읽기 쉽고, 테스트하기 쉬워짐

---

### 5. Global Object / Import-time I/O

- Airflow는 DAG 파일을 계속 parse함 → 따라서 import 시점에 무거운 작업을 하면 문제가 생김
- import-time side effect를 줄이고, 실행 시점에 명시적으로 I/O를 수행해야 함
- 예시
  ```python
  def get_connection():
      return duckdb.connect("/path/to/mart.duckdb")
  ```

---
