# 섹션 7. 스프링 DB 접근 기술

# 1. H2 데이터베이스 설치

✅ H2 데이터베이스

- 가벼운 개발용 RDBMS
- 브라우저에서 DB 상태 확인 가능
- JDBC 드라이버로 Java 앱과 연결 가능

✅ H2의 데이터 저장 방식

| 모드 | 설명 | JDBC URL 예시 |
| --- | --- | --- |
| 메모리 모드 | 애플리케이션 종료 시 데이터 사라짐 (휘발성) | `jdbc:h2:mem:testdb` |
| 파일 모드 | 디스크에 `.mv.db` 파일로 저장됨 | `jdbc:h2:~/test` |
| 서버 모드 | TCP 서버로 외부 접근 가능 (파일 기반) | `jdbc:h2:tcp://localhost/~/test` |

# 2. 순수 JDBC

✅ JDBC란

- 자바에서 DB에 접근할 수 있게 해주는 표준 API
- SQL을 직접 작성하고, 커넥션도 직접 열고 닫아야함

✅ JDBC 흐름 정리

```
1. DB 연결(Connection)
2. SQL 작성 & 실행(PreparedStatement)
3. 결과 처리(ResultSet)
4. 연결 종료(Close)
```

👉 예시 : `findById`

```
1. getConnection() 으로 DB 연결
2. "select * from member where id = ?" SQL 작성
3. pstmt.setLong(1, id) 로 값 세팅
4. executeQuery() 실행
5. ResultSet으로 결과 받아 Member 객체 생성
6. Optional로 감싸서 반환
7. finally 블록에서 연결 close()
```

✅ DI (의존성 주입)

- 객체 생성 및 연결을 개발자가 직접 하지 않고, 스프링이 대신 만들어서 주입하는 방식
- DI를 사용하면 기존 코드를 수정하지 않고 설정 파일만 바꿔서 구현체를 바꿀 수 있음(OCP 원칙)

🤖 `MemberService`, `MemberRepository`

- `MemberService`에서는 `MemberRepository`라는 인터페이스에 의존
- 이러한 구조에서 `MemberRepository`의 구현체를 교체해서 DB를 바꿀 수 있음

```java
@Configuration
public class SpringConfig {
		...

    @Bean
    public MemberRepository memberRepository() {
        // return new MemoryMemberRepository();  // 👉 메모리 버전 사용
        return new JdbcMemberRepository(dataSource); // 👉 JDBC 버전으로 변경
    }
}
```

💡 정리

1. 다형성으로 코드가 유연해짐

→ MemberService는 MemberRepository라는 인터페이스에만 의존

→ 덕분에 MemberRepository의 구현체를 바꿔끼울 수 있는 구조가 됨

2. DI로 `바꿔끼우는 작업`을 수행

→ DI가 없다면 직접 new로 객체를 생성해야함

→ DI 덕분에 런타임에 객체를 연결해주는 역할

# 3. 스프링 통합 테스트

✅ 통합 테스트 (Integration Test)

- 여러 컴포넌트(예: 서비스 + 리포지토리 + DB)를 실제 연결된 상태로 하는 테스트
- 스프링 앱이 전체적으로 잘 작동하는지 확인하기 위함
- 예시: `회원가입` 요청 시 실제로 DB에 잘 저장되고, 서비스 로직이 잘 작동하는지 확인

✅ `@SpringBootTest`

- 테스트 실행 시, 실제 스프링 부트 앱 전체를 실행시킴
- 이 애노테이션 덕분에 진짜 스프링 환경에서 테스트 가능

✅ `@Transactional`

- 테스트가 끝나면 자동으로 DB를 롤백
- 테스트 중 INSERT, UPDATE, DELETE해도 실제 DB에 남지 않음
- 테스트가 서로 영향을 주지 않도록 함

✅ `@Autowired`

- 스프링 컨테이너가 의존 객체를 자동으로 주입해줌(DI)
- 테스트에서도 서비스, 레포지토리를 자동 주입 받을 수 있음

```java
@Autowired MemberService memberService;
@Autowired MemberRepository memberRepository;
```

✅ `assertThrows` / `assertEquals` / `assertThat`

- JUnit과 AssertJ에서 제공하는 테스트 검증 도구

```java
assertEquals(기대값, 실제값);
assertThrows(예외클래스, 예외발생코드);
assertThat(값).isEqualTo(기대값);
```

✅ 테스트는 given-when-then 구조로 작성하는 것이 깔끔함

🤖 `MemberServiceIntegrationTest`

# 4. 스프링 JdbcTemplate

✅ JdbcTemplate이란?

- 스프링이 제공하는 JDBC 헬퍼 클래스
- 순수 JDBC의 작업 과정의 반복적인 코드를 줄여주고, SQL만 작성하도록 도와줌
- SQL은 여전히 개발자가 직접 작성해야함

`🤖` `JdbcTemplateMemberRepository`

- JdbcTemplate을 사용하는 회원 리포지토리 구현

✅ SimpleJdbcInsert

- INSERT문을 쉽게 처리할 수 있게 도와주는 클래스

✅ JdbcTemplate 생성

```java
private final JdbcTemplate jdbcTemplate;

public JdbcTemplateMemberRepository(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
}
```

- 스프링이 관리하는 `DataSource`를 받아 `JdbcTemplate`을 생성
- 이 `JdbcTemplate`을 사용해 DB 작업을 진행

🤖 회원 저장 (`save()`) 기능

```java
@Override
public Member save(Member member) {
    SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
    jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id");

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("name", member.getName());

    Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
    member.setId(key.longValue());
    return member;
}
```

- `SimpleJdbcInsert`: INSERT SQL을 자동으로 생성해주는 도구
- `withTableName("member")`: 테이블 이름 지정
- `usingGeneratedKeyColumns("id")`: `id` 자동 생성
- `Map<String, Object>`: insert 시 사용할 컬럼값(name) 지정
- `executeAndReturnKey(...)`: insert 실행 후 생성된 키(id)를 반환받아 member에 설정

🤖 ID로 회원 찾기 (`findById()`)

```java
@Override
public Optional<Member> findById(Long id) {
    List<Member> result = jdbcTemplate.query(
        "select * from member where id = ?",
        memberRowMapper(),
        id
    );
    return result.stream().findAny();
}
```

- SQL을 직접 작성: `"select * from member where id = ?"`
- `?`에는 `id` 값이 바인딩됨
- 결과는 `memberRowMapper()`를 통해 `Member` 객체로 변환됨
- 조회된 결과 중 첫 번째 요소만 가져와 `Optional`로 감싸서 반환

🤖 전체 회원 조회 (`findAll()`)

```java
@Override
public List<Member> findAll() {
    return jdbcTemplate.query("select * from member", memberRowMapper());
}
```

- 모든 회원을 조회하고, `RowMapper`로 `Member` 객체로 바꿔 리스트로 반환

🤖 이름으로 회원 찾기 (`findByName()`)

```java
@Override
public Optional<Member> findByName(String name) {
    List<Member> result = jdbcTemplate.query(
        "select * from member where name = ?",
        memberRowMapper(),
        name
    );
    return result.stream().findAny();
}
```

- 이름으로 회원을 조회. `?`는 `name`으로 바인딩됨
- 결과가 하나일 수도 없을 수도 있으므로 `Optional`로 반환

🤖 RowMapper 정의

```java
private RowMapper<Member> memberRowMapper() {
    return (rs, rowNum) -> {
        Member member = new Member();
        member.setId(rs.getLong("id"));
        member.setName(rs.getString("name"));
        return member;
    };
}
```

- DB의 `ResultSet`을 `Member` 객체로 변환하는 로직
- `rs.getLong("id")` → DB 컬럼 값을 자바 필드에 넣음

✅ JdbcTemplate을 사용하도록 설정 클래스 수정

- Bean 등록

```java
@Bean
public MemberService memberService() {
    return new MemberService(memberRepository());
}

@Bean
public MemberRepository memberRepository() {
    // return new MemoryMemberRepository();
    // return new JdbcMemberRepository(dataSource);
    return new JdbcTemplateMemberRepository(dataSource);
}
```

- `MemberService`는 `MemberRepository`를 필요로 함 → 의존성 주입(DI)
- 현재 사용하는 저장소는 `JdbcTemplateMemberRepository`로 설정됨

✅ JdbcTemplate은 왜 "템플릿 메서드 패턴"을 사용하는 이유

- JDBC 코드는 비슷한 흐름이 반복됨
- JdbcTemplate은 위의 공통된 흐름을 템플릿 메서드 패턴으로 감싸고, 개발자는 SQL과 결과 처리(RowMapper)만 작성하면 되게 만들어줌

👉 템플릿 메서드 패턴이란?

- 일정한 흐름(알고리즘)을 틀로 만들고, 그 중 일부만 개발자가 직접 구현하게 하는 디자인 패턴
- 공통 로직은 상위 클래스(템플릿)가 제공하고, 특정한 부분만 하위 클래스(혹은 람다)가 정의

# 5. JPA (Java Persistence API)

✅ JPA

- 자바 객체와 DB를 자동으로 매핑해주는 ORM 기술
- SQL을 작성하지 않고도 DB 작업을 객체 기반으로 처리 가능
- 장점 : SQL 작성 감소, 객체 중심 설계, 트랜잭션 자동 처리
- 반복되는 JDBC 코드를 제거하고 SQL 작성이 줄어들어 생산성 ↑, 유지보수성 ↑

🤖 JPA Entity 클래스

```java
@Entity
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    // getter/setter 생략
}
```

- `@Entity` : 해당 클래스가 DB 테이블과 매핑됨
- `@Id` : PK 설정
- `@GeneratedValue` : PK 자동 생성 전약 지정

🤖 JPA 레포지토리 : `JpaMemberRepository`

✅ 트랜잭션 설정

```java
@Transactional
public class MemberService { ... }
```

- `@Transactional`을 붙이면 해당 클래스 또는 메서드는 트랜잭션 안에서 실행됨
- 예외 발생 시 자동으로 롤백
- JPA는 반드시 트랜잭션 내에서 동작해야 함

# 6. 스프링 데이터 JPA (Spring Data JPA)

✅ 스프링 데이터 JPA

- JPA 위에서 동작하는 레포지토리 자동 구현 프레임워크
- CRUD 자동 구현, 쿼리 메서드 제공, 페이징 지원
- 인터페이스만 작성하면 구현체 없이 동작
- 이를 통해 개발자는 핵심 비즈니스에만 집중 가능

🤖 `SpringDataJpaMemberRepository`

- JPARepository : 기본 CRUD, 페이징 등 제공
- findByName() : 메서드 이름만으로 쿼리 자동 생성
- 구현 클래스 필요 없음, 스프링이 자동으로 프록시 객체를 만들어줌

✅ 복잡한 쿼리는 QueryDSL을 활용해서 자바 코드 기반의 안전한 쿼리 작성