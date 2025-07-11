# 섹션 13. 다형성과 설계

# 1. 좋은 객체 지향 프로그래밍이란?

✅ 객체 지향의 특징

1. 추상화
2. 캡슐화
3. 상속
4. 다형성

→ 이 중 다형성이 제일 중요함

✅ 객체 지향 프로그래밍(OOP)

- 프로그램을 여러 개의 독립된 단위인 `객체`의 모임으로 보는 것
- 각 `객체`는 `메세지`를 주고 받고 데이터를 처리(`협력`)
- OOP는 프로그램을 유연하고 변경이 쉽게 만들어줌
- 프로그램을 유연하고 변경이 쉽게 만들어주는 방법 ⇒ 다형성

🚙 실세계 비유 : 자동차와 운전자

- 자동차의 역할(인터페이스)에 따라 자동차를 만들어두면(ex. 엑셀, 브레이크, 전조등)
- 운전자는 여러 자동차의 구현(아반떼, K3 등)을 똑같이 사용 가능
- 새로운 자동차가 나와도 운전자는 운전 가능
- 운전자가 다른 지역에서 차를 빌릴 때, 다른 자동차로 운전 가능

✅ 역할과 구현을 분리했을 때 장점

- 단순해지고 유연해지며 변경이 쉬움
- 클라이언트는 대상의 역할(인터페이스)만 알면 되고 내부 구조는 몰라도 됨
- 클라이언트는 대상의 내부 구조나 대상 자체가 변경되어도 영향 없음

🤖 자바 언어에서 역활과 구현을 분리

- 다형성을 활용
- 역할 = 인터페이스, 구현 = 인터페이스를 구현한 클래스, 객체
- 객체 설계 시 역할(인터페이스)를 먼저 부여하고, 그 역할을 수행하는 객체를 만들기

✅ 객체의 협력

- 수 많은 객체는 서로 협력 관계를 갖음

✅ 다형성의 본질

- 인터페이스를 구현한 인스턴스를 실행 시점에 유연하게 변경 가능
- 객체 사이의 협력 관계를 바탕으로 다형성을 이해해야함
- 본질 : 클라이언트를 변경하지 않고 서버 구현 기능을 유연하게 변경 가능

✅ 인터페이스가 변하지 않도록 설계하는 것이 중요함

✅ 참고

- 디자인 패턴의 대부분은 다형성을 활용
- 스프링의 IoC, DI도 결국 다형성을 활용

# 2. 다형성 - 역활과 구현 예제 1

✅ 다형성 없이, 역할과 구현 구분이 없는 코드

🤖 `CarMain1`

- 운전자 클래스 안에 멤버 변수로 k3Car가 있음
- 운전자 클래스에 k3Car를 주입해서 인스턴스를 운전 가능

# 3. 다형성 - 역활과 구현 예제 2

🤖`CarMain1`

- 새로운 차량을 추가하는 요구사항
- 문제 : 새로운 자동차가 추가되면 Driver 클래스 코드를 수정해야함
- 문제의 원인 : 역할과 구현이 분리되어 있지 않음

# 4. 다형성 - 역활과 구현 예제 3

🤖 `CarMain1`

- 현재 Driver 클래스는 각 인스턴스(k3Car, model3Car)에 의존
- (여기서 `의존한다` == `그 존재를 알고 있음`)
- ∴ 새로운 자동차가 추가되면 Driver 클래스를 수정해야하는 문제 발생

🤖 `CarMain2`

- Driver 클래스는 Car 인터페이스에만 의존하고 각 자동차 인스턴스의 내부 구현은 모름
- Car는 인터페이스일 뿐, 실제 기능은 K3Car에 구현되어 있음
- ∴ 새로운 자동차가 추가돼도 Driver 클래스는 수정 없음, Car 인터페이스에만 의존하므로

# 5. OCP(Open-Closed Principle) 원칙

✅ OCP 원칙

- 객체 지향 설계 원칙 중 하나
- 기존 코드의 수정 없이 새로운 기능을 추가할 수 있어야함을 의미
- Open for extension : 새로운 기능의 추가나 변경 사항이 생겼을 때 기존 코드는 확장할 수 있어야함
- Closed for modification : 기존의 코드는 수정되지 않아야함

🤖 `CarMain2`

- 현재 코드에 새로운 자동차를 추가해도, 이를 사용하는 클라이언트, 즉 Driver 클래스는 수정 없음
- Open for extension : Car 인터페이스로 새로운 차량 추가 가능
- Closed for modification : Driver 클래스(Car 인터페이스를 사용하는 클라이언트)를 수정하지 않아도 됨