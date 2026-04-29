# 로컬 개발 환경 준비 상태(Breeze)

## 1. Breeze는 어떤 문제를 해결하는가?

- Breeze는 Airflow 개발 환경을 로컬에서 비슷하게 재현하기 위한 도구
- 해결하는 문제 : 환경 불일치, 복잡한 의존성, 테스트 실행 난이도, CI 실패 재현 문제

| 문제                                 | Breeze가 없다면                        | Breeze 사용 시                     |
| ------------------------------------ | -------------------------------------- | ---------------------------------- |
| Python/DB/Provider 의존성            | 로컬마다 다름                          | Docker 기반으로 표준화             |
| 테스트 실행                          | 어떤 명령을 써야 할지 복잡             | `breeze testing ...`으로 통일      |
| CI 실패 재현                         | GitHub Actions에서만 확인 가능         | 로컬에서 유사 환경 재현            |
| Airflow 구성요소 실행                | scheduler, API server, DB 등 직접 세팅 | Breeze가 개발용 환경 구성          |
| 특정 컴퓨터에서만 정상 동작하는 문제 | 자주 발생                              | contributor/CI 간 환경 차이 최소화 |

---

## 2. Breeze 실행 준비

> 우선 Breeze를 실행해보기 위해 포크 대신 클론을 수행
> 추후 포크 후 origin을 포크된 곳으로 바꾸면 됨
> 포크해야 PR 가능

---

### 🔹 진행 과정

1. git clone
   - `git clone https://github.com/apache/airflow.git`
   - 파일 용량 약 782.8MB
2. Breeze 설치 스크립트 실행

   ```sql
   cd airflow
   ./scripts/tools/setup_breeze
   # 'uv' is not on PATH. It is required to run breeze via uvx.'uv' is not on PATH. It is required to run breeze via uvx.
   ```

   - uv가 설치되어 있지 않으면 에러 발생([Breeze 설치 스크립트 실행 시 uv 미설치 에러](<./Breeze 설치 스크립트 실행 시 uv 미설치 에러.md>)

3. 실행
   - PATH에 shell이 해당 uv 경로를 찾을 수 있다면(`…/opt/hombrew/bin…`) 실행 가능

   ```bash
   cd airflow
   ./scripts/tools/setup_breeze
   # Please confirm Install breeze shim at /Users/sangunlee6/.local/bin/breeze. Are you sure? [y/N/q]
   # Breeze 실행용 shim을 저 위치에 설치할 것인지 물어봄
   # y
   ```

   - breeze 파일이 설치된 경로가 현재 터미널의 PATH에 없다면 shell이 breeze 명령어를 못찾음
   - 따라서 bash 터미널에 breeze 설치 경로 추가

   ```bash
   echo 'export PATH="/Users/sangunlee6/.local/bin:$PATH"' >> ~/.bash_profile
   source ~/.bash_profile
   ```

   - 이후 breeze 실행 : breeze 실행을 초기화하고 해당 명령어를 실행함

   ```bash
   airflow $ which breeze
   /Users/sangunlee6/.local/bin/breeze
   airflow $ breeze --help
   Regenerating provider dependencies file
   Installed 6 packages in 10ms
   Refreshed 101 providers with 2123 Python files.

   Written /Users/sangunlee6/Desktop/dev/airflow/generated/provider_dependencies.json
   ```

---

## 3. Breeze 실행

---

### 🔹 Breeze 진입

```bash
breeze
```

- 처음 실행하면 도커 이미지 다운로드 및 의존성 준비로 인해 시간이 소요
- 정상 진입하면 다음과 같은 프롬프트 생성

```bash
[Breeze:3.10.20] root@3ff3e19eb885:/opt/airflow$
```

- 이 상태가 되면 Breeze 컨테이너 안에 들어온 것

---

### 🔹 지금까지의 흐름

```
Airflow repo clone
→ uv 설치 확인
→ setup_breeze 실행
→ breeze shim 설치
→ PATH 설정
→ breeze 명령 실행
→ Breeze 개발환경 진입
```

- Airflow 코드를 내 Mac에서 contributor 개발 환경으로 실행할 준비까지 진행

---

### 🔹 전체 기여 흐름 중 현재 위치

```
1. GitHub Issue / 관심 영역 탐색
2. Fork / Clone
3. 개발환경 세팅  ← 지금 여기
4. 코드 구조 파악
5. 작은 수정
6. 테스트 실행
7. PR 생성
8. 리뷰 반영
9. Merge
```

---

### 🔹 Breeze 안에서 먼저 해볼 것

- Airflow 개발환경이 정상 구성됐는지 확인

  ```bash
  [Breeze:3.10.20] root@3ff3e19eb885:/opt/airflow$ pwd
  # /opt/airflow
  [Breeze:3.10.20] root@3ff3e19eb885:/opt/airflow$ python --version
  # Python 3.10.20
  [Breeze:3.10.20] root@3ff3e19eb885:/opt/airflow$ which python
  # /usr/python/bin/python
  [Breeze:3.10.20] root@3ff3e19eb885:/opt/airflow$ airflow version
  # 3.3.0(Airflow CLI 정상 실행 확인)
  [Breeze:3.10.20] root@3ff3e19eb885:/opt/airflow$ python -c "import airflow; print(airflow.__version__)"
  # 3.3.0(Python 코드에서 airflow 패키지를 import할 수 있는지 확인)
  ```

  - [Breeze:3.10.20] : Python 3.10.20 기반 Breeze 환경
  - root : 컨테이너 내부 사용자
  - 3ff3e19eb885 : 도커 컨테이너 ID
  - /opt/airflow : 컨테이너 안의 현재 디렉터리

---

## 4. breeze testing 확인

---

### 🔹 Airflow 테스트의 범위

- breeze testing --help(주요 테스트)

| 그룹                        | 의미                                    |
| --------------------------- | --------------------------------------- |
| `⭐️⭐️ core-tests`           | Airflow 핵심 기능 단위 테스트           |
| `⭐️ core-integration-tests` | Core 기능 통합 테스트                   |
| `⭐️ providers-tests`        | AWS, GCP, Slack 등 Provider 단위 테스트 |

---

### 🔹 Airflow core unit test 실행 방법 확인하기

```bash
breeze testing core-tests --help
```

| 봐야 할 것            | 의미                                           | 비고                                          |
| --------------------- | ---------------------------------------------- | --------------------------------------------- |
| `core-tests`          | Airflow Core 단위 테스트 실행                  | backfill/API/backend 쪽 기여와 가장 관련 있음 |
| `--test-type`         | 어떤 종류의 core test를 돌릴지 선택            | 전체 테스트 대신 범위를 줄이기 위함           |
| `--backend`           | DB backend 선택: `sqlite`, `postgres`, `mysql` | DB 관련 기능이면 중요                         |
| `--python`            | 테스트할 Python 버전 선택                      | Breeze 환경 버전 명시                         |
| `[EXTRA_PYTEST_ARGS]` | 특정 테스트 파일/함수만 실행                   | 초보 기여자가 가장 많이 쓰게 됨               |

1. 특정 core 테스트 파일만 실행 : 전체 core test가 아니라 수정한 코드와 관련된 테스트만 돌리는 것

```
breeze testing core-tests tests/<경로>/<테스트파일>.py
```

2. 특정 테스트 함수만 실행

```
breeze testing core-tests tests/<경로>/<테스트파일>.py::test_name
```

- 디버깅할 때 유용

3. DB가 필요한 core test 실행

- 기본 백엔드는 `sqlite`
- DB 동작이 중요한 기능이라면 `postgres`로 옵션을 줄 수 있음

```
breeze testing core-tests--backend postgres tests/<경로>/<테스트파일>.py
```

4. Python 버전 명시

```
breeze testing core-tests--python3.10 tests/<경로>/<테스트파일>.py
```

---

### 🔹 Airflow Backfill 테스트 찾아보기

```bash
airflow $ find . -path "*test*" -type f | grep -i backfill
./airflow-core/tests/unit/api_fastapi/core_api/routes/public/test_backfills.py
# Backfill API 동작
./airflow-core/tests/unit/models/test_backfill.py
# Backfill 모델/상태 관리
./airflow-core/tests/unit/cli/commands/test_backfill_command.py
# CLI에서 backfill 실행
```

- 테스트 목록 확인하기
  ```bash
  airflow $ grep -n "def test_" ./airflow-core/tests/unit/api_fastapi/core_api/routes/public/test_backfills.py
  #121:    def test_list_backfill(self, test_client, session):
  #155:    def test_get_backfill(self, session, test_client):
  #179:    def test_get_backfill_with_null_conf(self, session, test_client):
  #...
  ```
- breeze에서 collect only로 테스트가 잘 수집되는지 확인
  ```bash
  airfow $ breeze testing core-tests --collect-only ./airflow-core/tests/unit/api_fastapi/core_api/routes/public/test_backfills.py
  # 39개 테스트를 발견(테스트 파일을 Breeze, pytest가 정상 인식하는지 확인)
  # Airflow backend: Sqlite(DB는 Sqlite)
  ```
- 테스트 시도해보기
  ```bash
  airflow $ breeze testing core-tests ./airflow-core/tests/unit/api_fastapi/core_api/routes/public/test_backfills.py
  ```
- 결과 요약 : Backfill Public API 단위 테스트 39개를 Breeze 컨테이너 안에서 실행, 약 1분 소요

---

### 🔹 테스트 실행 흐름

1. CI Docker 이미지 재빌드 필요 여부 확인
2. Docker / docker-compose / uv / Python 버전 확인
3. 기존 테스트용 Docker volume 제거
4. 테스트용 network / volume 새로 생성
5. 현재 Airflow 소스를 기준으로 uv로 Airflow 설치
6. SQLite backend로 Airflow 테스트 환경 초기화
7. pytest로 test_backfills.py 실행
8. 39개 테스트 통과
9. 결과 XML / warning 파일 생성

---

### 🔹 breeze down

- 테스트 작업이 끝나면 Breeze 관련 컨테이너, 리소스를 내리기
