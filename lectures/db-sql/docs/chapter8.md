# Ch 8. 테이블 조인 (join)

# 1. Join

✅ Join이란

- 2개 이상 테이블에 있는 데이터를 한 번에 조회하는 것
- 여러 종류의 join이 존재함

# 2. implicit join vs explicit join

✅ implicit join

- where 절에 join condition이 같이 있는 형태
- from 절에는 테이블만 나열
- 단점 : where 절에 selection condition과 join condition이 같이 있어서 가독성 ↓
- 옛날 스타일의 join

✅ explicit join

- from 절에 join 키워드와 함께 jojned table을 명시하는 방식
- from 절의 on 뒤에 join condition이 있음
- 가독성이 좋음

🤖 id가 1인 임직원이 속한 부서 이름 조회하기

```sql
# implicit join
select D.name3. 
from EMPLOYEE E, DEPARTMENT D
where E.id = 1
and E.dept_id = D.id;
```

```sql
# explicit join
select D.name
from EMPLOYEE E 
join DEPARTMENT D on E.dept_id = D.id
where E.id = 1;
```

# 3. inner join vs outer join

🤖 inner join 예제

- 위에서 본 explicit join 예제에서, join 키워드 앞에 inner가 생략된 것

```sql
# explicit join
select D.name
from EMPLOYEE E 
join DEPARTMENT D on E.dept_id = D.id
where E.id = 1;
```

✅ inner join

- 두 테이블에서 join condition을 만족하는 튜플들로 결과 테이블 생성
- join condition에서 null 값을 갖는 튜플은 결과 테이블에 포함되지 못함
- 비교 연산자(=, <, ≠ 등) 사용 가능

```sql
from { 테이블명 } [inner] join { 테이블명 } on { join condition }
```

✅ outer join

- 두 테이블에서 join condition을 만족하지 않는 튜플까지 결과 테이블에 포함
- 마찬가지로 join condition에서 비교 연산자 사용 가능

```sql
from { 테이블명 } left [outer] join { 테이블명 } on { join condition }

from { 테이블명 } right [outer] join { 테이블명 } on { join condition }

# full outer join 은 mysql에서 지원하지 않음
from { 테이블명 } full [outer] join { 테이블명 } on { join condition }
```

🤖 left outer join 예제

- EMPLOYEE 테이블의 TOM은 dept_id가 null
- DEPARTMENT 테이블의 QA가 dept_id인 직원은 없음

```sql
select *
from EMPLOYEE E
left outer join DEPARTMENT D
on E.dept_id = D.id;
```

```
+----+---------+------------+------+-----------+-----------+---------+------+-------------+-----------+
| id | name    | birth_date | sex  | position  | salary    | dept_id | id   | name        | leader_id |
+----+---------+------------+------+-----------+-----------+---------+------+-------------+-----------+
|  1 | MESSI   | 1987-02-01 | M    | DEV_BACK  | 100000000 |    1003 | 1003 | development |         1 |
|  2 | JANE    | 1996-05-01 | F    | DSGN      |  90000000 |    1001 | 1001 | headquarter |         4 |
...
| 15 | TOM     | 2002-06-09 | M    | DEV_BACK  |  80000000 |    NULL | NULL | NULL        |      NULL |
+----+---------+------------+------+-----------+-----------+---------+------+-------------+-----------+
```

- left outer join 이라서 EMPLOYEE 테이블의 튜플 중 join condition에 매칭되지 않는 튜플까지 조회됨

🤖 right outer join 예제

```sql
select * 
from EMPLOYEE E
right outer join DEPARTMENT D
on E.dept_id = D.id;
```

```
+------+---------+------------+------+-----------+-----------+---------+------+-------------+-----------+
| id   | name    | birth_date | sex  | position  | salary    | dept_id | id   | name        | leader_id |
+------+---------+------------+------+-----------+-----------+---------+------+-------------+-----------+
|    2 | JANE    | 1996-05-01 | F    | DSGN      |  90000000 |    1001 | 1001 | headquarter |         4 |
|    5 | DINGYO  | 1990-11-05 | M    | CTO       | 120000000 |    1001 | 1001 | headquarter |         4 |
|    1 | MESSI   | 1987-02-01 | M    | DEV_BACK  | 100000000 |    1003 | 1003 | development |         1 |
...
| NULL | NULL    | NULL       | NULL | NULL      |      NULL |    NULL | 1006 | QA          |      NULL |
+------+---------+------------+------+-----------+-----------+---------+------+-------------+-----------+
```

- right outer join 이라서, DEPARTMENT 테이블 중 Join condition에 만족하지 않는 튜플까지 조회
- EMPLOYEE 테이블 중 join condition에 만족하지 못하면 조회되지 않음

# 4. equi join

✅ equi join

- join condition에 `=`(equality comparator)를 사용하는 join

# 5. using

🤖 using 사용이 필요한 예제

```sql
select *
from EMPLOYEE E
left outer join DEPARTMENT D
on E.dept_id = D.dept_id;
```

- 위와 같이 join condition에서 같은 컬럼명을 조회하는 경우
- 결과 테이블에 `dept_id`가 중복됨

```
+----+---------+------------+------+-----------+-----------+---------+---------+-------------+-----------+
| id | name    | birth_date | sex  | position  | salary    | dept_id | dept_id | name        | leader_id |
+----+---------+------------+------+-----------+-----------+---------+---------+-------------+-----------+
|  1 | MESSI   | 1987-02-01 | M    | DEV_BACK  | 100000000 |    1003 |    1003 | development |         1 |
...
| 14 | SAM     | 1992-08-04 | M    | DEV_INFRA |  70000000 |    1002 |    1002 | HR          |      NULL |
| 15 | TOM     | 2002-06-09 | M    | DEV_BACK  |  80000000 |    NULL |    NULL | NULL        |      NULL |
+----+---------+------------+------+-----------+-----------+---------+---------+-------------+-----------+
```

- 이때 using을 사용하면 결과 테이블의 중복을 줄일 수 있음

```sql
select *
from EMPLOYEE E
left outer join DEPARTMENT D
using (dept_id);
```

```
+---------+----+---------+------------+------+-----------+-----------+-------------+-----------+
| dept_id | id | name    | birth_date | sex  | position  | salary    | name        | leader_id |
+---------+----+---------+------------+------+-----------+-----------+-------------+-----------+
|    1003 |  1 | MESSI   | 1987-02-01 | M    | DEV_BACK  | 100000000 | development |         1 |
...
|    1005 | 13 | JISUNG  | 1987-07-07 | M    | PO        |  90000000 | product     |        13 |
|    1002 | 14 | SAM     | 1992-08-04 | M    | DEV_INFRA |  70000000 | HR          |      NULL |
|    NULL | 15 | TOM     | 2002-06-09 | M    | DEV_BACK  |  80000000 | NULL        |      NULL |
+---------+----+---------+------------+------+-----------+-----------+-------------+-----------+
```

✅ using

- join condition에서 `=` 를 사용할 때, attribute의 이름이 같다면 using으로 간단하게 작성 가능
- 이때 같은 attribute는 결과 테이블에서 1번만 표시됨

# 6. natural join

✅ natural join

- 두 테이블에서 같은 이름을 갖는 모든 attribute pair에 대해 equi join을 수행
- join condition을 따로 명시하지 않음

```sql
from { 테이블명 } natural join { 테이블명 }

from { 테이블명 } natural left join { 테이블명 }
from { 테이블명 } natural right join { 테이블명 }
```

- 만약 두 테이블에서 동일한 컬럼명이 2개 있다면 → 둘 다 같은 결과만 반환

# 7. cross join

✅ cross join

- 두 테이블의 튜플 페어로 만들 수 있는 모든 조합(Cartesian product)을 결과 테이블로 생성
- join condition이 없음

```sql
# implicit cross join
from table1, table2

# explicit cross join
from table1 cross join table2
```

✅ MySQL에서 cross join

- cross join = inner join = join
- cross join에서 on, using을 같이 사용하면 inner join으로 동작
- inner join에서 on, using 없이 사용하면 cross join으로 동작



# 8. self join

✅ self join

- 테이블이 자기 자신에게 join

# 9. join 사용 예제

🤖 id가 1003인 부서에 속하는 임직원 중 리더를 제외한 부서원의 id, 이름, 연봉 조회

```sql
# 쿼리 1 : ❌
select E.id, E.name, E.salary
from EMPLOYEE E
join DEPARTMENT D 
on D.dept_id = 1003
and E.id != D.leader_id
and E.dept_id = D.dept_id
```

```sql
# 쿼리 2 : ⭕️
select E.id, E.name, E.salary
from EMPLOYEE E
join DEPARTMENT D on E.dept_id = D.dept_id
where E.dept_id = 1003
and E.id != D.leader_id;
```

💡 쿼리 비교

- 쿼리 1과 2는 성능 차이는 없지만, 쿼리 1의 가독성이 나쁨
- ∵ join과 where 절을 구분하지 않아서
- 쿼리를 짤 때 다음과 같은 순서로 설계하기

```sql
1. 어떤 테이블이 필요한가?
-> from 절에 join 조건으로 테이블 연결

2. 테이블 중 어떤 데이터만 볼 것인가?
-> where 절에서 필터링
```

🤖 id가 2001인 프로젝트에 참여한 임직원의 이름, 직군, 소속 부서 이름 조회

```sql
select E.name, E.position, D.dept_name
from EMPLOYEE E
join WORKS_ON W
on E.id = W.empl_id
left join DEPARTMENT D
on E.dept_id = D.dept_id
where proj_id = 2001
order by E.id
```

💡 inner join, outer join

- 조인할 테이블의 정보가 필수라면 → inner join
- 필수가 아니라서 null이 나와도 된다면 → outer join

→ 위 쿼리에서 부서 이름은 null이 나올 수 있으므로 left join을 하는게 맞음

💡 `WHERE` vs `ON`에 조건 넣을 때 주의

> 만약 LEFT JOIN을 했는데 `WHERE D.dept_name = 'DSGN'` 이런 식으로 쓰면?
>
- LEFT JOIN 의미가 사라짐 (내부적으로 다시 INNER JOIN처럼 작동)