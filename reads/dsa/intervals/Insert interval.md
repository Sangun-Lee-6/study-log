# Insert interval

> 링크 : https://leetcode.com/problems/insert-interval/description/

새로운 구간을 올바른 위치에 넣고, 겹치는 구간은 하나로 합치기

```python
class Solution:
    def insert(self, intervals, newInterval):
        result = []
        i = 0

        # 1. newInterval보다 왼쪽에 있는 구간들 추가
        while i < len(intervals) and intervals[i][1] < newInterval[0]:
            result.append(intervals[i])
            i += 1

        # 2. newInterval과 겹치는 구간들 병합
        while i < len(intervals) and intervals[i][0] <= newInterval[1]:
            newInterval[0] = min(newInterval[0], intervals[i][0])
            newInterval[1] = max(newInterval[1], intervals[i][1])
            i += 1

        result.append(newInterval)

        # 3. newInterval보다 오른쪽에 있는 구간들 추가
        while i < len(intervals):
            result.append(intervals[i])
            i += 1

        return result
```

각 구간을 한 번만 봄 → 따라서 O(n)
