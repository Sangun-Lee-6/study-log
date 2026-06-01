# 템플릿

> 링크 :

## 1. Grain 확인

- 원본 DataFrame의 row 1개는 무엇인가?
- 최종 결과 row 1개는 무엇이어야 하는가?

## 2. SQL 흐름으로 변환

- filter -> groupby/agg -> merge -> sort -> select/rename

## 3. 출력 형태 검증

- Series/DataFrame 구분
- index reset 필요 여부
- 컬럼명, NaN, 중복 key 확인

# 문제 목록

| 순서 | 문제                                   | 핵심 문법            |                          |
| ---: | -------------------------------------- | -------------------- | ------------------------ |
|    1 | **2877. Create a DataFrame from List** | `pd.DataFrame()`     | raw input → tabular data |
|    2 | **2878. Get the Size of a DataFrame**  | `df.shape`           | row/column count 확인    |
|    3 | **2879. Display the First Three Rows** | `df.head()`          | 샘플링                   |
|    4 | **2880. Select Data**                  | `df.loc[]`           | row filter               |
|    5 | **2881. Create a New Column**          | column assignment    | derived column           |
|    6 | **2882. Drop Duplicate Rows**          | `drop_duplicates()`  | dedup                    |
|    7 | **2883. Drop Missing Data**            | `dropna()`           | null cleansing           |
|    8 | **2884. Modify Columns**               | vectorized operation | column transform         |
|    9 | **2885. Rename Columns**               | `rename()`           | schema standardization   |
|   10 | **2886. Change Data Type**             | `astype()`           | type casting             |
|   11 | **2887. Fill Missing Data**            | `fillna()`           | default value 처리       |
|   12 | **2888. Reshape Data: Concatenate**    | `pd.concat()`        | append / union           |
|   13 | **2889. Reshape Data: Pivot**          | `pivot()`            | long → wide              |
|   14 | **2890. Reshape Data: Melt**           | `melt()`             | wide → long              |
|   15 | **2891. Method Chaining**              | chaining             | pipeline-style transform |

| 우선순위 | 문제                                                   | 핵심 주제                      |                               |
| -------: | ------------------------------------------------------ | ------------------------------ | ----------------------------- |
|        1 | **595. Big Countries**                                 | boolean filtering              | 조건 필터링 기본              |
|        2 | **1757. Recyclable and Low Fat Products**              | multi-condition filter         | `AND` 조건 처리               |
|        3 | **183. Customers Who Never Order**                     | `merge`, anti join             | 미구매 고객, 누락 데이터 탐지 |
|        4 | **1148. Article Views I**                              | filter + distinct              | self-action 제거, unique user |
|        5 | **1683. Invalid Tweets**                               | string length filter           | 데이터 품질 검증              |
|        6 | **1873. Calculate Special Bonus**                      | `np.where`, conditional column | rule-based transform          |
|        7 | **1667. Fix Names in a Table**                         | string normalization           | 이름/텍스트 정규화            |
|        8 | **1517. Find Users With Valid E-Mails**                | regex                          | validation rule               |
|        9 | **1527. Patients With a Condition**                    | string pattern matching        | semi-structured text filter   |
|       10 | **570. Managers with at Least 5 Direct Reports**       | `groupby().size()`             | group aggregation             |
|       11 | **586. Customer Placing the Largest Number of Orders** | groupby + sort                 | top entity 추출               |
|       12 | **511. Game Play Analysis I**                          | groupby + min                  | first event                   |
|       13 | **550. Game Play Analysis IV**                         | cohort / retention             | next-day retention            |
|       14 | **607. Sales Person**                                  | multi-table merge              | join chain                    |
|       15 | **1321. Restaurant Growth**                            | rolling window                 | 7-day moving metric           |
