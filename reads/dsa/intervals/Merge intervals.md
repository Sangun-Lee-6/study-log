> 링크 : https://leetcode.com/problems/merge-intervals/description/

- 겹치는 구간들을 하나의 큰 구간으로 합치는 문제
- 핵심 로직

```
1. 시작점 기준으로 정렬한다.
2. 결과 배열 result를 만든다.
3. interval을 하나씩 보면서
   - result의 마지막 구간과 안 겹치면 새로 추가
   - 겹치면 result의 마지막 구간을 확장
```

- 현재 구간이 result의 마지막 구간과 겹치는지 확인하는게 중요함

```python
class Solution:
    def merge(self, intervals):
        # 1. 시작점 기준 정렬
        intervals.sort(key=lambda x: x[0]) # O(n log n)

        result = []

        # 2. interval을 하나씩 확인
        for interval in intervals:
            # result가 비어 있거나, 마지막 구간과 안 겹치면 추가
            if not result or result[-1][1] < interval[0]:
                result.append(interval)

            # 겹치면 마지막 구간을 확장
            else:
                result[-1][1] = max(result[-1][1], interval[1])

        return result
```

- `intervals.sort(key=lambda x: x[0])`
  - 2차원 배열을 각 구간의 첫번째 원소를 기준으로 오름차순 정렬
  - `key` : 정렬 기준
  - `lambda x: x[0]` : 각 원소 x에서 0번째 값을 꺼내는 것

## 시간복잡도

| 항목 | 복잡도       |
| ---- | ------------ |
| 정렬 | `O(n log n)` |
| 순회 | `O(n)`       |
| 전체 | `O(n log n)` |
| 공간 | `O(n)`       |
