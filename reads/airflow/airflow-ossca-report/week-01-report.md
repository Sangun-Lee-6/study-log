# week-01-report

> 일시 : 2026년 4월 29일 수요일 19:30-22:00

## 목차

1. 참여 전 준비
2. 당일 활동 기록
3. 다음 계획

## 1. 참여 전 준비

- [Airflow 기여 프로세스 이해](<../reference/Airflow 기여 프로세스 이해.md>)
- [로컬 개발 환경 준비 상태(Breeze)](<../reference/로컬 개발 환경 준비 상태(Breeze).md>)

## 2. 당일 활동 기록

- Apache Airflow 오픈소스 생태계에 진입하고, 실제 기여 흐름을 경험했다.
  - Airflow 저장소 구조와 기여 프로세스를 살펴보며, 이슈 확인부터 PR 생성, 리뷰 대응, CI 확인, merge까지의 흐름을 파악했다.
  - Airflow 기여를 위한 로컬 개발 환경을 준비했다. 특히 Airflow 전용 개발 도구인 Breeze를 사용해 테스트와 개발 작업을 수행할 수 있는 기반을 세팅했다.
  - i18n 관련 기여 방향과 정책 문서를 확인하며, Airflow 프로젝트에서 문구와 표기 일관성을 다루는 방식을 이해했다.

- 첫 upstream 기여를 완료했다.
  - Airflow 오픈소스 기여를 시작했고, 작성한 PR이 리뷰와 CI를 거쳐 upstream main 브랜치에 merge되었다.
  - 단순히 코드를 수정하는 것뿐만 아니라, 커뮤니티 리뷰를 받고 피드백에 대응하며 Apache Airflow의 협업 방식을 직접 경험했다.

- main 브랜치에 반영된 커밋
  - [CLI: Fix Backfill help text capitalization (#66093)](https://github.com/apache/airflow/commit/14fda0279ac3d626d4fed07aeba9cd787e81fc37)
    - Backfill CLI 도움말 문구의 대소문자 표기를 정리한 기여
    - 작성자: [Sangun-Lee-6](https://github.com/apache/airflow/commits?author=Sangun-Lee-6)
    - 반영일: 2026년 4월 30일
  - [UI: Align Dag capitalization in e2e tests (#66081)](https://github.com/apache/airflow/commit/f86fdd7e8384c755bef813490a96f86e85bd1e0e)
    - e2e 테스트에서 Dag 표기 방식의 일관성을 맞춘 기여
    - 작성자: [Sangun-Lee-6](https://github.com/apache/airflow/commits?author=Sangun-Lee-6)
    - 반영일: 2026년 4월 30일

## 3. 다음 계획

- AIP-78과 Backfill 도메인을 이해한다.
  - Airflow에서 Backfill이 어떤 문제를 해결하는 기능인지 정리한다.
  - AIP-78 문서를 읽고, Airflow 3.x에서 Backfill 기능이 어떤 방향으로 개선되고 있는지 파악한다.
  - CLI, API, UI에서 Backfill이 어떻게 연결되는지 확인하고, 관련 코드 흐름을 추적한다.

- 작은 단위의 테스트 기여를 이어간다.
  - 기능 변경보다 영향 범위가 작은 테스트, 문구, 표기 일관성 개선부터 기여한다.
  - 기존 테스트 구조를 읽고, e2e 테스트와 CLI 테스트에서 추가로 개선할 수 있는 지점을 찾는다.
  - PR을 작게 나누어 리뷰 부담을 줄이고, CI 결과와 리뷰 피드백에 빠르게 대응한다.
