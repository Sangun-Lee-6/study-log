> 링크 : https://leetcode.com/problems/number-of-islands/

# 1. 입력, 출력 형식을 먼저 확인

- 입력 : 2차원 배열, 각 원소의 type은 string('1', '0')
- 출력 : 숫자

# 2. 가장 단순한 풀이 생각(ex. brute force), 시간복잡도

- 모든 배열에 대해 검사하는데, 1인 노드만 dfs/bfs를 수행
- dfs/bfs 수행할 때 count += 1, 방문시 '0'으로 만들어주면 됨

# 3. 문제 상황을 해결하는 자료구조 생각, Trade off 고려(시간복잡도 등)

- dfs는 시작 지점에서 연결된 노드를 끝까지 따라가며 탐색하므로, 이 문제에 적합
- bfs는 시작 지점에서 연결된 노드를 방문 예약하며 탐색하므로, 이 문제에 적합

# 4. 먼저 정상 케이스를 구현하고, 빈 배열/중복/음수 같은 edge case를 확인

- [], [전부 물], [전부 땅]에도 잘 동작함

# 5. dfs 풀이

```python
class Solution:
    def numIslands(self, grid: List[List[str]]) -> int:

        numOfIslands = 0


        # 2차원 배열의 모든 원소에 대해
        for r, row in enumerate(grid):
            for c, node in enumerate(row):
                if node != "1":
                    continue

                numOfIslands += 1
                self.dfs_grid(grid, r,c)
        return numOfIslands



    def dfs_grid(self, grid, r, c):
            rows = len(grid)
            cols = len(grid[0])

            if r<0 or r >= rows or c<0 or c>=cols:
                return

            if grid[r][c]!='1':
                return

            grid[r][c] = '0'

            self.dfs_grid(grid,r+1, c)
            self.dfs_grid(grid,r-1, c)
            self.dfs_grid(grid,r, c+1)
            self.dfs_grid(grid,r, c-1)

```

# 6. bfs 풀이

```python

class Solution:
    def numIslands(self, grid):

        cnt = 0
        rows = len(grid)
        cols = len(grid[0])

        for r, rows in enumerate(grid):
            for c, cols in enumerate(rows):
                if grid[r][c]=='1':
                    cnt += 1
                    self.bfs(grid,r,c)

        return cnt


    def bfs(self, grid, r, c):
        rows = len(grid)
        cols = len(grid[0])

        queue = deque()
        queue.append((r,c))

        grid[r][c]='0' # 방문 처리

        directions = [
            (1,0), # 하
            (-1,0),# 상
            (0,1), # 우
            (0,-1) # 좌
        ]

        while queue:
            r,c = queue.popleft()

            for dr,dc in directions:
                nr = r + dr
                nc = c + dc

                if nr<0 or nr>=rows or nc<0 or nc>=cols:
                    continue

                if grid[nr][nc] == '0':
                    continue

                grid[nr][nc] = '0'
                queue.append((nr,nc))

```

# 7. dfs와 bfs의 차이

|             | DFS            | BFS            |
| ----------- | -------------- | -------------- |
| 자료구조    | Stack          | Queue(deque)   |
| 반복 관리   | Call Stack     | `while queue`  |
| 다음칸 처리 | 바로 함수 호출 | 큐에 방문 예약 |
