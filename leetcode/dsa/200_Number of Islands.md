> 링크 : https://leetcode.com/problems/number-of-islands/

# 1. 입력, 출력 형식을 먼저 확인

- 입력 : 2차원 배열, 각 원소의 type은 string('1', '0')
- 출력 : 숫자

# 2. 가장 단순한 풀이 생각(ex. brute force), 시간복잡도

- 모든 배열에 대해 검사하는데, 1인 노드만 dfs를 수행
- dfs 수행할 때 count += 1, 방문시 '0'으로 만들어주면 됨

# 3. 문제 상황을 해결하는 자료구조 생각, Trade off 고려(시간복잡도 등)

- dfs는 시작 지점에서 연결된 노드를 끝까지 따라가며 탐색하므로, 이 문제에 적합

# 4. 먼저 정상 케이스를 구현하고, 빈 배열/중복/음수 같은 edge case를 확인

- [], [전부 물], [전부 땅]에도 잘 동작함

# 5. 풀이

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
