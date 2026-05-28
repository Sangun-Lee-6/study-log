# 주제

> 우선 CTE, Window Function 위주의 문제

# 템플릿

> 링크 :

## 1. Grain 확인

- 원본 테이블의 row 1개는 무엇인가?
- 최종 결과 row 1개는 무엇이어야 하는가?

## 2. 변환 방식 선택

- 필터링: WHERE
- 집계: GROUP BY + HAVING
- 연결: JOIN
- 순위/이전값/누적: WINDOW FUNCTION

## 3. 검증

- NULL, 중복, JOIN 후 row 증가, 집계 기준이 맞는지 확인
