# 섹션 7. 컴포넌트 스캔

# 1. 컴포넌트 스캔과 의존관계 자동 주입 시작하기

📌 컴포넌트 스캔과 의존관계 자동 주입이 필요한 이유

- AppConfig 파일에 `@Congifuation + @Bean`을 사용한 수동 등록 방식
- 이 방식은 설정 누락, 코드의 반복, 관리 복잡도 증가 등의 문제
- 스프링은 이런 문제를 해결하기 위해 2가지 기능을 제공 : 컴포넌트 스캔(`@ComponentScan`), 의존관계 자동 주입(`@Autowired`)

🤖 `@ComponentScan` 도입 : `AutoAppConfig.java`

- 이러면 스프링이 `@ComponentScan`이 설정된 클래스부터 패키지 하위의 `@Component` 클래스를 스프링 빈에 등록해줌
- 기존 AppConfig와 달리 수동으로 `@Bean`으로 등록한 클래스가 하나도 없음

```java
@Configuration
@ComponentScan()
public class AutoAppConfig {
}
```

🤖 각 클래스에 `@Component`로 컴포넌트 등록

- `@Component`가 붙은 클래스는 컴포넌트 스캔됨

```java
@Component
public class MemoryMemberRepository implements MemberRepository {}

@Component
public class RateDiscountPolicy implements DiscountPolicy {}
```

- 그러나 `@Component`만 붙이면, 해당 클래스에서 필요한 인스턴스를 주입 받지 못함
- 따라서 `@Autowired`로 의존성 자동 주입

📌 `@Autowired`로 의존성 자동 주입

- 이러면 스프링이 타입 기준으로 자동 매칭해서 클래스에 의존성을 주입
- 여러 객체가 필요해도 모두 자동 주입됨
- 내부적으로는 `ac.getBean(타입)` 호출과 유사한 동작

```java
@Component
public class OrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
}
```

**📌 컴포넌트 등록 규칙 : 빈 이름**

- 기본 : 클래스명에서 맨 앞글자만 소문자로 변경
- 직접 이름 지정 가능 : `@Component("memberService2")`

**📌 인터페이스나 `Member` 클래스에는 컴포넌트 스캔이 필요 없는 이유**

- 컴포넌트 스캔은 스프링이 관리할 객체를 자동으로 등록하는 기능
- 인터페이스는 구현체가 없으므로 스프링이 빈으로 등록할 게 없음
- Member는 비즈니스 도메인 객체 → ∴ 스프링 빈으로 관리할 필요 없음

**🤖 로그에서 컴포넌트 스캔과 `@Autowired` 동작 확인**

- 컴포넌트 스캔 대상 탐색 : `@Component`가 붙은 클래스들이 스캔되어 Bean 등록 후보로 인식됨

```
ClassPathBeanDefinitionScanner --Identified candidate component class: ...RateDiscountPolicy.class
...
```

- 빈 생성 로그 : 각 클래스가 스프링 컨테이너에 싱글톤 빈으로 생성됨

```
Creating shared instance of singleton bean 'rateDiscountPolicy'
..
```

- `@Autowired` 자동 주입 로그 : `@Autowired`가 붙은 생성자에서 타입을 기준으로 빈을 찾아 자동 주입

```
Autowiring by type from bean name 'orderServiceImpl' via constructor to bean named 'rateDiscountPolicy'
...
```

# 2. 탐색 위치와 기본 스캔 대상

📌 컴포넌트 스캔의 탐색 위치 지정

1. 패키지 기준

```java
@ComponentScan(basePackages = "hello.core")
```

- `basePackages`: 탐색을 시작할 패키지의 경로 지정
- 지정한 패키지 하위까지 재귀적으로 스캔
- 복수 패키지 지정 가능

2. 해당 클래스가 속한 패키지를 기준으로 탐색

```java
@ComponentScan(basePackageClasses = AutoAppConfig.class)
```

3. 가장 권장되는 방법

- `@ComponentScan`을 붙인 설정 클래스(AppConfig 등)를 프로젝트 루트에 위치
- 그러면 하위 패키지 전체가 스캔 대상이 됨
- 스프링 부트도 이 방식 채택 : `@SpringBootApplication`에 `@ComponentScan` 포함

**📌 컴포넌트 스캔되는 애노테이션**

- 내부적으로 `@Component`가 포함되어 있어서 스캔됨
- 용도에 맞는 추가 기능이 있음

| 애노테이션 | 용도 |
| --- | --- |
| `@Component` | 컴포넌트 스캔의 핵심 대상 |
| `@Controller` | 스프링 MVC 컨트롤러로 인식됨 |
| `@Service` | 비즈니스 계층으로 의미 부여 (특별 처리 없음) |
| `@Repository` | 데이터 계층으로 인식, 예외 변환 기능 수행 |
| `@Configuration` | 설정 클래스로 인식, 싱글톤 보장 등 부가 처리 |

# 3. 필터

📌 컴포넌트 스캔 필터란?

- 컴포넌트 스캔 시 특정 조건의 클래스만 포함하거나 제외할 수 있는 기능
- 설정 정보에 `includeFilters`, `excludeFilters`를 지정해서 필터링

```java
@ComponentScan(
  includeFilters = @Filter(type = FilterType.XXX, classes = { ... }),
  excludeFilters = @Filter(type = FilterType.XXX, classes = { ... })
)
```

- `includeFilters`: 스캔에 강제로 포함할 대상 지정
- `excludeFilters`: 스캔에서 제외할 대상 지정

📌 FilterType 종류

| 타입 | 설명 | 예시 |
| --- | --- | --- |
| `ANNOTATION` | 애노테이션 기준 (기본값) | `@MyIncludeComponent` |
| `ASSIGNABLE_TYPE` | 특정 클래스나 하위 타입 기준 | `BeanA.class` |
| `ASPECTJ` | AspectJ 표현식 | `org.example..*Service+` |
| `REGEX` | 정규표현식 기반 | `org\.example\.Default.*` |
| `CUSTOM` | `TypeFilter` 인터페이스 구현체 사용 | `MyTypeFilter.class` |

📌 실무 적용 여부

- `includeFilters`: 거의 사용하지 않음 (직접 `@Component` 붙이면 충분)
- `excludeFilters`: 간혹 기존 설정을 임시로 무시할 때 유용
- 스프링 부트에서는 필터 옵션을 거의 건드리지 않고 기본 설정을 따르는 것이 권장됨

# 4. 중복 등록과 충돌

📌 컴포넌트 스캔에서 중복 등록이란?

- 스프링 빈의 이름이 동일한 경우, 스프링은 어떤 빈을 등록해야 할지 모름 → 충돌 발생

1️⃣ 상황별 충돌 처리 : 자동 빈 등록 vs 자동 빈 등록

- 예: `@Component`가 중복되어 같은 이름의 빈이 두 번 등록되는 경우
- 결과: `ConflictingBeanDefinitionException` 발생
- 설명: 자동 등록은 스프링이 중복 처리 기준이 없으므로 예외를 발생시킴

2️⃣ 상황별 충돌 처리 : 수동 빈 등록 vs 자동 빈 등록

- 예: 한쪽은 `@Component`, 다른 한쪽은 `@Bean`으로 같은 이름을 등록하는 경우

```java
@Component
public class MemoryMemberRepository implements MemberRepository {}
```

```java
@Configuration
@ComponentScan(
  excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {
  @Bean(name = "memoryMemberRepository")
  public MemberRepository memberRepository() {
    return new MemoryMemberRepository();
  }
}
```

- 과거에는 수동 빈 등록이 우선권이 있어서 자동 빈을 덮어씀
- 해결하기 힘든 버그를 만들 수 있음 → ∴ 현재는 오류 발생

📌 스프링 부트의 기본 정책 변화

- 스프링 부트는 의도하지 않은 오버라이딩을 막기 위해 정책을 강화함
- 충돌 발생 시, 기본적으로 오류를 발생시킴
- 에러 메시지

```
Consider renaming one of the beans or enabling overriding by setting
spring.main.allow-bean-definition-overriding=true
```

📌 실무 권장 사항

- 자동 등록을 기본으로 사용 + 빈 이름 충돌이 일어나지 않도록 설계할 것
- 수동 등록은 필요한 경우에만 명시적으로 작성
- 설정 클래스는 `@Configuration` + 명확한 빈 이름 사용
- 컴포넌트 스캔 시에는 빈 이름 중복 가능성 줄이기 위해 네이밍 규칙을 활용