> 링크 : https://leetcode.com/problems/two-sum/description/

# 1. 입력, 출력 형식을 먼저 확인

- 입력 1 : nums, 배열, 원소는 integers
- 입력 2 : target, integers
- 출력 : 배열, 인덱스

- 출력의 원소 순서는 상관 없음, 출력의 원소는 2개
- 딱 하나의 출력만 나오게 문제가 주어짐

# 2. 가장 단순한 풀이 생각(ex. brute force), 시간복잡도

- 단순한 풀이 : nums를 복사해서, 모든 원소와 다 더해보고 target과 비교하는 방식
- 그러나 이 방식은 O(n^2)이므로 다른 방식을 고려

# 3. 문제 상황을 해결하는 자료구조 생각, Trade off 고려(시간복잡도 등)

- 현재 숫자(`num`)를 보고, `target-num`이 이전에 나왔는지 확인
- 현재 숫자가 num이라고 한다면, 필요한 짝은 정해져 있음, target-num
- target-num 이라는 숫자를 조회할 때 O(n)이 아니라 O(1)로 빠르게 조회하기 위해 hashmap 사용
- 순회할 때마다 dict에 num의 값을 key로 저장하면 됨

```python
class Solution:
    def twoSum(self, nums, target):

        hash_dict = {}
        result = []

        for i,n in enumerate(nums):
            pair = target - n
            if pair in hash_dict:
                num_index = i
                pair_index = hash_dict.get(pair)
                result.append(num_index)
                result.append(pair_index)
            else:
                hash_dict[n]=i

        return result
```

# 4. 먼저 정상 케이스를 구현하고, 빈 배열/중복/음수 같은 edge case를 확인

- 최소 길이 : [3,3] 도 처리 가능
- 중복 값 : [3,3] 처리 가능
- 음수 : [-1,-2] 도 처리 가능
