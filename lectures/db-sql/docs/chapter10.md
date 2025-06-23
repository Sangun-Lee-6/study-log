# Ch 10. stored function

# 1. stored function

✅ stored function

- 사용자가 정의한 함수
- DBMS에 저장하고 사용 가능
- SQL의 select, insert, update, delete에서 사용 가능

✅ MySQL Stored Function 기본 구조

```sql
-- 1. 구문 종료 기호를 기본 세미콜론(;)에서 다른 기호로 임시 변경
delimiter $$

-- 2. 사용자 정의 함수 생성
create function function_name(parameter_list)
returns return_type
[deterministic | not deterministic]
[no sql | reads sql data | modifies sql data | contains sql]
begin
    -- 함수 본문: 로직 구현, return 사용
    return some_value;
end
$$

-- 3. 다시 기본 구문 구분자(;)로 복원
delimiter ;
```

👉 `delimiter $$`

- 함수 내용 안에 `;`가 포함되므로, MySQL이 현재 함수 정의 쿼리를 구분할 수 있도록 다른 구분자로 임시 변경

👉 `create function function_name(parameter_name data_type, ...)`

- 사용자 정의 함수 생성 구문
- 함수 이름과 파라미터 정의

👉 `returns return_type`

- 반환 타입 지정

👉 `no sql`

- 이 함수가 DB에 어떤 영향을 주는지 명시

👉 `begin … end`

- 함수 본문
- 내부에서 로직 처리
- return 필수

👉 `delimiter ;`

- 구분자를 다시 `;`로 복구

✅ deterministic property

| 키워드 | 의미 | 예시 | 결과 예측 가능성 |
| --- | --- | --- | --- |
| **`DETERMINISTIC`** | 같은 입력값이면 항상 같은 결과 반환 | `x + y`, `x * 2` | ✅ 항상 같음 |
| **`NOT DETERMINISTIC`** | 같은 입력값이라도 결과가 바뀔 수 있음 | `RAND()`, `NOW()`, `UUID()` | ❌ 결과가 매번 다름 |

👉 deterministic을 명시하는 이유

- MySQL에서는 다음과 같은 최적화를 할 때 함수의 결정성 여부를 기준으로 판단하므로

| 목적 | 영향 |
| --- | --- |
| 뷰, 생성 칼럼, 인덱스에서 함수 사용 가능 여부 | 대부분 `DETERMINISTIC`만 허용됨 |
| 결과 캐싱 가능 여부 | `DETERMINISTIC`이면 결과 캐싱 가능 |
| 반복 호출 최적화 | 동일한 값이면 재계산 생략 가능 |

✅ SQL 성격 제어 옵션(MySQL)

| 옵션 | 설명 |
| --- | --- |
| `NO SQL` | 내부에서 어떤 SQL도 실행하지 않음 (`SELECT`, `INSERT` 등 전부 금지) |
| `READS SQL DATA` | 읽기 전용 SQL 가능 (`SELECT`는 허용되나 `INSERT/UPDATE`는 안 됨) |
| `MODIFIES SQL DATA` | 데이터 변경 SQL 포함 가능 (예: `INSERT`, `UPDATE`, `DELETE`) |
| `CONTAINS SQL` | SQL이 있을 수 있으나 읽기/쓰기 여부는 불명확 (가장 포괄적) |

# 2. stored function 예제

### 🤖 임직원의 id를 5자리 정수로 랜덤하게 발급, id 맨 앞자리는 1로 고정

```sql
delimiter $$

create function id_generator()
returns int
no sql
begin 
	return (10000 + floor(rand() * 10000));
end
$$

delimiter ;
```

### 🤖 부서의 id를 파라미터로 받으면 해당 부서의 평균 연봉을 알려주는 함수

```sql
delimiter $$

create function get_dept_avg_salary(dept_id int)
returns int
reads sql data
begin
	declare avg_salary int;
	
	select avg(salary) into avg_salary
	from EMPLOYEE E
	where E.dept_id = dept_id;
	
	return avg_salary;
end
$$

delimiter ;
```

👉 함수 본문에서 변수 선언

```sql
declare var_name var_type;
```

👉 변수를 따로 선언하지 않고, `@avg_sal` 처럼 사용할 수도 있음

👉 변수에 값 저장

```sql
select avg(salary) into avg_salary
```

🤖 부서 정보와 부서 평균 연봉 조회

- stored function 사용

```sql
select *, get_dept_avg_salary(dept_id)
from DEPARTMENT
```

```
+---------+-------------+-----------+------------------------------+
| dept_id | dept_name   | leader_id | get_dept_avg_salary(dept_id) |
+---------+-------------+-----------+------------------------------+
|    1001 | headquarter |         4 |                     93000000 |
...
|    1005 | product     |        13 |                    135000000 |
|    1006 | QA          |      NULL |                         NULL |
+---------+-------------+-----------+------------------------------+
```

### 🤖 졸업 요건 중 하나인 토익 800 이상을 충족했는지 알려주는 함수

```sql
delimiter $$

create function toeic_pass_fail(toeic_score int)
returns char(4)
no sql
begin
	declare pass_fail char(4);
	if toeic_score is null   then set pass_fail = 'fail'
	elseif toeic_score < 800 then set pass_fail = 'fail'
	else                     then set pass_fail = 'pass'
	end if;
	return pass_fail;
end
$$

delimiter ;
```

# 3. stored function이 할 수 있는 일

✅ 위 예제에서 다루지 않은 작업도 가능

- loop를 돌면서 반복적인 작업 수행
- case 키워드로 값에 따라 분기 처리
- 에러 핸들링, 에러 발생 등

# 4. stored function 삭제하기

```sql
drop function sotred_function_name;
```

# 5. 등록된 stored function 파악하기

✅ DB에 있는 stored function 조회

```sql
show function status
where db = 'company'
```

✅ DB 목록 조회

```sql
show databases;
```

✅ 함수 코드 확인하기

```sql
show create function function_name;
```

# 6. stored function은 언제 써야할까

💡 기본적으로 회사 정책을 따라가기

✅ Three-tier architecture

1. Presentation tier : 사용자에게 보여지는 부분을 담당 → HTML, JS 등

2. Login tier : 비즈니스 로직 담당 → Java/Spring 등

3. Data tier : 데이터 저장, 관리 담당 → MySQL 등

✅ stored function 사용

- DB util 함수로 사용
- 비즈니스 로직을 stored function에 두는 것은 좋지 않음
- Logic tier에 비즈니스 로직이 있으므로