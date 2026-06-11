# 5. Docker Compose

## 1. Docker Copmose를 사용하는 이유

### 🔹 Docker Compose란

- 여러 개의 도커 컨테이너들을 하나의 서비스로 정의하고 구성해서 관리할 수 있게 도와주는 도구

### 🔹 Docker Compose를 사용하는 이유

1. 여러 컨테이너를 관리하기 쉬움
   - 여러 컨테이너로 이루어진 복잡한 애플리케이션을 한 번에 관리 가능
   - 여러 컨테이너를 하나의 환경에서 실행하고 관리 가능
2. 컨테이너 실행에 필요한 복잡한 명령어를 간소화 가능
   - ex. MySQL 이미지를 컨테이너로 실행할 때 아래와 같은 명령어를 실행함
     - `docker run -e MYSQL_ROOT_PASSWORD=password123 -p 3306:3306 -v [경로]`
   - 도커 컴포즈를 사용하면 컨테이너를 실행할 때 매번 복잡한 명령어를 입력할 필요 없이, `docker compose up`만 실행하면 됨

## 2. 실습 : Docker Compose로 Nginx 설치 및 실행

### 🔹 Docker CLI로 컨테이너를 실행할 때

- 터미널에 아래 명령어를 입력해서 Nginx 컨테이너를 실행
  ```bash
  docker run --name webserver -d -p 80:80 nginx
  ```

### 🔹Docker Compose로 컨테이너를 실행할 때

1. compose.yml 파일 작성
   - yml 파일은 들여쓰기로 문법을 파악하므로, 들여쓰기를 잘 맞춰서 작성해야 함

   ```yaml
   services:
     my-web-server:
       container_name: my-webserver
       image: nginx
       ports:
         - 80:80
   ```

   - `services: my-web-server`
     - Docker Compose에서 하나의 컨테이너를 서비스라고 부름
     - 이 옵션은 서비스에 이름을 붙이는 것
   - `container_name: my-webserver`
     - 컨테이너를 띄울 때 붙이는 별칭
   - `image: nginx`
     - 컨테이너를 실행할 떄 어떤 이미지를 사용할지
   - `ports`
     - 포트 매핑

2. compose 파일 실행

   ```bash
   docker compose up -d
   ```

3. compose 실행 현황 확인

   ```bash
   docker compose ps # docker compose 안에 정의된 컨테이너의 상태 조회
   ```

4. compose로 실행된 컨테이너 삭제

   ```bash
   docker compose down
   ```

### 🔹 NAME과 SERVICE의 차이

- `SERVICE`는 Docker compose에 정의된 서비스 이름이고, `NAME`은 실제 Docker가 실행한 컨테이너 이름
- 하나의 `SERVICE` 안에 여러 컨테이너가 포함될 수 있음
- docker compose 명령은 SERVICE 기준으로 조작
  ```bash
  docker compose logs [서비스명]
  docker compose restart [서비스명]
  docker compose exec [서비스명] bash
  ```
- docker 명령은 컨테이너 NAME 기준으로 조작
  ```bash
  docker logs [컨테이너명]
  ```

## 3. Docker Compose CLI 명령어

> 아래 명령어들은 `compose.yml`이 위치한 디렉터리에서 실행해야 함

### 🔹 `docker-compose` 명령어

- `docker-compose`로 시작하는 명령어는 업데이트를 지원하지 않는 v1 명령어이므로 사용 X
- `docker compose`로 시작하는 명령어를 사용

### 🔹 compose 파일 작성

- compose.yml
  ```bash
  services:
  	websever:
  		container_name: webserver
  		image: nginx
  		ports:
  			- 80:80
  ```
- compose.yml에서 정의한 컨테이너 실행
  ```bash
  docker compose up # 포그라운드 실행
  docker compose up -d # 백그라운드 실행
  ```

### 🔹 Docker Compose로 실행한 컨테이너 확인하기

```bash
# compose.yml에 정의된 컨테이너 중 실행 중인 컨테이너만 조회
docker compose ps

# compose.yml에 정의된 모든 컨테이너를 보여줌
docker compose ps -a
```

### 🔹 Docker Compose 로그 확인하기

```bash
# compose.yml에 정의된 모든 컨테이너 로그를 모아서 출력
docker compose logs
```

### 🔹 컨테이너 실행 전 이미이 재빌드

```bash
docker compose up --build # 포그라운드 실행
docker compose up --build -d # 백그라운드 실행
```

- `compose.yml`에서 정의한 이미지 파일에서 코드가 변경되면, 이미지를 다시 빌드해서 컨테이너를 실행해야 코드 변경 부분이 적용됨
- `docker compose up`
  - 이미지가 없을 때만 빌드해서 컨테이너를 실행
  - 이미지가 있다면 이미지를 빌드하지 않고 컨테이너 실행
- `docker compose up --build`
  - 이미지 존재 유무와 상관없이 무조건 빌드를 하고 컨테이너를 실행

### 🔹 이미지 다운받기, 업데이트하기

```bash
docker compose pull
```

- `compose.yml`에서 정의된 이미지를 다운받거나 업데이트
  - 로컬 환경에 이미지가 없다면 이미지 다운
  - 로컬 환경에 이미지가 있는데, 도커 허브의 이미지와 다른 이미지일 경우 이미지 업데이트

### 🔹 Docker Compose에서 이용한 컨테이너 종료하기

```bash
docker compose down
```

## 4. 실습 : Docker compose로 Redis 실행

### 🔹 Docker CLI로 컨테이너 실행하기

```bash
docker run -d -p 6379:6379 redis
```

### 🔹 Docker Compose로 컨테이너 실행

1. compose.yml 파일 작성

   ```yaml
   services:
     my-cache-server:
       image: redis
       ports:
         - 6379:6379
   ```

2. compose 파일 실행

   ```bash
   docker compose up -d
   ```

3. compose 실행 현황 조회

   ```bash
   docker compose ps
   ```

4. 컨테이너 로그 확인

   ```bash
   docker logs [컨테이너ID]
   ```

5. 컨테이너 접속

   ```bash
   docker exec -it [컨테이너ID] bash
   ```

## 5. 실습 : FastAPI, MySQL 컨테이너 동시에 띄워보기

### 🔹 FastAPI 프로젝트 세팅

1. 프로젝트 디렉터리 생성 및 파일 생성

   ```bash
   fastapi-mysql-practice
   ├── main.py
   ├── requirements.txt
   ├── Dockerfile
   └── compose.yml
   ```

2. 필요한 패키지 작성
   - requirements.txt
     ```bash
     fastapi
     uvicorn[standard]
     sqlalchemy
     pymysql
     ```
   - `fastapi`: API 서버 프레임워크
   - `uvicorn`: FastAPI 실행용 ASGI 서버
   - `sqlalchemy`: Python ORM / DB 연결 관리
   - `pymysql`: MySQL 연결 드라이버
3. [main.py](http://main.py) 코드 작성
   - 서버가 시작할 때 mysql에 연결 시도

   ```python
   from contextlib import asynccontextmanager # FastAPI 시작, 종료 시점에 실행할 코드 정의

   from fastapi import FastAPI
   # create_engine : SQLAlchemy DB 연결 엔진 생성
     # text : SQL 문자열을 SQLAlchemy가 실행 가능한 형태로 변환
   from sqlalchemy import create_engine, text

   # DB 접속주소
   DATABASE_URL = "mysql+pymysql://root:pwd1234@localhost:3306/mydb"

   # FastAPI 애플리케이션 생명주기 이벤트 관리
   # yield 이전 : 서버 시작 시
   # yield 이후 : 서버 종료 시
   @asynccontextmanager
   async def lifespan(app: FastAPI):
       engine = create_engine(DATABASE_URL) # DB 연결을 생성하고 관리하는 Engine 객체 생성

       # 애플리케이션 시작 시 DB 연결 확인
       with engine.connect() as connection:
           connection.execute(text("SELECT 1"))

       app.state.engine = engine
       yield

       engine.dispose()

   # FastAPI 애플리케이션 생성
   app = FastAPI(lifespan=lifespan)

   @app.get("/")
   def home():
       return "Hello, Docker Compose!"
   ```

4. Dockerfile 작성

```docker
FROM python:3.12-slim

WORKDIR /app

COPY requirements.txt .

# 필요한 파이썬 패키지 설치
RUN pip install --no-cache-dir -r requirements.txt

# 현재 프로젝트 파일을 컨테이너 내부 /app으로 복사
COPY . .

# FastAPI 서버 실행
ENTRYPOINT ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
```

1. componse.yml 파일 작성하기

   ```yaml
   services:
     my-server:
       build: . # Dockerfile로 이미지 빌드, Dockerfile에는 서버 관련 이미지가 정의되어 있음
       ports:
         - 8001:8000
       # my-db 컨테이너가 생성되고 healthy 하다고 판단될 때 my-server 컨테이너를 생성한다.
       depends_on:
         my-db:
           condition: service_healthy

     my-db:
       image: mysql # 도커허브의 mysql 이미지로 컨테이너 실행
       environment:
         MYSQL_ROOT_PASSWORD: pwd1234
         MYSQL_DATABASE: mydb # MySQL 최초 실행 시 mydb라는 데이터베이스를 생성해준다.
       volumes:
         - ./mysql_data:/var/lib/mysql # 호스트 경로:컨테이너 내부 경로
       ports:
         - 3308:3306
       healthcheck:
         test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-ppwd1234"]
         interval: 5s # 5초 간격으로 체크
         retries: 10 # 10번까지 재시도
   ```

2. compose 파일 실행

   ```bash
   docker compose up -d --build
   ```

3. compose 실행 환경 확인

   ```bash
   docker compose ps
   docker ps
   docker logs [Container ID]
   ```

4. compose 로그 확인

   ```bash
   docker compose logs my-server
   docker compose logs [서비스명]
   ```

### 🔹 DB 연결 에러 확인

- `docker compose logs my-server`
  - DB는 정상적으로 잘 떠있지만, FastAPI 서버와 연결이 안됨
  ```bash
  my-server-1  | INFO:     Started server process [1]
  my-server-1  | INFO:     Waiting for application startup.
  my-server-1  | ERROR:    Traceback (most recent call last):
  my-server-1  |   File "/usr/local/lib/python3.12/site-packages/pymysql/connections.py", line 682, in connect
  my-server-1  |     sock = socket.create_connection(
  ```

### 🔹 현재 문제점

- docker compose는 다음과 같은 구조
  ![image.png](./img/image6.png)
- 이 상황에서 현재 FastAPI의 DB 접속 주소는 다음과 같음
  - `DATABASE_URL = "mysql+pymysql://root:pwd1234@localhost:3306/mydb"`
  - 여기서 localhost는 FastAPI 컨테이너를 의미함
  - 따라서 MySQL 컨테이너의 3306번 포트로 접속을 시도하는게 아니라 FastAPI 컨테이너의 3306 컨테이너로 접속을 시도한 것

### 🔹 문제 해결

- Docker Compose 환경에서는 컨테이너끼리 서비스 이름으로 통신해야 함

  ```python
  DATABASE_URL = "mysql+pymysql://root:pwd1234@my-db:3306/mydb"
  ```

  - my-db는 compose.yml에 작성한 MySQL 서비스 이름

- 수정 후 다시 docker compose를 실행하면, 브라우저의 8001번 포트에서 정상적으로 접속 가능
  ![image.png](./img/image7.png)

## 6. networks:

### 🔹 `networks:` 란

- 컨테이너들이 어떤 가상 네트워크에 붙을지 명시하는 설정
- 위 FastAPI + MySQL에선 생략해도 동작함
  - Docker Compose가 프로젝트마다 default network를 만들고, 같은 네트워크가 붙은 서비스들은 service 이름으로 서로 통신 가능하므로

### 🔹 FastAPI, MySQL 예제에서 network

- Docker Compose가 다음과 같은 default network를 생성함
  ```
  fastapi-mysql-practice_default
  ```
- 그리고 두 컨테이너를 이 네트워크에 붙임
  ```
  my-server ─┐
             ├── fastapi-mysql-practice_default
  my-db    ──┘
  ```
- 따라서 FastAPI 컨테이너에서 MySQL을 서비스명으로 바로 찾을 수 있음

  ```python
  DATABASE_URL = "mysql+pymysql://root:pwd1234@my-db:3306/mydb"
  ```

  - `my-db`는 서비스명이고, Compose 네트워크 안에서 DNS 이름처럼 동작함

### 🔹 networks:가 해결하는 문제 : 기본 네트워크만으로 부족한 경우

- 기본 네트워크만으로는 부족한 경우
  1. 네트워크 이름을 직접 정하고 싶을 때
  2. 서비스별 접근 범위를 나누고 싶을 때
     - API는 DB에 접근 가능한데 외부 프록시는 DB 접근 불가하게 구성하고 싶은 경우
  3. 이미 존재하는 외부 네트워크에 붙이고 싶을 때
     - 다른 compose 프로젝트의 컨테이너와 통신

### 🔹 실습 : networks를 통한 접근 제어

- 구조
  ```
  Host
   ↓
  Nginx(public-network)
   ↓
  FastAPI(public-network + private-network)
   ↓
  MySQL(private-network only)
  ```
- 핵심은 MySQL의 ports를 제거하는 것
  - ports를 열어두면 호스트에서 DB로 직접 접근할 수 있으므로
- `compose.yml`

  ```yaml
  services:
    nginx:
      image: nginx
      ports:
        - 8088:80
      volumes:
        - ./nginx.conf:/etc/nginx/conf.d/default.conf:ro
      depends_on:
        - my-server
      networks:
        - public-network

    my-server:
      build: .
      # host에 직접 공개하지 않음
      # ports:
      #   - 8001:8000
      depends_on:
        my-db:
          condition: service_healthy
      networks:
        - public-network
        - private-network

    my-db:
      image: mysql
      environment:
        MYSQL_ROOT_PASSWORD: pwd1234
        MYSQL_DATABASE: mydb
      volumes:
        - ./mysql_data:/var/lib/mysql
      # host에 직접 공개하지 않음
      # ports:
      #   - 3308:3306
      healthcheck:
        test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-ppwd1234"]
        interval: 5s
        retries: 10
      networks:
        - private-network

  networks:
    public-network:
      name: fastapi-public-network
      driver: bridge

    private-network:
      name: fastapi-private-network
      driver: bridge
  ```

  - Docker Compose의 networks는 서비스 간 통신 경로를 정의함
  - 같은 네트워크에 붙은 서비스는 서비스명으로 찾을 수 있고, 서로 다른 bridge network에 있는 컨테이너는 기본적으로 격리됨

- `nginx.conf` 추가

  ```
  server {
      listen 80;

      location / {
          proxy_pass http://my-server:8000;
      }
  }
  ```

  - `my-server`는 Compose의 서비스명
  - 즉 nginx의 80번 포트로 요청이 오면, my-server:8000로 요청 전송

- FastAPI DB URL 확인

  ```python
  DATABASE_URL = "mysql+pymysql://root:pwd1234@my-db:3306/mydb"
  ```

  - `my-server`와 `my-db`가 둘 다 `private-network`에 붙어 있기 때문에 이 접근은 가능

---

- 실행 및 상태 확인
  ```bash
  docker compose down
  docker compose up --build -d
  ```
  ```bash
  docker compose ps
  ```
- 접근 제어 확인
  1. Host → Nginx 접근 가능

     ![image.png](./img/image8.png)

     ```
     Host:8088
     → nginx:80
     → my-server:8000
     ```

  2. Host → FastAPI 직접 접근 실패

     ```bash
     curl http://localhost:8001
     ```

     - 포트 매핑을 안했으므로 실패해야 정상
     - FastAPI 컨테이너는 도커 내부 네트워크에서는 접근 가능하지만, 호스트에는 직접 공개되지 않음

  3. Host → MySQL 직접 접근 실패

     ```bash
     mysql -h 127.0.0.1 -P 3308 -uroot -p
     ```

     - 마찬가지로 포트 매핑을 안했으므로 실패해야 정상

  4. FastAPI 컨테이너 → MySQL 접근 성공
     - FastAPI 컨테이너는 `private-network`에 붙어 있으므로 MySQL 접근이 가능

     ```bash
     docker compose exec my-server bash
     ```

     ```bash
     root@675a6f79cca3:/app mysql --skip-ssl -h my-db -P 3306 -uroot -p
     Enter password:
     Welcome to the MariaDB monitor.  Commands end with ; or \g.
     Your MySQL connection id is 115

     MySQL [(none)]>
     ```

- ports, networks
  - ports는 호스트 컴퓨터에 공개할 때만 사용
  - networks는 컨테이너끼리 통신 가능한 범위를 나눌 때 사용

![image.png](./img/image9.png)
