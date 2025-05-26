# 섹션 5. 생성자

# 1. 생성자 - 필요한 이유

🤖 `MethodInitMain1`

- MemberInit 객체를 초기화하는 코드가 중복됨

🤖 `MethodInitMain2`

- 객체 초기화 코드를 메서드로 중복 제거
- `initMember` 라는 메서드로 중복을 제거했지만, 이 메서드에서 사용하는 데이터(멤버 변수)는 메서드와 분리되어 있음
- 데이터와 해당 메서드를 하나로 묶는 것이 객체 지향

# 2. this

✅ 멤버 변수명과 메서드의 파라미터명이 같을 때, 이 둘을 구분하기 위해 this 사용

- this는 인스턴스 자신의 참조값

```java
public class MemberInit {
    String name;
    int age;
    int grade;

    // 추가
    void initMember(String name, int age, int grade) {
        this.name = name;
        this.age = age;
        this.grade = grade;
    }
}
```

✅ this 생략 가능한 경우 : 멤버 변수명과 파라미터명이 다를 때

- 가까운 지역 변수에 해당 변수명을 찾고 없다면 멤버 변수에서 해당 변수명을 찾음

```java

public class MemberThis {
    String nameField;

    void initMember(String name) {
        nameField = name; // this 생략
    }
}
```

# 3. 생성자 - 도입

✅ 생성자

- 객체 생성 후 바로 초기화를 하는 경우가 많음
- 따라서 OOP 언어는 생성자라는 메서드를 제공함
- 생성자를 사용하면 → 객체가 만들어질 때 즉시 필요한 기능을 수행 가능

✅ 생성자의 특징

- 생성자는 특별한 메서드
- 생성자명은 클래스명과 같아야함 → ∴ 첫 글자도 대문자로 시작함
- 생성자는 반환타입이 없음

✅ 생성자가 있는 클래스는 객체 생성시 초기값을 넣어줘야함

→ 그러면 객체를 생성하면서 초기화까지됨

🤖 `MemberConstruct`, `ConstructMain1`

- 생성자를 적용한 코드

✅ 생성자 장점 1 : 중복 호출 제거

- 생성자를 안쓴다면 → 객체 생성 후, 초기화 메서드를 한번 더 호출해야함
- 생성자를 사용하면 → 객체를 생성하면서 같이 초기화 가능

✅ 생성자 장점 2 : 필수값 입력을 보장할 수 있음

- 생성자를 안쓴다면 → 객체가 빈 데이터로 초기화됨(에러 없이 프로그램이 실행)
- 생성자를 사용하는 경우 → 객체를 초기화를 안한다면 컴파일 에러 발생

# 4. 기본 생성자

✅ 기본생성자

- 매개변수가 없는 생성자
- 클래스에 생성자가 없다면 자바 컴파일러는 기본 생성자를 만듦
- 생성자가 하나라도 있다면 기본 생성자를 만들지 않음

# 5. 생성자 - 오버로딩과 this()

🤖 `MemberConstruct2`, `ConstructMain2`

- 생성자를 하나 더 추가함
- 오버로딩을 통해 원하는 생성자를 호출 가능

🤖 `MemberConstruct3`

- `MemberConstruct2` 에는 [this.name](http://this.name) 코드가 중복됨
- 이를 해결하기 위해 `this()` 사용 가능

```java
   MemberConstruct3(String name, int age) {
        this(name, age, 50);
    }

    // 생성자
    MemberConstruct3(String name, int age, int grade) {
        System.out.println("생성자 호출 name: " + name + " age: " + age + " grade: " + grade);
        this.name = name;
        this.age = age;
        this.grade = grade;
    }
```

✅ this()

- 자신의 생성자를 호출함
- 생성자 구현부의 `첫번째 줄`에서만 사용 가능