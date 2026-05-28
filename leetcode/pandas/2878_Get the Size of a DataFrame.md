> 링크 : https://leetcode.com/problems/get-the-size-of-a-dataframe/description/?envType=study-plan-v2&envId=introduction-to-pandas&lang=pythondata

## 1. Grain 확인

- 원본 DataFrame의 row 1개는 무엇인가?
  - player 1명
- 최종 결과 row 1개는 무엇이어야 하는가?
  - DataFrame의 사이즈(행, 열)
  - 배열 1개

## 2. SQL 흐름으로 변환

- SQL 변환 흐름은 필요 없음

## 3. 출력 형태 검증

- 컬럼명, NaN, 중복 key 확인 필요 없음

## 4. 핵심 문법

| 목적                   |                               문법 | 의미                         |
| ---------------------- | ---------------------------------: | ---------------------------- |
| 행/열 개수 동시에 확인 |                         `df.shape` | `(행 수, 열 수)` 튜플 반환   |
| 행 수                  |                      `df.shape[0]` | row count                    |
| 열 수                  |                      `df.shape[1]` | column count                 |
| 행 수                  |                          `len(df)` | row count                    |
| 전체 원소 개수         |                          `df.size` | `행 수 × 열 수`              |
| 메모리 사용량          |                `df.memory_usage()` | 컬럼별 메모리 사용량         |
| 전체 메모리 사용량     | `df.memory_usage(deep=True).sum()` | 실제 문자열 포함 메모리 추정 |
