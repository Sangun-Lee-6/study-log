> 링크 : https://leetcode.com/problems/managers-with-at-least-5-direct-reports/description/

## 1. Grain 확인

### 원본 테이블 row 1개

- grain : 직원 1명
- 각 직원의 속성 중에 매니저가 누구인지 들어있음

### 최종 결과 row 1개

- 출력해야 하는 row 1개는 매니저 1명의 이름
- grain = 매니저 1명
- 이 매니저의 속성으로 부하 직원의 수가 5명 이상

---

## 2. 변환 방식 선택

- `SELECT name FROM Employee GROUP BY managerId`만 한다면, grain은 결국 직원들이 된다.
- 최종 grain은 매니저 1명만 나와야하므로, grain이 올바르지 않다.
- 따라서 우선 managerId만 먼저 선택하고, 이를 바탕으로 name 속성까지 추출해야하므로 CTE를 사용하는 것이 깔끔하다.

```sql
WITH get_manager AS (
    SELECT
        managerId
    FROM Employee
    WHERE managerId IS NOT NULL
    GROUP BY managerId
    HAVING COUNT(*) >=5
)
SELECT
    e.name
FROM Employee e
JOIN get_manager m
    ON e.id = m.managerId
```

## 3. 검증

- NULL : managerId IS NOT NULL로 매니저 없는 직원은 집계에서 제외
- 중복 : id는 PK라서 JOIN할 때 조인 중복 증가 없음

## 4. 핵심 패턴

GROUP BY로 만든 row의 의미와 최종 SELECT row의 의미가 다르면, 바로 SELECT하지 않고 JOIN이 필요하다.
