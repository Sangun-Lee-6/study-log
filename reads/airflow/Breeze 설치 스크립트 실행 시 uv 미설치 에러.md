## Breeze 설치 스크립트 실행 시 uv 미설치 에러

---

### 🔹 Breeze 설치 스크립트

- `./scripts/tools/setup_breeze`는 왜 하는가?
  - `/scripts/tools/` : Airflow 저장소 안의 도구 스크립트 폴더
  - `setup_breeze` : Breeze 실행 명령을 설치해주는 스크립트
- 이 스크립트는 `~/.local/bin/breeze`에 작은 실행 파일(즉, shim)을 설치
  - shim은 `dev/breeze` 폴더 기준으로 uvx를 통해 Breeze를 실행
  - shim : 사용자가 입력한 명령어를 실제 실행 대상에 연결해주는 실행 파일
  - shim : SW 용어, 연결/보정 역할을 하는 중간 계층, 단어 의미는 얇은 중간 끼움판

```
setup_breeze = 내 터미널에서 breeze 명령어를 쓸 수 있게 연결해주는 설치 작업
```

---

### 🔹 Breeze 설치 스크립트 실행 시 에러 발생

```sql
cd airflow
./scripts/tools/setup_breeze
# 'uv' is not on PATH. It is required to run breeze via uvx.'uv' is not on PATH. It is required to run breeze via uvx.
```

- 에러 : uv가 설치되어 있지 않거나, 설치됐지만 PATH에 잡혀 있지 않아서 Breeze shim을 만들 수 없음
- `# 'uv' is not on PATH. It is required to run breeze via uvx.`
  - Breeze는 uvx로 실행되는데, 현재 터미널에서 uv 명령어를 찾을 수 없음
  - 따라서 setup_breeze를 할 수 없음
- Breeze 실행 도구를 로컬의 전역 파이썬 환경과 분리해서 실행하기 위해 uvx 사용
  - Airflow는 PR 올릴 때 기여자마다 개발환경이 달라지는 문제를 줄이기 위해 Breeze 사용, uvx는 Breeze를 로컬 파이썬과 분리하기 위해 사용
  - uv : Python 패키지/환경 관리 도구 프로그램
  - uvx : 특정 파이썬 CLI 도구를 격리된 환경에서 실행하는 uv의 실행 명령

---

### 🔹 uv 미설치 에러 해결

1. uv가 있는지 확인

```bash
which uv
uv --version
```

- 둘 다 없으므로 현재 uv가 없는 상태

1. uv 설치

   ```bash
   brew install uv
   ```

2. 설치 확인

   ```bash
   airflow $ which uv
   /opt/homebrew/bin/uv
   airflow $ uv --version
   uv 0.11.8 (Homebrew 2026-04-27 aarch64-apple-darwin)
   ```
