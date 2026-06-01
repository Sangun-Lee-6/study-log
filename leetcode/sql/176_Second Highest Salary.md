> 링크 : https://leetcode.com/problems/second-highest-salary/description/

## 1. Grain 확인

- 원본 테이블의 row 1개는 무엇인가?
  - 원본 grain = Employee 1명
- 최종 결과 row 1개는 무엇이어야 하는가?
  - 최종 grain = Salary 정보(없으면 null)

## 2. 변환 방식 선택

- CTE로 대상 선정
- subquery로 값이 없으면 return NULL

```sql
WITH ranked_salary AS (
    SELECT DISTINCT salary
    FROM Employee
    ORDER BY salary DESC
    LIMIT 1 OFFSET 1
)
SELECT (
    SELECT salary
    FROM ranked_salary
) AS SecondHighestSalary;
```

## 3. 검증
