# 섹션 6. 싱글톤 컨테이너

# 1. 웹 애플리케이션과 싱글톤

> ✅ 웹 애플리케이션 환경에서 싱글톤이 중요한 이유
⇒ 싱글톤으로 사용자 요청마다 객체를 새로 생성하지 않고 인스턴스를 재사용해서 메모리 낭비가 적기에 중요함

✅ 스프링은 모든 빈을 싱글톤으로 관리해서 이 문제를 해결
>

📌 스프링과 웹 애플리케이션

- 스프링은 기업용 웹 애플리케이션을 위한 프레임워크로 만들어짐
- 웹 환경에서는 여러 사용자의 동시 요청이 일반적임
- 그리고 사용자 요청마다 객체를 새로 생성하면 메모리 낭비가 심각함

📌 실험 : 스프링이 없다면 요청마다 객체가 새로 생성되는가?

- 순수 자바 DI 컨테이너(`AppConfig`)로 실험 : `pureContainer()`
- `memberService()`메서드가 호출될 때마다 새로운 `memberService` 객체 생성
- 이는 참조값을 출력해서 인스턴스가 서로 다른 것을 확인했음

📌 스프링의 문제 해결 방향

- 따라서 같은 요청에 대해선 인스턴스를 재사용하는 구조가 필요
- 이러한 구조를 싱글톤 패턴(Singleton Pattern)이라고 부름

# 2. 싱글톤 패턴

📌 싱글톤 패턴

- 클래스의 인스턴스를 1개만 생성하고, 그 인스턴스를 공유하는 디자인 패턴
- 이를 통해 메모리 낭비 방지, 객체 간 공유/생성 비용 감소

🤖 싱글톤 패턴 구현

- 예제 :`SingletonService`, `SingletonServiceTest`
- static 영역에 1개의 인스턴스 생성
- 외부에 인스턴스를 조회할 수 있는 static 메서드 제공
- private 생성자로 외부에서 인스턴스를 생성하지 못하도록 제한
- 외부에선 다음과 같이 메서드로 인스턴스를 불러와서 사용

```java
SingletonService singletonService1 = SingletonService.getInstance();
```

📌 싱글톤 패턴의 장점

- 메모리 낭비 방지 (매번 인스턴스를 생성 안해도 되므로)
- 다수의 요청 간 객체 공유 가능
- 초기화 비용이 큰 객체를 재사용 가능

📌 싱글톤 패턴의 한계

- 아래와 같은 이유로 싱글톤은 안티패턴으로 취급됨

| 문제점 | 설명 |
| --- | --- |
| 코드 복잡도 증가 | static 필드, 메서드, 생성자 제한 등 구현 방식이 번거로움 |
| DIP 위반 | 클라이언트가 구체 클래스에 직접 의존하게 됨 |
| OCP 위반 위험 | 구현 변경 시 클라이언트 코드도 함께 수정될 가능성 |
| 테스트 어려움 | 객체 재사용으로 인해 상태 격리가 어려움 |
| 유연성 저하 | private 생성자 때문에 상속, 교체 어려움 |

📌 싱글톤 패턴과 스프링

- 객체를 공유한다는 점은 웹 환경에서 중요하지만 위와 같은 단점이 있음
- 스프링은 싱글톤의 장점은 가져가면서 단점을 해결

💡 참고 : `isSameAs()`, `isEqualTo()`

- 테스트 코드에서 사용됨
- `isSameAs()`는 ==, 즉 주소값 비교
- `isEqualTo()`는 두 객체의 내용이 같은지 비교

```java
String a = new String("hello");
String b = new String("hello");

assertThat(a).isEqualTo(b); // 통과 (내용이 같으므로)
assertThat(a).isSameAs(b);  // 실패 (주소가 다르므로)
```

# 3. 싱글톤 컨테이너

📌 스프링의 싱글톤 컨테이너

- 스프링 컨테이너는 앱 전체에서 하나의 인스턴스를 공유하도록 싱글톤 스코프로 빈을 관리함
- 싱글톤 패턴처럼 복잡한 구현 없이, 스프링 컨테이너가 싱글톤 레지스트리 역할 수행

📌 싱글톤 패턴의 자바 DI와 스프링 컨테이너 비교

| 항목 | 순수 자바 DI + 싱글톤 패턴 | 스프링 컨테이너 |
| --- | --- | --- |
| **싱글톤 유지 방식** | 개발자가 직접 구현 (`static`, `private 생성자`) | 스프링이 자동 관리 (싱글톤 레지스트리) |
| **객체 생성 제어** | `getInstance()` 통해 수동 제어 | `@Configuration + @Bean` 으로 선언만 하면 됨 |
| **코드 복잡도** | 싱글톤 관련 코드가 클래스에 침투 (안티패턴 우려) | 비즈니스 로직과 설정 분리 (관심사 분리) |
| **테스트 용이성** | 테스트 어려움 (전역 상태, new 제한) | DI로 외부 주입 가능 → 테스트 유연 |
| **OCP/DIP 만족 여부** | DIP 위반 (구체 클래스 직접 의존) | OCP/DIP 자연스럽게 만족 |
| **확장성/유연성** | 변경 어려움 (생성자 막혀 있음) | 설정 파일만 수정하면 유연하게 대체 가능 |

🤖 실험 : 스프링 컨테이너에서 인스턴스 재사용 여부

- `springContainer()`
- `getBean()`으로 같은 이름을 2번 조회 → 같은 인스턴스임을 확인

📌 싱글톤 컨테이너의 장점

1. 싱글톤 패턴에 필요한 구현 없이 객체 재사용 가능(ex. `private`, `static getInstance()`등)

2. DIP, OCP를 지키며 싱글톤 사용 가능

👉 1. 싱글톤 패턴 없이 객체 재사용

- 스프링 컨테이너를 사용하면 `MemberServiceImpl` 에 `static, private 생성자, getInstance()` 등 없이도 객체 재사용 가능

```java
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // ...
}
```

👉 2. DIP, OCP 지키며 싱글톤 사용 가능

- 스프링 없이 싱글톤을 사용하려면, 아래처럼 DIP 위반

```java
public class AppConfig {
    private final MemberRepository memberRepository = MemoryMemberRepository.getInstance();

    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository);
    }
}
```

- 스프링을 사용하면 싱글톤을 사용하면서 DIP를 지킬 수 있음

```java
@Configuration
public class AppConfig {
    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository(); // 구현체 교체 유연
    }

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }
}
```

💡 참고 : 스프링은 기본적으로 싱글톤 스코프

- 실무에서 99%는 싱글톤 스코프 사용
- 매번 새로운 객체를 생성하는 프로토타입 스코프도 사용 가능

# 4. 싱글톤 방식의 주의점

📌 싱글톤 방식의 주의점

- 스프링 컨테니어도 하나의 인스턴스를 여러 클라이언트가 공유
- 따라서 싱글톤 객체는 무상태(stateless)로 설계해야함

⚠️ 싱글톤에서 피해야 할 설계

1. 특정 클라이언트에 의존적인 필드

- ex. 클라이언트에 의해 값이 바뀔 수 있는 필드

2. 공유 객체에 값을 저장하고 꺼내는 구조

- ex. `StatefulService`에서 `this.price = price`와 같은 코드

📌 싱글톤에서 지켜야 할 원칙

- 지역 변수, 파라미터, ThreadLocal 등을 사용해서 공유를 피하기
- 공유 필드는 read-only(쓰기가 가능하면 안됨)
- 계산 결과는 반드시 지역 변수로 처리

🤖 테스트 : 싱글톤 객체의 공유 필드 값을 덮어씀

- `statefulServiceSingleton`
- 마지막 요청이 이전 요청의 값을 덮어씀

💡 실무에서 마주칠 수 있는 장애 유형이므로 반드시 주의하기

📎 참고 : stateless 설계

- 실제 비즈니스 로직에서는 아래처럼 상태 저장 없이 반환값 처리하는 것이 정석

```java
// 잘못된 설계 (상태 저장)
public void order(String name, int price) {
    this.price = price;
}

// 좋은 설계 (무상태)
public int order(String name, int price) {
    return price;
}
```

# 5. @Configuration과 싱글톤

**🧐 `AppConfig.java`를 보면 싱글톤이 깨질 것처럼 보임**

- ∵ `new MemoryMemberRepository()`가 3번 호출되므로, 3개의 객체가 생성될 것 같아서
- 그러나 실제 테스트를 해보면 MemoryMemberRepository는 1번만 생성됨 → ∴ 싱글톤

**🤖 테스트 :`configurationTest()`**

- `MemberService`, `OrderService`, `AppConfig.getBean("memberRepository")` 모두 동일한 인스턴스를 사용하고 있음

# 6. @Configuration과 바이트코드 조작의 마법

📌 스프링의 내부 처리 방식

- 스프링은 `@Configuration`이 붙은 설정 클래스를 바탕으로 프록시 객체(`AppConfig@CGLIB`)를 만듦
- 이때 CGLIB 바이트 코드 조작 라이브러리 사용
- 이 프록시 객체는 `@Bean` 메서드를 직접 호출하지 않고, 스프링 컨테이너를 먼저 조회해서 싱글톤 보장

📌 `AppConfig@CGLIB`의 내부 동작 (예상 코드)

- AppConfig 코드 상으론, 여러 번 `new`를 호출해도 스프링 컨테이너에 등록된 객체를 반환함
- 따라서 싱글톤 보장

```java
@Bean
public MemberRepository memberRepository() {
    if (스프링 컨테이너에 이미 등록됨) {
        return 기존 빈 반환;
    } else {
        MemoryMemberRepository 객체 생성 후 등록 및 반환;
    }
}
```

🤖 테스트 : `@Configuration`이 없는 경우

- MemoryMemberRepository 인스턴스가 3개 생성됨
- 싱글톤이 깨짐

📌 정리 : `@Configuration`을 써야하는 이유

- 스프링이 싱글톤을 보장하기 위해 → 프록시 객체를 만들어서 보장
- `@Bean`만 붙이면 컨테이너에 빈 등록이 되지만, 싱글톤 보장 안됨