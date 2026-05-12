# Sentinel Object Pattern

---

> 링크 : [https://python-patterns.guide/python/sentinel-object/#sentinel-value](https://python-patterns.guide/python/sentinel-object/#sentinel-value)

- 전통적인 센티널 값 패턴을 Python 방식으로 변형한 패턴
  - 값이 없음, 아직 지정되지 않음, 특수 상태를 표현하기 위해 고유한 객체를 사용하는 패턴
- sentinel : 보초, 감시자 파수꾼
  - → 여기선 특정 상태를 감시하기 위해 세워둔 특별한 객체의 의미
  - → 실제 데이터가 아니라 `값이 없음`과 같은 상태를 구분하기 위한 객체
- 센티널 객체 패턴의 필요성
  - 프로그래밍 실무에선 항상 값이 명확히 존재하지 않음
  - 따라서 객체의 속성이나 객체 자체가 없을 수 있는 경우를 처리하기 위한 패턴이 필요함

---

## 결론

- 센티널 객체 패턴은 Python 표준 라이브러리와 그 외 여러 코드에서 사용되는 표준적인 Python스러운 접근 방식
- 이 패턴에서는 보통 Python 내장 객체인 `None`을 사용함
  - `None` 자체가 유효한 값인 상황에서는, `object()`로 만든 센티널 객체를 사용해서 `“데이터가 없음"`을 표현할 수 있음

---

## 1. 센티널 값

### 🔹 센티널 값

- 센티널 값 패턴 예시 : `str.find()`, `str.index()`
  - 이 방식은 코드 한 줄과 들여쓰기 한 단계 정도를 줄여줌
- 예시 : 센티널 값

  ```python
  try:
      i = a.index(b)
  except:
      return

  # 반면

  i = a.find(b)
  if i == -1:
      return
  ```

  - `-1`은 정수지만 사전에 약속된 `“찾지 못했음”`이라는 특별한 의미가 있음
  - 그러나 프로그램이 `-1`을 반환했는데, 이를 확인하지 않고 문자열의 인덱스로 사용하려고 하면 문제 발생

- `str.find()`가 만들어질 때는 C/Unix 관습으로 실패하면 불가능한 위치 값을 반환해서 `-1`을 반환했음
  - 오늘날 새로 만들어졌다면 센티널 객체 패턴을 사용했을 가능성이 높음
  - 즉, 찾지 못했을 때 `-1`이 아니라 `None`을 반환
- 센티널 값을 사용하는 곳
  - Go 언어 : 단순 참조 대신 항상 문자열을 반환하는 스타일
  - Django 프레임워크 : 문자열 기반 필드에서는 null 사용을 피하라고 권장

### 🔹 센티널 값과 센티널 객체 패턴

- 센티널 값은 특정 값에 의미를 부여하는 방식
  - 센티널 객체 패턴은 그 값이 실제 데이터와 헷갈리는 문제를 해결하기 위해 고유한 객체 자체를 사용하는 방식
- 즉, 센티널 객체 패턴은 센티널 값을 더 안전하게 만든 Python식 버전

---

## 2. 널 포인터 패턴

- Python 코드 레벨에서는 널 포인터 패턴을 사용할 수 없음
  - Python의 None은 C의 NULL처럼 아무것도 가리키지 않는 포인터가 아니라 실제로 존재하는 객체이므로

### 🔹 이름, 객체, 주소

- 파이썬에서 변수는 값을 직접 담고 있는 상자가 아니라 객체를 가리키는 이름표
  - `x = 10`
    - `이름 x → 정수 객체 10`
  - `x = None`
    - `이름 x → None 객체`
- 파이썬에서 `None`은 아무것도 없음이 아니라 `None`이라는 실제 객체
  - 따라서 `x=None`이라도, 여전히 어떤 객체를 가리키는 상태

---

### 🔹 C의 포인터와 `NULL`

- C 언어의 포인터 : 메모리 주소를 저장하는 변수
  - `int *p;`
  - `p`는 정수값 자체가 아니라, 정수가 저장된 메모리 위치를 가리키는 변수
  - 의미 : `포인터 p → 어떤 메모리 주소`
- C에서 포인터가 아무것도 가리키지 않을 수 있음 : 이때 보통 `NULL`을 사용
  - `p = NULL;`
  - 의미 : `포인터 p → (아무것도 가리키지 않음)`
  - C에서 `NULL`은 “현재 이 포인터는 유효한 객체를 가리키지 않는다”는 특별한 값

### 🔹 `NULL`과 segmentation fault

- C에서 포인터가 NULL인데 그 포인터를 사용하려고 하면 문제가 생김

  ```c
  int *p = NULL;
  printf("%d", *p); // p가 가리키는 곳에 가서 값을 읽기
  ```

  - 그런데 `p`는 아무것도 가리키지 않고, 프로그램이 접근하면 안 되는 메모리 영역에 접근하면서 segmentation fault가 발생할 수 있음

- 따라서 C에서는 `NULL`이 될 수 있는 포인터를 사용하기 전에 항상 확인해야함
  - `if (p != NULL) {   // p 사용 }`

### 🔹 Python에서 널 포인터 패턴은 없다

- 파이썬의 변수는 항상 어떤 객체를 가리킴
  - 따라서 `x = None`은 아무것도 가리키지 않는게 아니라 `None`객체를 가리키는 것
  - Python의 `None`은 C언어의 `NULL` 포인터가 아님

### 🔹 Python 내부 C 코드에서는 NULL을 사용한다

- 파이썬은 주로 C로 구현되어 있음
  - 대표 구현체 : CPython
- Python 객체를 C에서 다룰 때는 보통 `PyObject *` 라는 포인터를 사용
  - `PyObject *obj;`
  - 의미 : `obj`는 Python 객체를 가리키는 C 포인터
- 그런데 Python의 모든 값은 실제 객체
  - 따라서 C 입장에선 정상적인 파이썬 반환값이 있다면 항상 `PyObject *`를 반환할 수 있음
  - 파이썬 함수가 `None`을 반환해도 C 입장에서는 항상 `None` 객체를 가리키는 유효한 주소를 반환하는 것

### 🔹 따라서 `NULL`을 특별한 신호로 사용 가능

- 파이썬 객체들은 항상 대상이 있고, 모두 실제 주소가 있음
- 따라서 C 내부에서는 `NULL`을 다른 의미로 남겨둘 수 있음
  - `NULL` 반환 = 정상적인 Python 객체를 반환한 것이 아니라, 예외 발생
- 예시

  ```c
  PyObject *my_repr = PyObject_Repr(obj); // PyObject_Repr(obj) : C API 함수

  if (my_repr == NULL) {
      // 예외가 발생했다는 뜻
  }
  ```

  - 정상이라면 `my_repr ───> Python 문자열 객체`
  - 예외가 발생했다면 `my_repr = NULL`

- 즉, NULL은 여기서 센티널 값으로 쓰임

### 🔹 센티널 객체 패턴과 널 포인터 패턴

- 센티널은 일반 값과 구분되는 특별한 신호
- 파이썬에서는 모든 변수가 항상 존재하는 객체를 가리키므로 널 포인터 패턴을 쓸 수 없음 → 따라서 CPython 내부 C API에서 NULL을 센티널로 사용 가능

| 상황         | 센티널                      | 의미                        |
| ------------ | --------------------------- | --------------------------- |
| C 포인터     | `NULL`                      | 아무것도 가리키지 않음      |
| Python C API | `NULL`                      | 예외 발생                   |
| Python 코드  | `None`                      | 값 없음                     |
| Python 코드  | `object()`로 만든 고유 객체 | 정말로 값이 없거나 미지정됨 |

---

## 3. 널 객체 패턴

### 🔹 널 객체와 널 객체 패턴

- 널 객체 : 실제로 존재하는 유효한 객체지만, 빈 값이나 존재하지 않는 항목을 표현하는 객체
- 널 객체 패턴 : None을 넣고 매번 `if obj is None`으로 검사하는 대신, `없음`을 표현하는 진짜 객체를 만들어 일반 객체처럼 사용하게 하는 패턴

### 🔹 널 객체 패턴 예시

- 일반적으로 `값이 없음`은 이렇게 표현함 → 사용할 때마다 확인 필요
  ```python
  employee.manager = None
  ```
  ```python
  if employee.manager is None:
      print("관리자 없음")
  else:
      print(employee.manager.display_name())
  ```
- 널 객체 패턴은 이 `None`을 없애고 관리자가 없음을 나타내는 객체를 넣음 → 코드가 단순해짐

  ```python
  NO_MANAGER = Person(name="no acting manager")

  employee.manager = NO_MANAGER
  ```

  ```python
  print(employee.manager.display_name())
  ```

  - manager가 실제 존재하든, `NO_MANAGER`든 같은 방식으로 다룰 수 있게 만드는 것

### 🔹 센티널 객체와 관계

- 널 객체는 특별한 종류의 센티널 객체로 볼 수 있음

| 구분        | 목적                        | 사용 방식                    |
| ----------- | --------------------------- | ---------------------------- |
| 센티널 객체 | “값 없음/미지정”을 감지     | `if value is MISSING:`       |
| 널 객체     | “없음”도 정상 객체처럼 동작 | `value.method()` 그대로 호출 |

- 센티널 객체의 목적은 구분하기 위한 객체

  ```python
  MISSING = object()

  if value is MISSING:
      ...
  ```

- 널 객체의 목적은 그냥 사용해도 안전한 객체
  ```python
  NO_MANAGER.display_name()
  ```
- 즉, 센티널 객체가 “특수 상태임”을 알려주는 신호라면, 널 객체는 “특수 상태지만 일반 객체처럼 행동해서 코드가 깨지지 않게 하는 객체”

### 🔹 Python `logging.NullHandler` 예시

- Python 표준 라이브러리의 `logging.NullHandler`가 널 객체 예시
- 일반 핸들러는 로그를 파일이나 콘솔에 출력함
  ```python
  logger.addHandler(logging.StreamHandler())
  ```
- `NullHandler`는 핸들러처럼 붙일 수 있지만, 실제로는 아무 로그도 남기지 않음

  ```python
  logger.addHandler(logging.NullHandler())
  ```

  - 즉, “로그 핸들러가 없음”을 `None`으로 표현하지 않고 아무 일도 하지 않는 핸들러 객체로 표현한 것

### 🔹 널 객체 패턴의 장점

- 코드 반복 줄이기
  - `None` 체크 감소 ⇒ `if obj is None` 같은 코드 반복을 줄일 수 있음
- 코드 흐름 단순화
  - 호출하는 쪽이 객체 존재 여부를 덜 신경쓰게 됨
- 의미 명확화
  - `None`보다 `NO_MANAGER`가 도메인 의미를 더 잘 표현
- 런타임 오류 감소
  - `None.display_name()` 같은 실수를 줄일 수 있음

### 🔹 단점/주의점

- 계산/통계에서는 널 객체 사용을 주의해야 함
- 예시 : NO_MANAGER를 일반 사람처럼 집계하면 평균, 개수, 비율 왜곡
  ```python
  employees = [
      Person("Alice"),
      Person("Bob"),
      NO_MANAGER,
  ]
  ```
- 따라서 널 객체 사용 조건
  - 없음도 일반 객체와 같은 인터페이스로 안전하게 처리할 수 있을 때
- 널 객체 사용이 부적합한 경우
  - 없음을 반드시 별도로 구분해야 하거나, 집계/통계/검증에서 실제 데이터와 섞이면 안 될 때

---

## 4. 센티널 객체

- 센티널 객체 패턴은 `None` 만으로는 값 없음을 안전하게 표현하기 어려울 때, `object()`로 만든 고유한 객체를 특별 신호로 사용하는 패턴

### 🔹 기본 센티널은 `None`

- 파이썬에서 가장 흔한 센티널은 `None`(대부분 `None`으로 충분함)
- 예시 : `None`의 의미는 `값이 없다`

  ```python
  user = None

  if user is None:
      print("사용자 없음")
  ```

### 🔹 `None`만으로 부족한 경우

- None 자체가 정상 데이터인 경우
- 예시 : 캐시에서 None
  ```python
  cache = {
      "user_1": None
  }
  ```
- None은 2가지로 해석될 수 있음
  1. `user_1`이 캐시에 없음(cash miss)
  2. `user_1`이 캐시에 있고 저장된 값이 None(cached value is None)
  - 따라서 둘 다 None으로 표현하면 구분이 안됨

### 🔹 그래서 고유한 센티널 객체를 생성

- `object()`로 특별한 객체를 하나 만들어서 문제 해결

  ```python
  MISSING = object()
  ```

  ```python
  result = cache.get(key, MISSING)

  if result is MISSING:
      print("캐시에 없음")
  else:
      print("캐시에 있음")
  ```

- `cache.get(key, MISSING)` : `key`가 없으면 `MISSING`을 반환하라
  - 이렇게 되면 다음 2가지 상황을 정확하게 구분 가능

  ```python
  cache = {
      "user_1": None
  }

  MISSING = object()

  result = cache.get("user_1", MISSING)

  if result is MISSING:
      print("캐시에 없음")
  else:
      print("캐시에 있음")
  ```

  - 결과 : `캐시에 있음`
    - `"user_1"`은 캐시에 있고, 그 값이 `None`일 뿐이기 때문

### 🔹 선택적 인자에서도 필요함

- 함수 인자에서도 비슷한 문제가 생김
  ```python
  def update_user(name=None):
      ...
  ```
- 위 함수는 아래 두 경우를 구분할 수 없음

  ```python
  update_user()
  update_user(name=None)
  ```

  - 둘 다 함수 안에서는 `name is None`

- 그러나 두 함수의 의미는 다를 수 있음
  | 호출 | 의미 |
  | ------------------------ | --------------------------------- |
  | `update_user()` | name을 아예 전달하지 않음 |
  | `update_user(name=None)` | name을 명시적으로 None으로 설정함 |
- 따라서 이 둘을 구분해야 한다면 센티널 객체를 사용

  ```python
  NOT_GIVEN = object()

  def update_user(name=NOT_GIVEN):
      if name is NOT_GIVEN:
          print("name 인자가 전달되지 않음")
      elif name is None:
          print("name을 명시적으로 None으로 전달함")
      else:
          print("name:", name)
  ```

- 사용 예시

  ```python
  update_user()
  # name 인자가 전달되지 않음

  update_user(name=None)
  # name을 명시적으로 None으로 전달함

  update_user(name="Lee")
  # name: Lee
  ```

---

## 5. 센티널 객체의 핵심은 `==`가 아니라 `is`

- 센티널 객체는 값이 중요한 게 아니라, 이 객체가 해당 객체인지가 중요함
  - 따라서 `==`가 아니라 `is`로 비교

  ```python
  if value is MISSING:
      ...
  ```

  - `==`는 “값이 같은가?”를 비교하고, `is`는 “같은 객체인가?”를 비교

- 센티널 객체는 고유한 객체 하나를 만들어두고, 그 객체와 같은 객체인지 확인하는 방식

---

## 6. 센티널 값과 센티널 객체의 차이

| 구분        | 예시                 | 비교 방식 | 문제                           |
| ----------- | -------------------- | --------- | ------------------------------ |
| 센티널 값   | `-1`, `"UNKNOWN"`    | `==`      | 실제 데이터와 충돌 가능        |
| 센티널 객체 | `MISSING = object()` | `is`      | 고유 객체라 충돌 가능성이 낮음 |

- 예를 들어 `-1`은 센티널 값

  ```python
  index = text.find("abc")

  if index == -1:
      print("못 찾음")
  ```

  - 그러나 -1은 여전히 정수라서, 실수로 인덱스로 사용 가능

- 반면 센티널 객체는 일반 데이터로 우연히 등장하기 어려움

  ```python
  MISSING = object()

  if value is MISSING:
      print("값 없음")
  ```

## 7. 파이썬에서 센티널 객체 패턴이 사용되는 이유

- 센티널 객체 패턴은 파이썬 전용 패턴은 아니지만, 파이썬에서 자주 보이는 패턴
  - ∵ 파이썬에서 `None`이 `null`에 해당되는 개념이지만, `None`도 객체라서 유효한 값일 수 있으므로
- `None`은 `인자가 생략됨`과 `None이 명시적으로 전달됨`을 구분하지 못함
  - 따라서 센티널 객체 패턴을 사용
- `is`가 있어서 정확히 해당 객체인지 구분 가능

## 8. 핵심 요약

| 개념            | 핵심 의미                            | 실무 감각                                 |
| --------------- | ------------------------------------ | ----------------------------------------- |
| Sentinel Value  | 특정 값에 특별한 의미를 부여         | `-1`, 빈 문자열, `"UNKNOWN"` 같은 값      |
| Sentinel Object | 고유 객체 자체로 특별 상태를 표현    | `object()`로 만든 값, `is`로 비교         |
| `None`          | Python의 대표적인 기본 센티널        | “값 없음”의 기본 표현                     |
| Null Object     | 없음을 나타내지만 정상 객체처럼 동작 | 조건문을 줄이고 코드 흐름을 단순화        |
| `is` 비교       | 같은 객체인지 확인                   | 센티널 객체 비교에는 `==`보다 `is`가 핵심 |

> `None`이 “값 없음”으로 충분하면 `None`을 쓰고, `None`도 유효한 데이터라면 `object()`로 고유 센티널을 만들어 `is`로 비교한다.
