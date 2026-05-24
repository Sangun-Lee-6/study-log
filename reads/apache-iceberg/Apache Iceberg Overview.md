# Apache Iceberg Overview

## 📌 Iceberg란 무엇인가?

> S3 같은 오브젝트 스토어에 흩어져 있는 데이터 파일들을 안전한 테이블처럼 다루게 해주는 테이블 포맷

DB에서는 `SELECT * FROM orders`만 하면 관련 정보가 나오지만, 데이터 레이크에서는 `orders` 테이블을 구성하는 파일이 정확히 무엇인지 관리가 필요함 → Iceberg는 이 문제를 메타데이터로 해결

>

### 🔹 전통적인 DB, DW

- DB/DW는 물리 데이터 저장 방식을 숨기고 사용자에게 테이블이라는 추상화를 제공
  - → 물리 데이터 저장 방식 : 파일⋅페이지⋅블록⋅인덱스 같은 구조로 저장
- 따라서 사용자는 테이블만 바라보고 작업할 수 있음
  - ex. `SELECT * FROM orders;`

### 🔹 Data Lakehouse

- Data Lake : 데이터를 파일 형태로 한곳에 모아 저장하는 대용량 데이터 저장소
- Data Lakhouse : 데이터 레이크(ex. S3)에 저장된 파일 데이터를 DW처럼 관리 가능하게 만드는 아키텍처
- Data Lakehouse에서 데이터는 DB가 아니라 S3 같은 오브젝트 스토어에 파일로 저장
  - 그리고 Spark, Flink 같은 엔진이 그 파일을 읽음
  ```
  Object Storage(ex. S3)
        ↑
  Spark / Flink
        ↑
       SQL
  ```
- 장점
  - 데이터를 한 곳에 저장 가능
  - Spark, ML 작업 등 여러 엔진이 같은 데이터를 읽을 수 있음
  - DW보다 유연하고 확장성이 좋음
  - Parquet 같은 컬럼 기반 파일 포맷을 활용 가능
- 한계
  - 파일 저장소에는 테이블 개념이 없음
  - 아래 질문에 답하기 어려움
  ```
  - 어떤 파일이 최신 데이터인지?
  - orders 관련 데이터는 어떤 파일로 구성된거지?
  - 동시에 두 작업이 데이터를 쓰면 어떻게 안전하게 처리하지?
  - 어제 시점의 테이블로 돌아가려면 어떻게 하지?
  ...
  ```

### 🔹 테이블 포맷

- 테이블 포맷은 데이터 레이크에 DW의 테이블 관리 능력을 가져오는 기술
  - 데이터 레이크하우스를 구현하기 위해 필요한 기술 중 하나
  ```
  사용자
    ↓
  SQL: SELECT * FROM orders
    ↓
  Spark
    ↓
  Iceberg 메타데이터
    ↓
  Parquet
    ↓
  S3
  ```
- 테이블 포맷의 역할
  - 메타데이터에 파일 목록 기록
    - → 어떤 파일이 해당 테이블에 해당되는지 알 수 있음
  - snapshot 관리
    - → 최신 테이블 상태 조회 가능
  - time travel 지원
    - → 과거 시점 데이터 조회 가능
  - ACID 지원
    - → 동시 쓰기 작업 지원
  - 피알 통계, 파티션 정보로 불필요한 파일 스킵
    - → 쿼리 속도 향상

---

### 🔹 Apache Hive의 한계

- 테이블 포맷 중 과거에는 Apache Hive가 많이 사용됨
- Hive는 디렉터리 기반 방식
  - 이 디렉터리에 있는 파일들이 테이블로 생각되는 방식 → 안정적인 관리가 어려움
  - Hive 메타데이터 : `orders` 테이블의 `date=2026-05-22` 파티션은 이 디렉터리에 있는 파일
  ```
  s3://data/orders/date=2026-05-22/file1.parquet
  s3://data/orders/date=2026-05-22/file2.parquet
  ```
- Hive 방식의 장점
  - 단순함
  - 이해하기 쉬움
  - 오래 사용되어 생태계 넓음
- Hive 방식의 한계
  - 작은 변경이 비효율적
    - → 일부 row만 바꾸고 싶어도 파일/파티션 단위 작업
  - 여러 파티션 변경이 위험
    - → 여러 디렉터리를 동시에 안전하게 커밋하게 어려움
  - 테이블 상태 관리가 약함
    - → 특정 시점의 정확한 파일 목록 관리가 어려움
  - 사용자가 디렉터리, 파티션 구조를 알아야함
  - 쿼리 계획이 느려질 수 있음
    - → 디렉터리, 파티션 탐색 비용

### 🔹 Apache Iceberg의 개선

- Iceberg는 파일 기반 메타데이터 관리 방식을 사용(파일 목록과 상태를 명시적으로 관리)
  - Hive : 이 디렉터리 안에 있는 파일들이 테이블이다.
  - Iceberg : 이 테이블에 속하는 파일은 a,b,c 이고, 이 파일은 snapshot A이고, 각 파일 컬럼의 min/max 값은 이렇다.
- 따라서 엔진은 S3 전체를 찾는게 아니라 Iceberg 메타데이터를 보고 정확히 필요한 파일만 읽음

---

## 📌 Iceberg 아키텍처

### 🔹 Iceberg 테이블의 5단계 구조

- Iceberg 아키텍처 : 테이블의 현재 버전이 어떤 데이터 파일들로 구성되는지를 여러 단계의 메타데이터로 추적하는 구조
- 3개의 Layer로 구성됨 : Catalog Layer(Catalog) - Metadata Layer(Metadata File, Manifest List, Manifest File) - Data Layer(Data File, Delete File)

1. Catalog : 현재 메타데이터 파일 위치 관리
2. Metadata File : 테이블 버전, 스키마, 스냅샷 관리
3. Manifest LIst : 특정 스냅샷의 manifest 목록 관리(스냅샷 목차)
4. Manifest File : 실제 데이터 파일 목록과 통계 관리(데이터 파일 명세서)
5. Data, Delete File : 실제 데이터와 삭제 정보 저장(실제 데이터 파일)

### 🔹 Iceberg 아키텍처를 왜 이렇게 나누는가?

- Iceberg가 해결하고자 하는 문제 : orders 테이블의 현재 상태는 정확히 어떤 파일로 구성되어 있나?
  - 따라서 Iceberg는 파일 위에 메타데이터 계층을 생성
- 상위 계층은 하위 계층의 위치와 상태를 기록함
  - 따라서 쿼리 엔진은 S3 전체를 찾는게 아니라 아래 순서로 찾아감
  - Catalog → Metadata File → Manifest List → Manifest File → Data File

### 🔹 Data Layer

- Data Layer : 실제 데이터가 저장되는 계층
- Data Layer를 구성하는 2가지 파일
  1. Data File : 실제 row 데이터 저장
     - → 보통 Parquet, Avro 같은 포맷으로 저장
  2. Delete File : 삭제된 row 정보 저장
- Delete File이 필요한 이유
  - 오브젝트 스토어의 파일은 보통 DB처럼 row 하나만 바로 수정/삭제가 어려움
  - 그래서 삭제 대상을 기록해놓고, 읽을 때 삭제된 건 제외하고 읽음 ⇒ `merge-on-read` 방식
  ```
  최종 결과 = data file - delete file에 기록된 row
  ```

### 🔹 Manifest File

- Manifest File : 테이블에 속한 데이터 파일과 상태(포맷, 통계 등)를 저장한 파일
- 예시

  ```
  manifest-001.avro

  포함된 data files:
  - /data/orders_001.parquet
  - /data/orders_002.parquet

  파일 포맷:
  - Parquet

  통계:
  - order_date min = 2026-05-01
  - order_date max = 2026-05-22
  - amount min = 1000
  - amount max = 90000
  ```

- 통계 정보가 필요한 이유
  - 각 데이터 파일의 min/max 값이 있다면, 쿼리 엔진은 읽을 필요 없는 파일을 건너뛸 수 있음
  - ex. 쿼리에 `WHERE order_date = '2026-05-22';` 조건이 있다면, 특정 파일이 26년 1월 1일~26년 1월 31일 데이터 통계정보인 경우, 이 파일은 읽을 필요가 없음

---

### 🔹 Manifest List

- Manifest List : 특정 스냅샷이 어떤 manifest file로 구성되는지 저장한 파일
  - 스냅샷 : 특정 시점의 테이블 버전
  - 예시 : `snapshot s1`
  ```
  snapshot s1
    ↓
  manifest-list-s1.avro
    ↓
  manifest-001.avro
  manifest-002.avro
  manifest-003.avro
  ```
- Manifest File을 바로 Metadata File에 넣지 않은 이유
  - 테이블이 커지면 데이터 파일이 많아지면서, manifest file도 많아지므로, 중간에 Manifest List 계층을 둠

### 🔹 Metadata File

- Metadata FIle : Iceberg 테이블의 전체 상태 정보를 저장하는 파일
- Iceberg 테이블의 상태 정보
  - schema : 테이블 컬럼과 타입
  - partition spec : 파티션 방식
  - snapshots : 스냅샷 목록
  - current snapshot : 최신 snapshot
  - manifest list 위치 : 현재 스냅샷이 참조하는 manifest list 경로
  - table properties : 테이블 설정값
- 예시

  ```
  v3.metadata.json

  table: orders
  schema: order_id, user_id, amount, order_date
  current_snapshot: s2

  snapshots:
  - s0
  - s1
  - s2

  s2 manifest list:
  - /metadata/snap-s2.avro
  ```

- Metadata File이 중요한 이유
  - Iceberg는 테이블이 변경될 때마다 새로운 Metadat File을 생성함
  - 따라서 아래 기능이 가능
    - 최신 데이터 조회
    - Time travel : 과거 데이터 조회
    - 스키마 변경 이력 추적
    - Rollback : 이전 데이터로 되돌릴 수 있음

### 🔹 Catalog

- Catalog : 특정 Iceberg 테이블의 현재 Metadata File의 위치를 저장한 파일
- Catalog의 주요 책임
  - 단순히 위치만 알려주는게 아니라 현재 metadata pointer를 원자적으로 업데이트 해야함
  - 동시에 같은 메타데이터 파일을 쓰지 않도록

## 📌 Iceberg에서 읽기, 쓰기 작업

### 🔹 Iceberg에서 읽기 작업

- 예시 쿼리
  ```sql
  SELECT *
  FROM orders
  WHERE order_date = '2026-05-22';
  ```
- Iceberg를 사용하면 쿼리 엔진은 S3를 전체 스캔하지 않고 Iceberg 메타데이터에 따라 필요한 파일만 검색
  1. Catalog : orders의 최신 메타데이터 파일 위치 찾기
  2. Metadat file : 현재 스냅샷 확인, 현재 스냅샷의 Manifest List 위치 찾기
  3. Manifest List : Manifest File 목록 찾기
  4. Manifest File : 조건에 맞는 Data File 찾기
  5. 필요한 Data File만 읽어서 결과 반환

### 🔹 Iceberg에서 쓰기 작업

- 예시 쿼리
  ```sql
  INSERT INTO orders VALUES (...);
  ```
- Iceberg 동작 : 파일을 수정하는게 아니라 새로운 파일⋅메타데이터를 만들고 포인터를 바꿈
  1. 새 Data File 생성
  2. 새 Data File을 가리키는 Manifest File 생성
  3. 새 Manifest File을 포함하는 Manifest List 생성
  4. 새 snapshot을 포함하는 Metadata File 생성
  5. Catalog의 현재 metadata pointer를 새 Metadata File로 atomic update

---
