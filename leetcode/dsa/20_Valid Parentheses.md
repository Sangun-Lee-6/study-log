> 링크 : https://leetcode.com/problems/valid-parentheses/description/

## 1. 입력, 출력 형식을 먼저 확인

- input : string, 1개
- output : boolean
- s.length <= 10^4
- s는 (){}[]로만 구성

## 2. 가장 단순한 풀이 생각(ex. brute force), 시간복잡도

## 3. 문제 상황을 해결하는 자료구조 생각, Trade off 고려(시간복잡도 등)

- 열린 괄호가 있다면, 열린 괄호가 오거나, 해당하는 닫힌 괄호가 와야하므로 stack

```python
class Solution:
    def isValid(self, s: str) -> bool:
        result = True
        stack=[]

        for b in s:
            if self.isOpenBracket(b):
                stack.append(b)
            else: # 닫힌괄호
                if not stack: # stack이 비어있다면
                    return False
                openB = stack.pop()
                if self.isPair(openB, b):
                    continue
                else:
                    return False

        if stack:
            result=False

        return result



    def isOpenBracket(self, bracket):
        if bracket == '(' or bracket == '[' or bracket == '{':
            return True
        else:
            return False

    def isPair(self, openB, closeB):
        if openB == '(' and closeB ==')':
            return True

        if openB == '[' and closeB ==']':
            return True

        if openB == '{' and closeB =='}':
            return True

        return False

```

## 4. 먼저 정상 케이스를 구현하고, 빈 배열/중복/음수 같은 edge case를 확인

- s.length가 1이상이므로 빈배열은 없음
- s의 원소는 괄호로만 구성
- 열린 괄호만 오거나 닫힌 괄호만 들어올 때 예외 처리
