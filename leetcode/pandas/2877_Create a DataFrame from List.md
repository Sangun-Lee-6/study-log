> 링크 : https://leetcode.com/problems/create-a-dataframe-from-list/

## 1. Grain 확인

- 원본 DataFrame의 row 1개는 무엇인가?
  - student_data의 row 1개, 즉 학생 정보 1개
- 최종 결과 row 1개는 무엇이어야 하는가?
  - 학생 1명의 정보
  - 컬럼 : student_id, age

## 2. SQL 흐름으로 변환

- 단순히 1차원 배열을 DataFrame으로 변환

## 3. 출력 형태 검증

- 컬럼명 : student_id, age
- Nan, 중복 key는 별도 처리 필요 없음

## 4.

### pandas 라이브러리 가져오기

```python
import pandas as pd
```

### pd.DataFrame() : list, dict, 2차원 list 같은 데이터를 pandas의 표 형태 객체인 DataFrame으로 만들어주는 생성자

1. 2차원 리스트

   ```python
   data = [
       [1,15],
       [2,11]
   ]

   df = pd.DataFrame(data, columns=["student_id", "age"])
   ```

   - 이러면 결과로 표 형태의 객체인 DataFrame으로 변환됨
   - 2차원 리스트의 원소 = df의 row 1개

2. 딕셔너리

   ```python
   data={
       "student_id":[1,2,3],
       "age":[15, 11, 20]
   }

   df = pd.DataFrame(data)
   ```

   - key는 df의 column name, value는 df의 column value

3. 딕셔너리 리스트
   - API 응답이나 JSON 처리에서 자주 사용됨

   ```python
   data = [
       {"student_id":1, "age":15},
       {"student_id":2, "age":11},
       {"student_id":3, "age":20},
   ]

   df = pd.DataFrame(data)
   ```

   - 각 dict는 df의 row 1개, dict의 key는 df의 column name

### 주의할 점

1. 1차원 배열도 들어갈 수는 있다, 이때 컬럼명 지정 필요
2. 컬럼 개수는 데이터 구조와 맞아야 한다.
