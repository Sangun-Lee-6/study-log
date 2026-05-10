# week-02-report

> 일시 : 2026년 5월 6일 수요일 19:30-22:00

## 목차

1. 참여 전 준비
2. 당일 활동 기록
3. 다음 계획

## 1. 참여 전 준비

- [기여 방향 정하기](<../reference/기여 방향 정하기.md>)
- [Airflow Backfill Dry Run의 dag_run_conf 검증 누락 흐름 분석](<../reference/Airflow Backfill Dry Run의 dag_run_conf 검증 누락 흐름 분석.md>)
- PR 제출
  - `AIP-78: Fix airflowctl backfill management API methods` (#66408)
    - 라벨: `area:airflow-ctl`, `backport-to-airflow-ctl/v0-1-test`, `ready for maintainer review`
    - 상태: 리뷰 필요, 체크리스트 1개 완료
  - `Validate dag run conf in backfill dry-run`
    - 라벨: `area:API`, `ready for maintainer review`
    - 상태: triage 이후 설정됨

## 2. 당일 활동 기록

- `CODEOWNERS`를 기준으로 Airflow의 영역별 담당 구조와 기여 가능 범위를 파악했다.
- 데이터 신뢰성을 보강하는 방향을 핵심 기여 주제로 정리했다.
- 운영진이 "코드에 기여할 때 AI가 다 작성한 코드는 티가 난다"고 했다.

## 3. 다음 계획

- 파이썬 디자인 패턴을 학습해서
  1. 기여할 코드를 AI에게 코드 작성을 다 맡기지 않기
  2. 기여할 코드의 가독성, 유지보수성을 높이기
