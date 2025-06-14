# 섹션 5. 스프링 빈과 의존관계

📌 스프링 빈을 등록하는 2가지 방법

1. 컴포넌트 스캔과 자동 의존관계 설정

2. 자바 코드로 직접 스프링 빈 등록하기

# 1. 컴포넌트 스캔과 자동 의존관계 설정

✅ 컴포넌트 스캔이란?

- 스프링이 `@Component` 애노테이션이 붙은 클래스를 자동으로 탐색해서 스프링 빈으로 등록하는 기능
- 이 기능 덕분에 해당 클래스가 자동으로 스프링에 의해 관리됨

✅ `@Component`를 포함하는 주요 애노테이션

- 아래 애노테이션도 @Component 기반이라서 컴포넌트 스캔 대상임

| 애노테이션 | 역할 | 설명 |
| --- | --- | --- |
| `@Component` | 기본 컴포넌트 | 직접 사용 가능 |
| `@Controller` | 웹 컨트롤러 | 웹 요청 처리용 클래스 |
| `@Service` | 서비스 계층 | 핵심 비즈니스 로직 구현 클래스 |
| `@Repository` | 데이터 접근 계층 | DB와 관련된 클래스, 예외 변환 기능 포함 |

✅ 예제: 회원 서비스, 회원 레포지토리 빈 등록

```java
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired // 스프링이 자동으로 의존 객체를 주입해줌
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}
```

```java
@Repository
public class MemoryMemberRepository implements MemberRepository {
    // 구현 내용 생략
}
```

✅ `@Autowired`

- 스프링이 의존성(빈)을 자동으로 주입해주는 애노테이션
- 스프링 컨테이너에 등록된 다른 스프링 빈을 주입받고 싶을 때 사용
- `@Autowired`가 붙은 생성자, 필드, 메서드에 대해 스프링은 해당 타입의 빈을 찾아 자동으로 주입

✅ 스프링 빈 등록 구조 (개념 이미지)

- memberService, memberRepository 객체는 스프링 컨테이너에 자동으로 등록된 스프링 빈

```
스프링 컨테이너
 ├── memberService (MemberService)
 └── memberRepository (MemoryMemberRepository)
```

✅ 스프링 빈은 기본적으로 싱글톤

- 즉, 동일한 빈은 하나의 인스턴스만 생성돼서 공유됨
- 특별한 설정이 없다면, 대부분 싱글톤을 유지하는 것이 일반적

# 2. 자바 코드로 직접 스프링 빈 등록하기

🤖 `SpringConfig`

- 이렇게 Java 코드로 스프링 빈을 직접 등록 가능
- `@Configuration, @Bean` 사용

✅ 의존성 주입(DI)이란?

- 스프링이 필요한 객체를 대신 만들어서 넣어주는 기능
- ex. 스프링이 MemberService와 MemberRepository를 연결해주는 것

👉 DI를 하는 3가지 방법

| 방법 | 설명 | 장단점 |
| --- | --- | --- |
| **필드 주입** | 클래스 내부에 바로 주입<br/>예: `@Autowired private MemberRepository memberRepository;` | ❌ 테스트/유지보수에 불리해서 **추천되지 않음** |
| **Setter 주입** | setter 메서드로 주입<br/>예: `@Autowired public void setRepo(...)` | 일부 상황엔 유용하지만, 필수가 아닌 의존성에 사용 |
| **생성자 주입** ✅ | 생성자를 통해 주입<br/>예: `@Autowired public MemberService(...)` | ✅ 가장 안전하고 명확해서 실무에서 가장 많이 사용 |

⚠️ 주의할 점: `@Autowired`는 스프링이 만든 객체에서만 동작

- `@Autowired`는 스프링이 관리하는 객체(=스프링 빈)에서만 작동해요.
- 직접 `new`로 만든 객체에서는 스프링이 개입하지 않음

# 3. 보충 내용

✅ 스프링 컨테이너란?

- 스프링이 만든 객체(= 스프링 빈)를 보관하고 관리하는 공간
- 스프링 앱이 실행되면 → 스프링은 필요한 객체를 미리 자동으로 만들고, 이를 보관함
- 이 보관 장소가 스프링 컨테이너
- 스프링 컨테이너는 여러 개의 스프링 빈을 보관하고, 필요할 때 꺼내서 자동으로 주입해줌

✅ 스프링 빈이란?

- 스프링 컨테이너에 등록된 객체

👉 예제 : 스프링 빈

- `@Service` 덕분에 스프링이 `MemberService`의 객체를 만들어서 컨테이너에 등록
- 이렇게 등록된 객체가 스프링 빈

```java
@Service
public class MemberService {
}
```