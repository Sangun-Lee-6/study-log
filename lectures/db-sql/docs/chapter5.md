# Ch 5. 데이터 조회하기 (select)

# 1. SELECT로 데이터 조회하기

🤖 id가 9인 임직원 조회하기

```sql
select name, position 
from EMPLOYEE 
where id = 9;
```

- selection condition : where 절에 들어가는 조건
- ex. { `id = 9` }
- projection attributes : 관심 있는 속성
- ex. `name, position`

✅ select 문 정리

```sql
select { 컬럼명 }
from { 테이블명 }
where { 조건절 }
```

🤖 project 2002의 리더인 직원 정보 조회

- PROJECT 테이블과 EMPLOYEE 테이블 필요

```sql
select E.id, E.name, E.position
from PROJECT P, EMPLOYEE E
where P.id = 2002
and P.leader_id = E.id;
```

# 2. AS 키워드

✅ AS 키워드

- 테이블이나 attribute에 별칭을 붙일 때 사용
- AS는 생략 가능

```sql
select E.id leaderId, E.name leaderName, E.position leaderPosition
from PROJECT P, EMPLOYEE E
where P.id = 2002
and P.leader_id = E.id;
```

# 3. DISTINCT 키워드

🤖 디자이너들이 참여하고 있는 프로젝트들의 ID와 이름 조회

```sql
select distinct P.id projectId, P.name projectName
from EMPLOYEE E, WORKS_ON W, PROJECT P
where E.position = 'DSGN'
and E.id = W.empl_id
and W.empl_id = P.id;
```

✅ distinct 키워드

- select 결과 중 중복되는 레코드는 제외하고 싶을 때

# 4. LIKE 키워드

🤖 이름이 N으로 시작하거나 N으로 끝나는 임직원 조회

```sql
select * 
from EMPLOYEE E
where E.name like 'N%'
or E.name like '%N'; 
```

🤖 이름에 NG가 들어가는 임직원 조회

```sql
select *
from EMPLOYEE E
where E.name like '%NG%';
```

🤖 이름이 J로 시작하고, 총 4글자의 이름을 갖는 임직원 조회

```sql
select *
from EMPLOYEE E
where E.name like 'J___'
```

✅ `%`, `_`

- `%` : 0개 이상의 임의의 개수를 갖는 문자열
- `_` : 1개의 문자

✅ `%`나 `_`를 문자열로 조회하고 싶다면 `\`를 앞에 붙여주면 됨

- `\` : escape 문자

# 5. *(asterisk) 키워드

✅ * 키워드

- 모든 attribute를 조회하고 싶을 때

# 6. 주의사항

✅ select 조회 시, 조건절이 있다면 → 해당 attribute에 인덱스가 있어야함

- 그렇지 않으면 조회 속도가 느려짐