# DFS

> DFS : 한 방향으로 끝까지 갔다가, 더 갈 곳이 없으면 되돌아오는 그래프 탐색 알고리즘

---

## 1. DFS 기본 개념

- Depth First Search(깊이 우선 탐색)
- 동작 방식
  1. 그래프에서 한 노드를 방문(시작점)
  2. 이웃 노드 중 하나로 이동
  3. 더 깊이 이동
  4. 더 이상 갈 곳이 없으면 이전 노드로 되돌아감
- 구현 방식과 이웃 노드의 순서에 따라 탐색 경로는 달라질 수 있음
  - 핵심은 연결된 노드를 빠짐없이 방문한다는 것

---

## 2. DFS가 DE에게 중요한 이유

- 데이터 엔지니어링에는 그래프 구조가 자주 등장함
  | DE 상황           | 그래프 관점                          |
  | ----------------- | ------------------------------------ |
  | Airflow DAG       | Task가 node, dependency가 edge       |
  | 데이터 lineage    | 테이블/컬럼이 node, 의존 관계가 edge |
  | ETL 파이프라인    | Job이 node, 실행 순서가 edge         |
  | 테이블 dependency | upstream/downstream 관계             |
  | 순환 참조 탐지    | cycle detection 문제                 |
  | 영향도 분석       | 특정 테이블 변경 시 downstream 탐색  |
- 예를 들어 Airflow Dag는 그래프 구조, DFS를 이해하면 다음과 같은 상황을 이해하는데 도움됨
  - 이 task가 실행되기 전에 어떤 upstream task가 필요한가?
  - 이 테이블이 바뀌면 어떤 downstream table이 영향받는가?
  - 이 dependency에 cycle이 생기면 왜 Dag가 깨지는가?

---

## 3. DFS에서 반드시 알아야 할 요소

- DFS 구현에는 보통 3개가 필요함
  1. `graph` : 노드 간 연결 관계
  2. `visited` : 이미 방문한 노드 기록
  3. `stack` OR 재귀 호출 : 다음에 방문할 노드 관리
- `visited`가 없으면 무한 반복이 발생할 수 있음(순환 그래프의 경우)

---

## 4. 반복문 기반 DFS

- 알고리즘 테스트에선 반복문, stack 방식을 주로 사용하는게 안전함

```python
def dfs(graph, start): # graph : 그래프 전체, start : 탐색을 시작할 노드
    visited = set() # node가 visited에 있는지 확인하는 연산이 빠르므로
    stack = [start] # 탐색할 노드를 담는 스택

    while stack: # 탐색할 노드가 남아있는 동안 반복
        node = stack.pop()

        if node in visited: # 이미 방문한 노드는 건너뜀
            continue

        visited.add(node) # 방문하지 않은 노드는 visited에 추가

        for neighbor in graph.get(node, []): # 현재 노드와 연결된 이웃 노드들에 대해
            if neighbor not in visited: # 이웃 노드가 방문하지 않았다면 stack에 추가
            stack.append(neighbor)

    return visited # 방문한 모든 노드 반환
```

---

## 5. 예제로 흐름 보기

```python
graph = {
    "A": ["B", "C"],
    "B": ["D"],
    "C": ["E"],
    "D": [],
    "E": []
}
```

| 단계 | pop한 node | visited         | stack 변화 |
| ---- | ---------- | --------------- | ---------- |
| 1    | A          | {A}             | B, C 추가  |
| 2    | C          | {A, C}          | E 추가     |
| 3    | E          | {A, C, E}       | 추가 없음  |
| 4    | B          | {A, C, E, B}    | D 추가     |
| 5    | D          | {A, C, E, B, D} | 추가 없음  |

- A의 이웃을 B, C 순서로 넣었지만 → 스택은 LIFO라서 C가 먼저 처리됨

## 6. 왜 DFS인가?

- DFS는 한 방향으로 갈 수 있을 만큼 깊게 들어간 뒤, 더 이상 갈 곳이 없으면 이전 후보로 돌아오는 방식
- 이 역할을 stack이 수행
- 순회하면서, neighbors가 있다면, 이웃 노드들을 stack에 추가함

## 7. 방문 순서까지 보장하고 싶다면

- visited를 set으로 반환함
- 따라서 방문 순서가 보장되지 않음
- 방문 순서까지 알고 싶다면, 아래와 같이 order 배열 추가
  - `visited.add(node)`할 때 `order`에 `append`해주고 `return order`
  ```python
  def dfs(graph, start):
      visited = set()
      order = []
      stack = [start]

      while stack:
          node = stack.pop()

          if node in visited:
              continue

          visited.add(node)
          order.append(node)

          for neighbor in graph.get(node, []):
              if neighbor not in visited:
                  stack.append(neighbor)

      return order
  ```

## 8. 시간복잡도

- O(V+E), V는 노드 수, E는 간선 수
  - 모든 노드를 최대 한 번 방문하고, 모든 간선을 최대 한 번 확인하므로
- 공간복잡도 : O(V)
  - visited에 최대 모든 노드가 들어가므로

## 9. 2차원 격자 DFS 템플릿

- 보통 알고리즘 문제에서는 그래프가 딕셔너리로 주어지지 않고 2차원 배열로 나오는 경우가 많음

```python
def dfs_grid(grid, r, c):
    rows = len(grid)
    cols = len(grid[0])

		# 범위를 벗어나면 종료
    if r < 0 or r >= rows or c < 0 or c >= cols:
        return

		# 방문 대상이 아니면 종료
    if grid[r][c] != "1":
        return

		# 방문 처리
    grid[r][c] = "0"

		# 상하좌우 탐색
    dfs_grid(grid, r + 1, c)
    dfs_grid(grid, r - 1, c)
    dfs_grid(grid, r, c + 1)
    dfs_grid(grid, r, c - 1)
```
