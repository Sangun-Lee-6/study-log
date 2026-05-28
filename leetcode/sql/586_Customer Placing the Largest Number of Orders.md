> 링크 : https://leetcode.com/problems/customer-placing-the-largest-number-of-orders/description/?utm_source=chatgpt.com

## 1. Grain 확인

- 원본 테이블의 row 1개는 무엇인가?
  - 주문 1건, customer_number는 중복 가능
- 최종 결과 row 1개는 무엇이어야 하는가?
  - 고객 1명(가장 많은 주문을 한 고객)

## 2. 변환 방식 선택

- GROUP BY : 고객마다 주문 수 측정
- ORDER BY : 가장 많은 주문을 한 고객 측정
- LIMIT : 1명

## 3. 검증

- NULL, 중복, JOIN 후 row 증가, 집계 기준이 맞는지 확인

## 4. CTE를 사용해서 푸는 이유

- 이 문제는 CTE 없이 GROUP BY만 사용해서 문제를 해결할 수 있다.
  - 그리고 요구사항도 잘 읽힌다.(고객별 주문 수를 세고, 가장 큰 고객 1명을 뽑기)

  ```sql
  SELECT
      customer_number
  FROM Orders
  GROUP BY customer_number
  ORDER BY COUNT(*) DESC
  LIMIT 1;
  ```

- 그러나 실무에서 확장 가능하고(ex. 최근 30일 주문 수 조건 추가), 가독성이 좋고(중간 결과를 네이밍할 수 있으므로), 디버깅이 가능하고, 유지보수성이 좋은 쿼리는 CTE를 사용한 쿼리다.
  - 집계는 CTE, 최종 선택(order by, limit)은 outer query

  ```sql
  WITH customer_order_counts AS (
      SELECT
          customer_number,
          COUNT(*) AS order_count
      FROM Orders
      GROUP BY customer_number
  )
  SELECT
      customer_number
  FROM customer_order_counts
  ORDER BY order_count DESC
  LIMIT 1;
  ```
