> 링크 : https://leetcode.com/problems/best-time-to-buy-and-sell-stock/description/

## 1. 입력, 출력 형식을 먼저 확인

- 입력 : 배열, 각 원소의 타입은 숫자
- 출력 : 숫자, 의미는 최대 수익금

## 2. 가장 단순한 풀이 생각(ex. brute force), 시간복잡도

- 입력된 배열을 2개로 만들어서, 모든 경우의 수를 따져봐서 최대 수익금 계산
- 이 풀이는 O(n^2)

## 3. 문제 상황을 해결하는 자료구조 생각, Trade off 고려(시간복잡도 등)

- 시간축의 특성이 있으므로, 슬라이딩 윈도우로 문제 해결
- 즉, 이전에 가격으로 팔 수 없으므로, 항상 이후의 가격만 고려하면 됨

```python
class Solution:
    def maxProfit(self, prices: List[int]) -> int:

        max_profit=0
        left=0
        right=1

        while right<len(prices):
            if prices[left] < prices[right]:
                profit = prices[right]-prices[left]
                max_profit = max(max_profit, profit)
            else:
                left = right
            right += 1



        return max_profit
```

## 4. 먼저 정상 케이스를 구현하고, 빈 배열/중복/음수 같은 edge case를 확인

- prices의 길이가 1이상이므로 빈 배열 없음
- prices의 원소의 크기가 0이상 10,000이하이므로, 음수는 없음
- 중복 원소는 들어올 수 있음
