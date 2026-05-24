# DSA for DE

# 1. 알아야 할 자료구조와 알고리즘

## 1.1 List

- 리스트 : 원소들의 순서를 저장할 수 있는 동적 배열
- 시간복잡도
  - 읽기 : O(1)
    - 인덱스로 원소에 접근하므로
  - 쓰기 : O(1), O(n)
    - 원소를 끝에 추가하는 것은 O(1)
    - 중간에 삽입/제거하는 경우 O(n)

```python
# 리스트 생성 : O(n)
my_list = [1, 2, 3, 4, 5]

# 리스트 읽기(인덱스) : O(1)
element = my_list[2]  # element = 3

# 리스트 쓰기(맨 뒤에 원소 추가) : O(1)
my_list.append(6)  # my_list = [1, 2, 3, 4, 5, 6]

# 리스트 쓰기(특정 원소 삭제) : O(n)
my_list.remove(3)  # my_list = [1, 2, 4, 5, 6]
print(my_list)

# 리스트 정렬 : O(n log n)
my_list.sort()

# 리스트 순회 : O(n)
for idx, elt in enumerate(my_list):
    print(f'enumerate를 사용해 인덱스 {idx}와 요소 {elt}를 얻습니다')

for elt in my_list:
    print(f'반복문으로 요소를 직접 순회합니다: {elt}')
```

---

## 1.2 Dictionary

- 딕셔너리 : key-value 쌍을 저장하는 순서 없는 컬렉션
  - 키를 기준으로 빠른 조회/삽입/삭제가 가능
- 시간 복잡도
  - 읽기 : O(1)
    - 내부적으로 해시 테이블을 사용하므로 키로 값을 조회하는 작업이 빠름
  - 쓰기 : O(1)
    - key-value 쌍을 삽입하거나 삭제하는 작업도 해시 테이블 덕분에 빠름

```python
# 딕셔너리 생성 : O(n)
my_dict = {'a': 1, 'b': 2, 'c': 3}

# 딕셔너리 읽기 : 평균 O(1), 최악 O(n)(ex. 해시충돌)
value = my_dict['b']  # value = 2

# 딕셔너리 쓰기(추가) : 평균 O(1), 최악 O(n)(ex. 내부 해시 테이블 resize를 하는 경우, 기존 데이터 재배치)
my_dict['d'] = 4  # my_dict = {'a': 1, 'b': 2, 'c': 3, 'd': 4}

# 딕셔너리 쓰기(제거) : 평균 O(1), 최악 O(n)(ex. 해시충돌)
del my_dict['a']  # my_dict = {'b': 2, 'c': 3, 'd': 4}
print(my_dict)
```

---

## 1.3 Queue

- FIFO 원칙을 따르는 선형 자료구조
- 읽기 : O(1)
  - deque를 사용하면 맨 앞 요소에 O(1)로 접근 가능
- 쓰기 : O(1)
  - deque를 사용하면 요소 추가, 요소 제거 작업 모두 O(1)

```python
from collections import deque

queue = deque([1, 2, 3])

# 요소 추가
queue.append(4)  # queue = deque([1, 2, 3, 4])

# 요소 제거
element = queue.popleft()  # element = 1, queue = deque([2, 3, 4])

print(f'꺼낸 요소는 {element}이고, 남은 큐는 {queue}입니다')
```

---

## 1.4 Stack

- LIFO 원칙을 따르는 선형 자료구조
  - 요소는 맨 위에 추가되고 맨 위에서 제거됨
- 읽기 : O(1)
  - 맨 위 요소에 접근하는 것은 O(1)
- 쓰기 : O(1)
  - 맨 위 요소에 대해 push, pop은 O(1)

```python
stack = [1, 2, 3]

# 요소 추가(push)
stack.append(4)  # stack = [1, 2, 3, 4]

# 요소 제거(pop)
element = stack.pop()  # element = 4, stack = [1, 2, 3]

print(f'꺼낸 요소는 {element}이고, 남은 스택은 {stack}입니다')
```

---

## 1.5. Set

- 중복 없는 고유한 원소들을 저장하는, 순서 없는 컬렉션
- 읽기 : O(1)
  - 특정 요소가 집합에 있는지 확인하는 작업은 평균적으로 O(1)
- 쓰기 : 추가/삭제 : O(1)
- 집합 연산
  - 합집합 : O(n + m), n/m은 각 집합의 길이
  - 교집합 : O(min(n, m))
  - 차집합 : O(n)

```python
# 집합 생성
my_set = {1, 2, 3, 4, 5}

# 요소 추가
my_set.add(6)  # my_set = {1, 2, 3, 4, 5, 6}

# 요소 제거
my_set.remove(3)  # my_set = {1, 2, 4, 5, 6}

# 멤버십 검사
is_in_set = 4 in my_set  # is_in_set = True

# 집합 연산: 합집합, 교집합, 차집합
other_set = {4, 5, 6, 7, 8}

union_set = my_set.union(other_set)
# union_set = {1, 2, 4, 5, 6, 7, 8}

intersection_set = my_set.intersection(other_set)
# intersection_set = {4, 5, 6}

difference_set = my_set.difference(other_set)
# difference_set = {1, 2}

print(f' my_set은 {my_set}이고 other_set은 {other_set}입니다')
print(f' union_set은 {union_set}입니다')
print(f' intersection_set은 {intersection_set}입니다')
print(f' difference_set은 {difference_set}입니다')
```

---

## 1.6. Counter — collections 모듈

- `Counter`는 Python의 딕셔너리 하위 클래스
  - 해시 가능한 객체의 개술르 셀 때 유용
  - 요소 빈도를 계산할 때 유용

```python
from collections import Counter

# 리스트로 Counter 생성
my_counter = Counter(['apple,' 'banana,' 'apple,' 'orange,' 'banana,' 'apple'])

print(my_counter)
```

---

## 1.7. Heap

- 힙 속성을 만족하는 이진 트리 기반 자료구조
  - Max-heap: 어떤 노드 `i`에 대해서도 `i`의 값이 자식 노드들의 값보다 크거나 같음
  - Min-heap: 어떤 노드 `i`에 대해서도 `i`의 값이 자식 노드들의 값보다 작거나 같음
- 힙은 우선순위 큐를 구현할 때 자주 사용됨
- 읽기 : O(1)
  - 최대값, 최소값은 항상 힙의 루트에 있으므로 빠르게 접근 가능
- 쓰기
  - 삽입 : O(log n)
    - 힙 속성을 유지해아 하므로 트리를 따라 비교하고 필요한 경우 요소 교환 필요
  - 삭제 : O(log n)
    - 최대값, 최소값을 삭제하면 히븡ㄹ 다시 정렬해야 함

```python
import heapq

# 기본적으로 min-heap입니다.
# max-heap을 만들고 싶다면 삽입 전에 숫자에 -1을 곱하면 됩니다.
a = []

heapq.heappush(a, 10)

heapq.heappop(a)
```

---

## 1.8. Graph Search

1. Depth First Search — DFS
   - (링크)

2. Breadth First Search — BFS
   - (링크)

---

## 1.9. Binary Search

- (링크)

# 2. DE 인터뷰에서 자주 나오는 DSA 질문

## 2.1. Intervals

- Insert Interval
- Merge Intervals
- Non-overlapping Intervals

---

## 2.2. Graph

- Number of Islands
- Number of Connected Components
- Course Schedule

---

## 2.3. Two Pointers

- Container With Most Water
- 2Sum
- Valid Palindrome

---

## 2.4. Array & Hashing

- Contains Duplicate
- Valid Anagram
- Two Sum
- Top K Frequent Elements
- Longest Consecutive Sequence

---

## 2.5. Stacks

- Validate Parentheses

---

## 2.6. Sliding Window

- Best Time to Buy and Sell Stock

---

## 2.7. Linked List

- Reverse Linked List
- Merge 2 Sorted List
- Linked List Cycle
- Merge K Sorted List

---

## 2.8. Tree

- Invert a Binary Tree
- Depth of a Binary Tree
- Subtree of a Binary Tree
- LCA in BST
- Serialize and Deserialize Binary Tree

---

## 2.9. Heap

- Find Median in a Datastream

---

## 2.10. Dynamic Programming

- Coin Change
- House Robber
- Decode Ways

---

## 2.11. Construct Data Structures

- LRU Cache
- Circular Queue

---
