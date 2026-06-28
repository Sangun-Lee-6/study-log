## 📌 AWS Glue 소개

### 🔹 Glue

- 서버리스 데이터 통합 서비스
- 완전 관리형 ETL 서비스
- 데이터 카탈로그, 크롤러, 트리거 등 여러 기능 제공
- Spark 기반 분산 처리 환경 제공 → 대규모 데이터 처리 가능
- 다른 AWS 서비스와의 통합(Athena, S3, Redshift 등)
- 과금 : 기본적으로 사용량에 따라 과금

### 🔹 Glue Architecture

- AWS Glue : 데이터를 찾아서 Catalog에 등록하고, ETL Job으로 변환한 뒤, Target에 적재하는 서버리스 데이어 파이프라인 서비스
  ![image](./img/image14.png)
- 컴포넌트별 역할
  - Data Source : 원본 데이터(S3, RDS, Redshift 등)
  - Crawler : 데이터 탐색기
  - Data Catalog : 데이터 주소록
    - 어디에 어떤 데이터가 있고, 스키마가 무엇인지 저장하는 메타데이터 저장소
  - Job : ETL 실행 단위
    - 데이터 읽기, 변환, 저장 작업
  - Script : 변환 코드 : Python 등으로 작성된 ETL 로직
  - Data Target : 결과 저장소
- 사용 사례 : 광고 로그 데이터 파이프라인
  ![image](./img/image15.png)

  ```
  S3 Bronze 광고 로그
    ↓
  Glue Crawler
    ↓
  Glue Data Catalog 테이블 생성
    ↓
  Glue Job 실행
    ↓
  TSV → Parquet 변환, 컬럼 정리, 날짜 파티셔닝
    ↓
  S3 Silver / Mart 적재
    ↓
  Athena 또는 BI에서 조회
  ```

  - Data Source : `s3://bucket/bronze/ad_clicks/`에 원본 `.tsv` 저장
  - Crawler : `.tsv` 파일을 읽고 `campaign_id`, `click_time` 같은 컬럼 구조 파악
  - Data Catalog : `ad_clicks_raw` 테이블의 메타데이터 저장
  - Job : 원본 데이터를 읽어서 변환 로직 실행
  - Script : 타입 변환, null 처리, Parquet 변환 등
  - Data Target : `s3://bucket/silver/ad_clicks_dt=2026-05-01/`에 저장

## 📌 Data Catalog

### 🔹 Data Catalog

- 메타데이터 저장소
- 크롤러를 이용한 데이터 자동 검색 가능
- Database, Stream Schema 등 제공
- Apache Iceberg를 이용한 테이블 최적화
- 다른 서비스와의 통합

### 🔹 Crawler

- 데이터 카탈로그를 자동으로 생성하기 위해 사용하는 기능
- 데이터 소스를 스캔하여 자동으로 스키마 추론
- S3, RDS, Redshift 등 여러 데이터 소스를 스캔 가능
- 테이블과 파티션 분류
- 온디멘드(한 번 실행) 또는 주기를 설정하여 실행
