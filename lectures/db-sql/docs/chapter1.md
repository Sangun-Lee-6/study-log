# Ch 1. 데이터베이스 개론

# 1. DB & DBMS & DB System

✅ Database(DB)

- 전자적으로(electronically) 저장되고 사용되는 관련있는(related) 데이터들의 조직화된 집합(organized collection)
- `관련있는 데이터` : 같은 서비스 안에서 사용되는 데이터들, ex. SNS 회원 정보, 게시글 정보
- `조직화` : 데이터를 잘 관리하기 위해 조직화되어 있어야함
- `전자적으로` : 데이터가 컴퓨터에 저장되고 사용될 때

✅ DBMS(Database Management System)

- 사용자에게 DB를 관리하는 기능을 제공하는 SW
- ex. MySQL, PostgreSQL 등
- DB를 관리하다 보면 부가적인 데이터 발생함

👉 부가적인 데이터 = Metadata

- DB를 정의하거나 기술하는 데이터
- catlog 라고도 부름
- ex. 데이터 타입, 구조, 제약 조건, 인덱스, 사용자 그룹 등
- 메타데이터도 DBMS를 통해 저장/관리됨

✅ DB System

- DB + DBMS + 연관된 Application

# 2. Data model

✅ Data models

- DB 구조(Structure)를 기술하는데 사용될 수 있는 개념들이 모인 집합
- DB 구조를 추상화해서 표현할 수 있는 수단
- Data model은 여러 종류가 있으며 추상화 수준과 DB 구조화 방식이 조금씩 다름
- DB에서 읽고 쓰기 위한 기본적인 동작들(operations)도 포함

👉 DB 구조

- ex. 데이터 유형, 관계(relationship), 제약 사항(constraints) 등

✅ 데이터 모델의 분류

1. Conceptual(or high-level) data models

2. Logical(or representational) data models

3. Physical(or low-level) data models

1️⃣ Conceptual(or high-level) data models

- 일반 사용자들이 쉽게 이해할 수 있는 개념들로 이루어진 모델
- 추상화 수준이 가장 높음
- 비즈니스 요구 사항을 추상화해서 기술할 때 사용
- ex. ER diagram : Entity, Relation으로 표현

2️⃣ Logical data models

- 이해하기 어렵지 않으면서 디테일하게 DB를 구조화 할 수 있는 개념을 제공
- 데이터가 컴퓨터에 저장될 때와 비슷하게 DB 구조화 가능
- 특정 DBMS나 스토리지에 종속되지 않는 수준에서 DB 구조화 가능
- ex. relational data model : 테이블 형태로 데이터를 저장

3️⃣ Physical data models

- 컴퓨터에 데이터가 어떻게 파일 형태로 저장되는지를 기술할 수 있는 수단을 제공
- ex. data format, data orderings, access path 등

👉 access path

- 데이터 검색을 빠르게 하기 위한 구조체
- ex. index

# 3. Schema & State

✅ DB Schema

- Data model을 바탕으로 DB의 구조를 기술(description)한 것
- 스키마는 DB를 설계할 때 정해짐
- 한 번 정해진 후에는 자주 바뀌지 않음

👉 RDB에서 DB Schema

- 테이블의 컬럼

✅ DB State

- 특정 시점에 DB에 있는 데이터
- snapshot 이라고도 함

✅ Three-schema architecture

- DB System을 구축하는 아키텍처
- User application으로부터 물리적인(physical) DB를 분리시키기 위함
- 안정적인 운영을 위해 각 레벨을 독립시켜 서로 영향을 주지 않기 위함
- 3가지 레벨이 존재하며, 각 레벨마다 스키마가 정의되어 있음
- External schema, Conceptual Schema, Internal Schema
- 데이터가 존재하는 곳은 Internal Schema

1️⃣ Internal Schema

- 물리적으로 데이터가 어떻게 저장되는지 Physical data model을 통해 표현
- ex. data storage, data structure, access path 등

2️⃣ External Schema

- 특정 유저들이 필요로 하는 데이터만 표현
- 그 외의 데이터는 숨김
- External views, user views 라고도 불림
- Logical data model을 통해 표현됨

3️⃣ Conceptual Schema

- 전체 DB에 대한 구조를 기술
- 물리적인 저장 구조에 관한 내용은 숨김
- entities, data types, relationships, constraints에 집중
- Logical data model을 통해 기술

# 4. DB language

✅ DDL(Data definition language)

- 대부분 Conceptual Schema를 정의하기 위해 사용되는 언어

✅ SDL(Storage definition language)

- Internal Schema를 정의하기 위한 언어
- 요즘 RDBMS에서는 SDL이 거의 없고 파라미터 등의 설정으로 대체됨

✅ VDL(Veiw definition language)

- External Scehma를 정의하기 위해 사용되는 언어
- 대부분 DBMS에서는 DDL이 VDL 역할까지 수행

✅ DML(Data manipulation language)

- DB에 있는 데이터를 활용하기 위한 언어
- 데이터 추가, 삭제, 수정, 검색 등 기능을 제공

✅ 오늘날 DBMS는 DML, DDL 등이 통합된 언어로 존재

- ex. SQL