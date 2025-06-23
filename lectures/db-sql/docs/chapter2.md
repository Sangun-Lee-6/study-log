# Ch 2. 관계형 데이터베이스

# 1. 수학에서 relation

✅ Set

- 서로 다른 elements를 가지는 collection
- 하나의 set에서 elements 순서는 중요 X

✅ Cartesian product

- 표현식 : A x B
- Set A, Set B의 원소로 만들 수 있는 모든 쌍
- 이때 binary relation(두 집합의 원소쌍)은 Cartesian product의 부분 집합

✅ 따라서 수학에서 relation은

- subset of Cartesian product
- 또는 set of tuples

# 2. relational model에서 relation

✅ relational data model에서 Set은 Domain이라고 부름

- Domain이 테이블의 컬럼
- Domain 안에 있는 값들의 조합이 tuple

✅ relational data model에서 하나의 tuple은 레코드를 나타냄

- 이러한 tuple은 테이블로 표현하기 좋음

💡 주요 개념 정리

| 주요 개념 | 설명 |
| --- | --- |
| domain | set of atomic values |
| domain name | domain 이름 |
| attribute | domain이 relation에서 맡은 역할 이름 |
| tuple | 각 attribute의 값으로 이루어진 리스트
(NULL 가능) |
| relation | set of tuples |

# 3. relation schema

✅ relation schema

- relation의 구조를 나타냄
- relation 이름과 attributes 리스트로 표기됨, 관련 constraints도 포함
- ex. `STUDENT(id, name, grade, major, phone_num)`

✅ degree of a relation(관계의 차수)

- relation schema에서 attributes의 수
- ex. `STUDENT(id, name, grade, major, phone_num)` → degree 5

# 4. relational DB

✅ relational DB

- relational data model에 기반하여 구조화된 DB
- relational DB는 여러 개 relation으로 구성

✅ relational DB Schema

- relation schemas set + integrity constraints set

# 5. relation의 특징

✅ relation은 중복된 튜플을 가질 수 없음

- ∵ relation은 set of tuples이므로

✅ relation의 튜플을 식별하기 위해 attribute의 부분 집합을 key로 설정함

- ex. Student 테이블에서 id로 해당 레코드를 특정함

✅ relation에서 tuple의 순서는 중요하지 않음

- ∴ 튜플을 정렬할 수 있는 방법은 여러 개

✅ 하나의 relation에서 attribute의 이름은 중복되면 안됨

✅ 하나의 tuple에서 attribute의 순서는 중요하지 않음

✅ attribute는 atomic 해야함

- 원자적, 즉 더이상 나누어지면 안됨
- 데이터가 여러 개 묶여 있다면 다른 attribute로 나눠서 저장해야함

# 6. NULL

✅ NULL의 의미

- 값이 존재하지 않음 | 존재하지만 아직 알지 못함 | 관련 없음

# 7. Keys

✅ super key

- relation에서 tuples를 unique하게 식별할 수 있는 attributes set
- 전체 attributes 집합도 하나의 tuple을 특정할 수 있으므로 superkey가 될 수 있음

✅ candidate key

- 어느 한 attribute라도 제거하면 unique하게 튜플을 식별할 수 없는 super key

✅ primary key

- relation에서 tuples를 unique하게 식별하기 위해 선택된 candidate key
- ex. { id }, { team_id, back_number }

✅ unique key

- primary key가 아닌 candidate key
- alternate key라고도 부름
- ex. { id }가 pk로 선택되면 { team_id, back_number } 가 unique key

✅ foreign key

- 다른 relation의 pk를 참조하는 attributes set

# 8. Constraints

✅ Constraints

- relational db의 relations들이 언제나 항상 지켜줘야 하는 제약 사항
- 데이터 일관성을 위한 제약 사항

👉 implicit constraints

- relational data model 자체가 갖는 constraints
- relation은 중복되는 tuple을 가질 수 없음
- relation 내에서는 같은 이름의 attribute를 가질 수 없음

👉 schema-based constraints

- 주로 DDL을 통해 schema에 직접 명시할 수 있는 constraints
- explicit constraints라고도 함

👉👉 domain constraints

- attribute의 value는 해당 attribute의 domain에 속한 value
- ex. grade 속성의 도메인이 1,2,3,4 라면, 100이란 값은 들어갈 수 없음

👉👉 key constraints

- 서로 다른 tuples는 같은 value의 key를 가질 수 없음
- ex. 서로 다른 레코드가 같은 id 값(PK)을 가질 수 없음

👉👉 NULL value constraints

- attribute가 NOT NULL로 명시됐다면, NULL을 값으로 가질 수 없음

👉👉 entity integrity constraints

- PK는 value에 NULL을 가질 수 없음

👉👉 referential integrity constraints

- FK와 PK의 도메인이 같아야하고, PK에 없는 values를 FK가 값으로 가질 수 없음