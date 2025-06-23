# Ch 3. SQL 기본 & DDL

# 1. SQL 기본 개념

✅ SQL 뜻

- Structured Query Language
- RDBMS의 표준 언어
- 종합적인 DB 언어임(DDL + DML + VDL)

✅ SQL 주요 용어

| relational data model | SQL |
| --- | --- |
| relation | table |
| attribute | column |
| tuple | row |
| domain | domain |

✅ SQL에서 relation

- multiset of tuples
- 즉, 중복된 tuple을 허용함

✅ SQL & RDBMS

- SQL이 표준 언어지만 구현에 강제가 없음
- 따라서 RDBMS 마다 제공하는 SQL 언어가 조금씩 다름

✅ 예제 : IT 회사 관련 RDB 만들기

- 부서, 사원, 프로젝트 정보들을 저장하는 RDB
- RDBMS는 MySQL(InnoDB 엔진) 사용

🤖 docker로 mysql 실행하기

- mysql 컨테이너 실행

```bash
docker run -d \
  --name mysql-local \
  -e MYSQL_ROOT_PASSWORD=my-secret-pw \
  -e MYSQL_DATABASE=testdb \
  -p 3306:3306 \
  mysql:8.0
```

- mysql 내부 접속

```bash
docker exec -it mysql-local mysql -uroot -p
# my-secret-pw 입력
```

# 2. 데이터베이스 생성하기

🤖 mysql의 DB 목록 조회

```sql
SHOW DATABASES;
```

```bash
mysql> SHOW DATABASES;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| mysql              |
| performance_schema |
| sys                |
| testdb             |
+--------------------+
5 rows in set (0.01 sec)
```

🤖 DB 생성하기

```sql
CREATE DATABASE company;
```

🤖 현재 선택된 DB 조회하기

```sql
SELECT database();
```

- NULL이 나온다면 현재 지정된 DB가 없는 것

🤖 사용할 DB 선택하기

```sql
USE company;
```

🤖 DB 삭제하기

```sql
DROP DATABASE company;
```

✅ DB vs Schema

- MySQL에서 DB와 Schema는 같은 의미
- Oracle, PostgreSQL에서는 다음과 같이 구분됨

→ DB : 데이터의 물리적 집합, 여러 schema 포함 가능

→ Schema : 테이블, 뷰, 인덱스, 트리거 등 논리적 구조 집합

# 3. 테이블 생성하기

🤖 예제 : IT 회사 정보를 저장하는 DB의 스키마

- DEPARTMENT : id, name, leader_id
- EMPLOYEE : id, name, birth_date, sex, position, salary, dept_id
- PROJECT : id, name, leader_id, start_date, end_date
- WORKS_ON : empl_id, proj_id

🤖 DEPARTMENT 테이블 생성하기

```sql
create table DEPARTMENT(
id INT PRIMARY KEY, 
name VARCHAR(20) NOT NULL UNIQUE, 
leader_id INT);
```

# 4. attribute data type

✅ attribute data type : 숫자

| 종류 | 설명 | 사이즈 | MySQL |
| --- | --- | --- | --- |
| 정수 | 정수를 저장할 때 사용 | 1 byte | TINYINT |
|  |  | 2 byte | SMALLINT |
|  |  | 3 byte | MEDIUMINT |
|  |  | 4 byte | INT |
|  |  | 8 byte | BIGINT |
| 부동 소수점 방식 | - 실수를 저장할 때 사용
- 고정 소수점 방식에 비해 정확하지 않음 | 4 byte | FLOAT |
|  |  | 8 byte | DOUBLE |
| 고정 소수점 방식 | - 실수를 정확하게 저장할 때 사용 | variable | DECIMAL, NUMERIC |

✅ attribute data type : 문자열

| 종류 | 설명 | MySQL |
| --- | --- | --- |
| 고정 크기 문자열 | - 최대 몇 개의 문자를 가질지 결정
- 나머지 공간은 space로 채워서 저장 | CHAR(n)
(0≤ n ≤ 255) |
| 가변 크기 문자열 | - 최대 몇 개의 문자를 가질지 결정
- 저장될 문자열 길이 만큼만 저장 | VARCHAR(n)
(0≤ n ≤ 65,535) |
| 사이즈가 큰 문자열 | - 사이즈가 큰 문자열을 저장할 때 | TINYTEXT
TEXT
MEDIUMTEXT
LONGTEXT |

✅ attribute data type : 날짜와 시간

| 종류 | 설명  | MySQL |
| --- | --- | --- |
| 날짜 | - 년,월,일 저장
- `YYYY-MM-DD` | DATE |
| 시간 | - 시, 분, 초를 저장
- `hh:mm:ss` or `hhh:mm:ss` | TIME |
| 날짜와 시간 | - 날짜와 시간을 같이 표현
- `YYYY-MM-DD hh:mm:ss`
- TIMESTAMP는 time-zone이 반영됨 | DATETIME, TIMESTAMP |

✅ attribute data type : 그 외

| byte-string | - byte string을 저장
- 주로 암호화에서 사용됨 | BINARY
  VARBINARY
  BLOB type |
  | --- | --- | --- |
  | boolean | - true, false 저장
- MySQL에는 없음 | TINYINT로 대체 사용 |
  | 위치 관련 |  | GETMETRY |
  | JSON | - json 형태 데이터 저장 | JSON |

# 5. primary key 설정하기

✅ primary key

- UNIQUE & NOT NULL
- 테이블의 튜플을 식별하기 위해 사용
- 하나 이상의 attribute로 구성

✅ PK 선언하는 방법

1. attribute 하나로 구성될 때

```sql
create table PLAYER(
id INT PRIMARY KEY,
...
);
```

2. attribute 하나 이상으로 구성될 때

```sql
create table PLAYER(
team_id VARCHAR(12),
back_number INT,
...
PRIMARY KEY(team_id, back_number)
);
```

# 6. unique constraint 설정하기

✅ UNIQUE

- 해당 attribute는 중복된 값을 가질 수 없음
- NULL은 중복 허용할 수도 있음(RDBMS 마다 다름, MySQL은 중복 허용)

✅ UNIQUE 선언하는 방법

1. attribute 하나로 구성될 때

```sql
create table PLAYER(
id INT UNIQUE,
...
);
```

2. attribute 하나 이상으로 구성될 때

```sql
create table PLAYER(
team_id VARCHAR(12),
back_number INT,
...
UNIQUE(team_id, back_number)
);
```

# 7. not null constraint 설정하기

✅ NOT NULL

- 해당 attribute는 NULL을 가질 수 없음

✅ NOT NULL 선언하는 방법

```sql
create table Student(
...,
phone_number INT NOT NULL UNIQUE,
...
);
```

🤖 EMPLOYEE 테이블 생성

```sql
create table EMPLOYEE(
id INT PRIMARY KEY,
name varchar(30) not null,
birth_date date,
sex char(1) check(sex in ('M', 'F')),
position varchar(10),
salary int default 50000000,
dept_id int,
foreign key(dept_id) references DEPARTMENT(id) on delete set null on update cascade,
check(salary >=50000000)
);
```

# 8. attribute의 default 값 설정하기

✅ defult 속성

- attribute의 default 값을 정의할 때 사용
- 새로운 튜플을 저장할 때 해당 attribute에 대한 값이 없다면 default 값으로 저장됨

✅ default 선언 방법

```sql
create table Orders(
...,
menu varchar(15) DEFAULT 'chicken',
...
);
```

# 9. check constraint 설정하기

✅ check

- attribute의 값을 제한하고 싶을 때 사용

✅ check 선언 방법

1. attribute 하나로 구성될 때

```sql
create table EMPLOYEE(
...,
age int check(age>-20)
);
```

2. attribute 하나 이상으로 구성될 때

```sql
create table PROJECT(
...,
start_date date,
end_date date,
...
check(start_date < end_date)
);
```

# 10. foreign key 설정하기

✅ foreign key

- attribute가 다른 테이블의 PK나 UNIQUE KEY를 참조할 때 사용
- 참조 대상은 반드시 존재해야함

✅ foreign key 선언 방법

```sql
create table Employee(
...,
dept_id INT,
foreign key (dept_id)
	references DEPARTMENT(id)
	on delete reference_option
	on update reference_option
);
```

- dept_id 컬럼은 DEPARTMENT 테이블의 id 컬럼을 참조
- 참조하고 있는 키가 삭제되거나 업데이트될 때, dept_id는 어떻게 해줄 것인가 정해줘야함

👉 reference_option 5가지

| reference_option | 참조값이 삭제/변경 시 action | MySQL 지원 |
| --- | --- | --- |
| CASCADE | 변경값을 그대로 반영 | ⭕️ |
| SET NULL | NULL로 변경 | ⭕️ |
| RESTRICT | 참조값 삭제/변경 금지 | ⭕️ |
| NO ACTION | RESTRICT와 유사 | ❌ |
| SET DEFAULT | default 값으로 변경 | ❌ |

# 11. constraint 이름 명시하기

✅ constraint 이름 붙이기

- 이름을 붙이면 어떤 constraint를 위반했는지 쉽게 파악 가능
- constraint 삭제 시, 이름으로 삭제 가능

🤖 예시

```sql
create table TEST(
	age INT CONSTRAINT age_over_20 CHECK(age>20)
);

# 위반 시 : Check constraint 'age_over_20' is violated.
```

- 만약 저렇게 이름을 정해주지 않는다면, 아래와 같이 메세지가 나옴 → 가독성 ↓

```sql
# Check constraint 'TEST_chk_1' is violated.
```

- 만약, TEST_chk_1의 의미를 알고 싶다면 아래 명령어로 조회

```sql
show create table TEST;
```

🤖 나머지 PROJECT, WORKS_ON 테이블 생성

```sql
create table PROJECT(
id int primary key,
name varchar(20) not null unique,
leader_id int,
start_date date,
end_date date,
foreign key(leader_id) 
	references EMPLOYEE(id)
	on delete SET NULL
	on update CASCADE,
check(start_date < end_date)
);
```

```sql
create table WORKS_ON(
empl_id int,
proj_id int,
primary key(empl_id, proj_id),
foreign key(empl_id)
	references EMPLOYEE(id)
	on delete cascade
	on update cascade,
foreign key(proj_id)
	references PROJECT(id)
	on delete cascade
	on update cascade
);
```

# 12. 테이블 변경하기 (alter table)

🤖 DEPARTMENT 테이블의 leader_id에 FK 생성하기

- DEPARTMENT 테이블을 생성할 때, EMPLOYEE 테이블이 없었으므로 FK 설정 불가
- 따라서 FK 없이 생성 후, FK를 추가
- 이때 사용하는 명령어가 `alter table`

```sql
alter table DEPARTMENT add foreign key(leader_id)
	references EMPLOYEE(id)
	on update cascade
	on delete set null;
```

✅ alter table

- 스키마 생성 후, 스키마 변경이 필요할 때 사용하는 SQL
- attribute 추가, 이름 변경, 타입 변경, 삭제 등
- table 이름 변경 등
- PK, FK 추가 등

⚠️ 테이블의 스키마를 변경하는 것

- 서비스 중인 테이블의 스키마를 변경할 때
- 서비스의 영향이 얼마나 미칠지 검토한 후 변경하는 것이 중요

# 13. 테이블 삭제하기 (drop table)

✅ drop table

- 테이블을 삭제할 때 사용

```sql
drop table table_name;
```