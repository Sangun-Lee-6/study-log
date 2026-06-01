# 주제

> 우선 CTE, Window Function 위주의 문제

# 템플릿

> 링크 :

## 1. Grain 확인

- 원본 테이블의 row 1개는 무엇인가?
- 최종 결과 row 1개는 무엇이어야 하는가?

## 2. 변환 방식 선택

- 필터링: WHERE
- 집계: GROUP BY + HAVING
- 연결: JOIN
- 순위/이전값/누적: WINDOW FUNCTION

## 3. 검증

- NULL, 중복, JOIN 후 row 증가, 집계 기준이 맞는지 확인

# 풀 문제

|     | 문제                                        | 핵심 주제                         | DE 관점에서 배우는 것                                                                 |
| --- | ------------------------------------------- | --------------------------------- | ------------------------------------------------------------------------------------- |
| 1   | **176. Second Highest Salary**              | `DISTINCT`, ranking               | 중복 제거 후 N번째 값 추출. 지표 산출 전 dedup 사고 훈련.                             |
| 2   | **177. Nth Highest Salary**                 | `DENSE_RANK`, CTE                 | 파라미터 기반 N-rank 조회. mart에서 top-N 지표 만들 때 유사.                          |
| 3   | **184. Department Highest Salary**          | `PARTITION BY`, max per group     | 그룹별 대표 row 선택. 부서별 최고값 문제는 실무의 “entity별 최신/최대 record”와 같다. |
| 4   | **185. Department Top Three Salaries**      | `DENSE_RANK`                      | 그룹별 Top-K. 광고/상품/유저별 Top-N 리포트 패턴.                                     |
| 5   | **180. Consecutive Numbers**                | `LAG`, 연속성                     | 이벤트 로그에서 연속 발생 탐지. anomaly/data quality check에 가까움.                  |
| 6   | **601. Human Traffic of Stadium**           | gaps-and-islands                  | 연속 구간 탐지. DAU streak, 장애 지속 구간, 트래픽 피크 탐지에 유용.                  |
| 7   | **626. Exchange Seats**                     | `CASE`, row ordering              | 정렬 기반 row 변환. window 없이도 row 위치를 조작하는 훈련.                           |
| 8   | **1204. Last Person to Fit in the Bus**     | cumulative sum                    | `SUM() OVER(ORDER BY ...)` 누적합. quota, capacity, budget cap 계산 패턴.             |
| 9   | **1321. Restaurant Growth**                 | moving average                    | 7일 이동합/이동평균. 실무 KPI mart의 rolling metric과 직접 연결됨.                    |
| 10  | **1164. Product Price at a Given Date**     | latest value as-of date           | 특정 기준일의 최신 상태 조회. SCD Type 2, snapshot mart 사고와 유사.                  |
| 11  | **550. Game Play Analysis IV**              | first event + retention           | 첫 이벤트 기준 다음날 재방문율. cohort/retention 분석 기본형.                         |
| 12  | **1393. Capital Gain/Loss**                 | conditional aggregation           | 거래 이벤트를 손익 지표로 변환. event → metric 변환 훈련.                             |
| 13  | **1045. Customers Who Bought All Products** | set coverage                      | 전체 조건 만족 고객 탐지. completeness check, coverage validation에 유용.             |
| 14  | **585. Investments in 2016**                | group condition, duplicate filter | 동일값/유니크 조건을 함께 다루는 집계 문제. 데이터 품질 조건 필터링에 좋음.           |
