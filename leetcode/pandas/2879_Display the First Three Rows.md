> 링크 : https://leetcode.com/problems/display-the-first-three-rows/description/

## 1. Grain 확인

- 원본 DataFrame의 row 1개는 무엇인가?
  - grain = 직원 1명
- 최종 결과 row 1개는 무엇이어야 하는가?
  - grian = 직원 1명

## 2. SQL 흐름으로 변환

- grain이 같으므로 별도의 변환은 필요 없음

## 3. 출력 형태 검증

- 상단의 3개의 row만 반환하면 됨

```python
import pandas as pd

def selectFirstRows(employees: pd.DataFrame) -> pd.DataFrame:
    return employees.head(3)
```

- df.head(n)
  - 앞쪽 n개의 row를 가져오는 메서드
  - 데이터 확인용으로 주로 사용됨
