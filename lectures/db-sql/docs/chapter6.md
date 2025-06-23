# Ch 6. 쿼리 안의 쿼리 (subquery)

# 1. subquery 예제

🤖 ID가 14인 임직원보다 생일이 빠른 임직원의 id, 이름, 생일 조회

- 아래 두 쿼리를 한 번에 실행할 때, subquery 사용

```sql
select birth_date
from EMPLOYEE
where id = 14;

# 1992-08-04
```

```sql
select id, name, birth_date 
from EMPLOYEE
where birth_date < '1992-08-04';
```

```sql
select id, name, birth_date 
from EMPLOYEE
	where birth_date < (
		select birth_date
		from EMPLOYEE
		where id = 14
);
```

✅ 용어 정리

- subquery(nested query, inner query) : select, insert, update, delete 안에 포함된 쿼리
- outer query(main query) : subquery를 포함하는 쿼리
- subquery는 `( )` 안에 작성됨

🤖 id가 1인 임직원과 같은 부서, 같은 성별인 임직원들의 id, 이름, position 조회

```sql
select dept_id, sex
from EMPLOYEE
where id = 1;
```

```sql
select id, name, position
from EMPLOYEE
where (dept_id, sex) = 
	(select dept_id, sex
	from EMPLOYEE
	where id = 1);
```

# 2. IN 키워드

🤖 id가 5인 임직원과 같은 프로젝트에 참여한 임직원들의 id 조회

```sql
select proj_id 
from WORKS_ON
where empl_id = 5;
```

```sql
select id, name
from EMPLOYEE
where id in (
	select empl_id 
	from WORKS_ON W
	where proj_id IN 
		(select proj_id 
		from WORKS_ON
		where empl_id = 5)
	and empl_id != 5);
```

- 서브쿼리가 where 절 뿐만 아니라 from 절에도 올 수 있음

# 3. EXIST 키워드

🤖 id가 7 또는 12인 임직원이 참여한 프로젝트의 id와 이름 조회

- IN 키워드를 사용한 쿼리

```sql
select P.id projectId, P.name projectName
from PROJECT P
where P.id in (
	select proj_id
	from WORKS_ON W
	where W.empl_id in (7,12)
);
```

- EXIST 키워드를 사용한 쿼리

```sql
select P.id, P.name
from PROJECT P
where exists(
	select *
	from WORKS_ON W
	where W.proj_id = P.id
	and W.empl_id in (7,12)
	);
```

✅ exist 키워드

- exist 절 안에 있는 레코드가 존재하면
- 해당 레코드의 `id`와 `name`을 조회

🆚 exist, not exist

- exist : subquery의 결과가 최소 하나의 row라도 있다면 true를 반환
- not exist : subquery의 결과가 단 하나의 row도 없다면 true를 반환

# 4. NOT EXIST 키워드

🤖 2000년대생이 없는 부서의 id와 이름 조회

```sql
select D.id, D.name
from department D
where not exists(
	select *
	from EMPLOYEE E
	where E.dept_id = D.id
	and E.birth_date >= '2000-01-01'
	);
```

- D 테이블의 레코드 하나하나에 대해 not exists 안의 서브 쿼리를 실행해서, 결과가 없으면 해당 레코드를 반환

✅ exist나 not exist는 outer 쿼리부터 읽는 것이 쉬움

# 5. ANY 키워드

🤖 리더보다 높은 연봉을 받는 부서원을 가진 리더의 id, 이름, 연봉 조회

```sql
select E.id, E.name, E.salary
from DEPARTMENT D, EMPLOYEE E
where D.leader_id = E.id
and E.salary < any(
	select salary
	from EMPLOYEE
	where id != D.leader_id
	and dept_id = E.dept_id);
```

# 6. ALL 키워드

🤖 id가 13인 직원과 한번도 같은 프로젝트에 참여하지 못한 임직원의 id, 이름, 직군 조회

- not in 키워드를 사용한 쿼리

```sql
select E.id, E.name, E.position
from WORKS_ON W, EMPLOYEE E
where W.empl_id = E.id 
and W.proj_id not in (select distinct proj_id
from WORKS_ON
where empl_id = 13);
```

- all 키워드를 사용한 쿼리

```sql
select distinct E.id, E.name, E.position
from EMPLOYEE E, WORKS_ON W
where E.id = W.empl_id
and W.proj_id != ALL (
	select proj_id
	from WORKS_ON
	where empl_id = 13
	);
```