> 링크 : https://leetcode.com/problems/nth-highest-salary/description/

## 1. Grain 확인

- 원본 테이블의 row 1개는 무엇인가?
  - Employee 1명의 정보, 속성으로 salary
- 최종 결과 row 1개는 무엇이어야 하는가?
  - salary 정보 1개

## 2. 변환 방식 선택

```sql

CREATE FUNCTION getNthHighestSalary(N INT) RETURNS INT
BEGIN
    SET N = N - 1;

    RETURN (
        WITH ranked_salary AS (
            SELECT DISTINCT salary
            FROM Employee
            ORDER BY salary DESC
            LIMIT 1 OFFSET N # N개를 건너뛰고 1개만 가져오기
        )
        SELECT salary
        FROM ranked_salary
    );
END

```

## 3. 검증

- NULL, 중복, JOIN 후 row 증가, 집계 기준이 맞는지 확인
