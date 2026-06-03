> 링크 : https://leetcode.com/problems/contains-duplicate/description/

## 1. 입력, 출력 형식을 먼저 확인

- input : 배열, 각 원소는 숫자, 1 <= nums.length <= 10^5, nums[i]는 음수도 가능
- output : boolean, 모든게 distinct면 return false

## 2. 가장 단순한 풀이 생각(ex. brute force), 시간복잡도

- nums를 순회하면서, 순회한 원소를 다른 배열에 저장한다. 그리고 순회한 원소가 해당 배열에 있는지 검색해서, 있으면 return True, 배열을 다 순회할 때까지 없으면 return False
- 원소에 해당 배열이 있는지 순회할 때 O(n)이므로, 총 시간 복잡도는 O(n^2), 그러나 배열의 길이가 10^5이므로 시간초과

## 3. 문제 상황을 해결하는 자료구조 생각, Trade off 고려(시간복잡도 등)

- 해당 배열에 원소가 있는지 검색할 때 HashMap을 사용 : 특정 키가 있는지 조회할 때 O(1)

```python

class Solution:
    def containsDuplicate(self, nums: List[int]) -> bool:

        hash_dict={}
        for n in nums:
            if n in hash_dict:
                return True
            else:
                hash_dict[n] = True
        return False

```

## 4. 먼저 정상 케이스를 구현하고, 빈 배열/중복/음수 같은 edge case를 확인

- 음수 들어올 수 있음, 빈배열은 안들어옴, 중복은 가능
