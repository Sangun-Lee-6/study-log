# Ch 9. 그룹 짓기(grouping), 집계 함수(aggregate function), 정렬하기(ordering)

# 1. order by

✅ order by

- 조회 결과를 특정 attribute나 여러 attributes 기준으로 정렬하여 가져오고 싶을 때 사용
- 오름차순 정렬 : ASC(기본값)
- 내림차순 정렬 : DESC

🤖 임직원의 정보를 연봉 순서대로 정렬해서 조회

```sql
select *
from EMPLOYEE E
order by E.salary asc
```

🤖 임직원의 정보를 같은 부서끼리 연봉 순서대로 정렬해서 조회

```sql
select *
from EMPLOYEE E
order by E.dept_id asc ,E.salary asc
```

- MySQL은 null 먼저 정렬

# 2. aggregate function

✅ aggregate function

- 여러 튜플의 정보를 요약해서 하나의 값으로 추출하는 함수
- ex. count, sum, max, min, avg
- 주로 관심있는 attribute에 사용됨 → ex. avg(salary)
- null 은 제외하고 요약 값을 추출

🤖 임직원 수를 조회

```sql
select count(*)
from EMPLOYEE;
```

- 만약 count() 안에 null 값이 있는 attribute를 넣는다면, 해당 튜플은 제외하고 집계됨

🤖 프로젝트 2002에 참여한 임직원 수와 최대 연봉, 최소 연봉, 평균 연봉 조회

```sql
select count(*), max(salary), min(salary), avg(salary)
from EMPLOYEE E
join WORKS_ON W
on E.id = W.empl_id
where W.proj_id = 2002
```

# 3. group by

🤖 각 프로젝트에 참여한 임직원 수와 평균 연봉을 조회

```sql
select W.proj_id, count(*), avg(salary)
from EMPLOYEE E
join WORKS_ON W
on E.id = W.empl_id
group by W.proj_id
```

✅ group by

- 관심 있는 attribute 기준으로 그룹을 나눠서 그룹별로 aggregate function을 사용하고 싶을 때 사용
- grouping attribute(s) : 그룹을 나누는 기준이 되는 attribute
- grouping attribute에 null이 있을 때는 null끼리 묶임

✅ group by를 사용할 때 select 절의 조건

- select 절에 있는 컬럼은 반드시 다음 중 하나

1. group by 절에 포함되어 있는 컬럼

2. 집계 함수를 통해 표현

# 4. having

🤖 프로젝트 참여 인원이 4명 이상인 프로젝트에 대해, 각 프로젝트에 참여한 직원 수와 최대 연봉 조회

```sql
select W.proj_id, count(*), avg(E.salary)
from EMPLOYEE E
join WORKS_ON W
on E.id = W.empl_id
group by W.proj_id
having count(*) >= 4
```

- grouping 결과에 대해 필터링을 걸고 싶을 때 : having 사용

✅ having

- group by와 함께 사용됨
- aggregate function의 결과값을 바탕으로 그룹을 필터링하고 싶을 때 사용

# 5. 예제

🤖 각 부서별 인원 수를 인원 수가 많은 순서대로 정렬

```sql
select E.dept_id, count(*)
from EMPLOYEE E
group by E.dept_id
having E.dept_id is not null
order by count(*) desc
```

🤖 각 부서별-성별 인원 수를 인원 수가 많은 순서대로 정렬

```sql
select E.dept_id, E.sex, count(*)
from EMPLOYEE E
group by E.dept_id, E.sex
order by count(*) desc
```

- 이렇게 group by에 1개 이상 attribute를 적을 수 있음

```
+---------+------+----------+
| dept_id | sex  | count(*) |
+---------+------+----------+
|    1003 | M    |        2 |
|    1001 | F    |        2 |
|    1001 | M    |        2 |
...
|    NULL | M    |        1 |
+---------+------+----------+
```

🤖 회사 전체 평균 연봉보다 평균 연봉이 적은 부서들의 평균 연봉 조회

```sql
select avg(salary)
from EMPLOYEE
```

```sql
select E.dept_id, avg(salary)
from EMPLOYEE E
group by E.dept_id
having avg(salary) < (
	select avg(salary)
	from EMPLOYEE
);
```

🤖 각 프로젝트별로 프로젝트에 참여한 90년대생들의 수와 이들의 평균 연봉 조회

```sql
select W.proj_id, count(*), round(avg(E.salary),0)
from EMPLOYEE E
join WORKS_ON W
on E.id = W.empl_id
where E.birth_date < '2000-01-01'
group by W.proj_id
```

🤖 프로젝트의 참여 인원이 4명 이상일 때, 각 프로젝트별로 프로젝트에 참여한 90년대생들의 수와 이들의 평균 연봉 조회

```sql
select W.proj_id, count(*), round(avg(E.salary),0)
from EMPLOYEE E
join WORKS_ON W
on E.id = W.empl_id
where E.birth_date < '2000-01-01'
and W.proj_id in (
 select W.proj_id
 from WORKS_ON W
 group by W.proj_id
 having count(*) >= 4
)
group by W.proj_id
```

- 조건에 따라, where 절에 조건을 걸어야하는지 having 절에 조건을 걸어야하는지 구분해야함

💡 where 조건과 having 조건 구분

- 개별 레코드를 필터링 ⇒ where 절에 조건이 들어감
- 그룹, 집계 결과를 필터링 ⇒ having 절에 조건이 들어감

# 6. select 요약

✅ select 요약

```sql
select attribute(s) || aggregate function(s)
from table(s)
where condition
group by group_attributes
having group_conditions
order by attribute
```

✅ select 실행 순서(개념적인 순서)

1. from

2. where

3. group by

4. having

5. order by

6. select