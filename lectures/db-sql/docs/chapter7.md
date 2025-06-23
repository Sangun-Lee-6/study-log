# Ch 7. three-valued logic

# 1. NULL

✅ SQL에서 NULL의 의미

1. unknown : 아직 알려지지 않음

2. unavailable || withheld : 이용할 수 없음

3. not applicable : 해당사항이 없음

✅ SQL에서 NULL은 비교 연산자(`=, !=`)를 사용할 수 없음

- NULL = NULL 은 true나 false가 아니라 unknown(알수 없음)

🤖 `birth_date`가 NULL인 임직원을 조회하고 싶을 때, is를 사용해야함

```sql
# ❌ 
select id from EMPLOYEE where birth_date = null;

# ⭕️ 
select id from EMPLOYEE where birth_date is null;
```

# 2. three-valued logic

✅ SQL에서 NULL과 비교 연산을 하게 되면, 그 결과는 true, false가 아닌 unknown

✅ three-valued logic의 의미

- 비교/논리 연산의 결과는 3개 : true, false, unknown

✅ NULL 비교 연산 결과

- 연산 대상에 NULL이 있으면 결과는 무조건 unknown

| 비교 연산 예제 | 결과 |
| --- | --- |
| 1 = 1 | true |
| 1 != 1 | false |
| 1 = NULL | unknown |
| 1 != NULL | unknown |
| NULL = NULL | unknown |

# 3. UNKNOWN 논리 연산 결과

✅ AND 연산

|  |  | 연산 결과 |
| --- | --- | --- |
| true | true | true |
|  | false | false |
|  | unknown | unknown |
| false | false | false |
|  | unknown | false |
| unknown | unknown | unknown |

✅ NOT 연산

|  | 연산 결과 |
| --- | --- |
| true | false |
| false | true |
| unknown | unknown |

✅ OR 연산

|  |  | 연산 결과 |
| --- | --- | --- |
| true | true | true |
|  | false | true |
|  | unknown | true |
| false | true | true |
|  | false | false |
|  | unknown | unknown |
| unknown | unknown | unknown |

# 4. where condition

✅ where condition은 true인 레코드만 선택됨

- 즉, 결과가 false이거나 unknown이면 레코드는 선택되지 않음
- 따라서 조건절에 null이 있다면 주의할 것

# 5. NOT IN 주의 사항

✅ A not in ( B, C, D ) 의미

- A ≠ B and A ≠ C and A ≠ D 해야 true
- B, C, D 중 하나라도 NULL 이라면 unknown이 나올 수 있음

🤖 예제

|  | 결과 |  |
| --- | --- | --- |
| 3 not in (1,2,4) | true | 3이 없으므로 |
| 3 not in (1,2,3) | false | 3이 있으므로 |
| 3 not in (1,3,null) | false | 3이 있으므로 |
| 3 not in (1,2,null) | unknown |  |

🤖 예제 : NOT IN 사용 시 주의사항

```sql
# 2000년대생이 없는 부서의 id와 이름 조회
select D.id, D.name
from DEPARTMENT D
where D.id not in (
	select E.dept_id
	from EMPLOYEE E
	where E.birth_date >= '2000-01-01'
	);
```

- 문제 : 만약 dept_id에 NULL이 있다면 메인 쿼리는 아무 결과도 반환받지 못함
- 해결 1 : NOT EXISTS 키워드 사용
- 해결 2 : dept_id is not null 조건 추가