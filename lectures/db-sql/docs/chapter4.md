# Ch 4. 테이블에 데이터 추가 / 수정 / 삭제하기

# 1. 데이터 추가하기 (insert)

🤖 EMPLOYEE 테이블에 데이터 추가하기

| id | name | birth_date | sex | position | salary | dept_id |
| --- | --- | --- | --- | --- | --- | --- |

```sql
insert into EMPLOYEE
	values (1, 'MESSI', '1987-02-01', 'M', 'DEV_BACK', 100000000, null);
```

```sql
insert into {테이블 이름}
 values( {값} );
```

- 값의 순서가 중요함 → attribute 순서대로 값을 넣어줘야함

👉 dept_id에 NULL을 넣은 이유

- 현재 DEPARTMENT 테이블에 값이 없으므로 참조할 대상이 없음
- 따라서 NULL을 우선 넣어준 것

🤖 EMPLOYEE 테이블에 데이터 추가하기 2

```sql
insert into EMPLOYEE
	values (2, 'JANE', '1996-05-01', 'F', 'DSGN', 60000000, null);
```

🤖 EMPLOYEE 테이블에 데이터 추가하기 3

- attribute 적어주기
- 이러면 값을 넣는 순서는 쿼리에 적은 순서대로 넣을 수 있음
- 그리고 값을 무조건 다 넣을 필요 없이 넣고 싶은 값만 넣을 수 있음
- 적어주지 않은 attribute는 default 값이 들어감

→ default가 없다면 에러

```sql
insert into EMPLOYEE (name, birth_date, sex, position, id)
	values ('JENNY', '2000-01-04', 'F', 'DEV_FRONT', 3);
```

✅ insert 문 정리

1. 모든 attribute에 레코드 1개 추가하기

```sql
insert into table_name
	values ( val1, val2, ...);
```

2. 특정 attribute에 레코드 1개 추가하기

```sql
insert into table_name( att1, att2 )
	values ( val1, val2 );
```

3. 모든 attribute에 레코드 여러 개 추가하기

```sql
insert into table_name
	values  ( val1, val2, ... ),
					( val1, val2, ... ),
					( val1, val2, ... );
				
```

🤖 EMPLOYEE 테이블에 데이터 추가하기

```sql
insert into EMPLOYEE values
(4, 'BROWN', '1996-03-13', 'M', 'CEO', 120000000, null),
(5, 'DINGYO', '1990-11-05', 'M', 'CTO', 120000000, null),
(6, 'JULIA', '1986-12-13', 'F', 'CFO', 120000000, null),
(7, 'MINA', '1993-06-17', 'F', 'DSGN', 80000000, null),
(8, 'JOHN', '1999-10-22', 'M', 'DEV_FRONT', 65000000, null),
(9, 'HENRY', '1982-05-20', 'M', 'HR', 82000000, null),
(10, 'NICOLE', '1991-03-26', 'F', 'DEV_FRONT', 90000000, null),
(11, 'SUZANNE', '1993-03-23', 'F', 'PO', 75000000, null),
(12, 'CURRY', '1996-03-13', 'M', 'PLN', 85000000, null),
(13, 'JISUNG', '1987-07-07', 'M', 'PO', 90000000, null),
(14, 'SAM', '1992-08-04', 'M', 'DEV_INFRA', 70000000, null);
```

🤖 DEPARTMENT 테이블에 데이터 추가하기

```sql
insert into DEPARTMENT values
(1001, 'headquarter', 4),
(1002, 'HR', 6),
(1003, 'development', 1),
(1004, 'design', 3),
(1005, 'product', 13);
```

🤖 PROJECT 테이블에 데이터 추가하기

```sql
insert into PROJECT values
(2001, '쿠폰 구매/선물 서비스 개발', 13, '2022-03-10', '2022-07-09'),
(2002, '백엔드 리팩토링', 13, '2022-01-23', '2022-03-23'),
(2003, '홈페이지 UI 개선', 11, '2022-05-09', '2022-06-11');
```

🤖 WORKS_ON 테이블에 데이터 추가하기

```sql
INSERT INTO WORKS_ON (empl_id, proj_id)
VALUES
  (1, 2001),  -- MESSI -> 프로젝트 2001
  (2, 2001),  -- JANE -> 2001
  (3, 2003),  -- JENNY -> UI 프로젝트
  (7, 2003),  -- MINA -> UI 프로젝트
  (8, 2003),  -- JOHN -> UI 프로젝트
  (10, 2002), -- NICOLE -> 프로젝트 2002
  (11, 2003), -- SUZANNE -> UI 프로젝트
  (12, 2002), -- CURRY -> 프로젝트 2002
  (14, 2001); -- SAM -> 프로젝트 2001
```

# 2. 데이터 수정하기 (update)

🤖 현재 EMPLOYEE 테이블의 dept_id는 NULL

- 따라서 이를 업데이트 해줘야함

```sql
update EMPLOYEE 
set dept_id = 1003
where id = 1;
```

- 여러 직원의 dept_id 업데이트

```sql
update EMPLOYEE 
set dept_id = 1003
where id in (1,3,4,5);
```

🤖 2002 프로젝트에 참여하고 있는 사람의 연봉을 2배로 올려주기

- EMPLOYEE, WORKS_ON 2개의 테이블 필요

```sql
update EMPLOYEE E, WORKS_ON W
set salary = salary * 2
where E.id = W.empl_id
and W.proj_id = 2002;
```

✅ update 문 정리

```sql
update {테이블 이름}
set {컬럼명} = {값}, ({컬럼명}={값})
(where {조건});
```

- 여러 컬럼 업데이트 가능
- 여러 레코드 업데이트 가능
- `where` 절이 없다면 전체 레코드 업데이트

# 3. 데이터 삭제하기 (delete)

🤖 JOHN과 관련된 정보 삭제하기

```sql
delete from EMPLOYEE
where id = 8;
```

- 그렇다면 WORKS_ON 테이블도 같이 삭제를 해줘야하는가? → NO
- ∵ WORKS_ON 테이블은 FK로 EMPLOYEE 테이블 참조 중
- 그리고 on delete cascade 조건 포함 중
- 따라서 EMPLOYEE 테이블의 데이터가 사라지면, WORKS_ON의 해당 데이터도 같이 삭제됨

🤖 JANE의 WORKS_ON 관련 정보 삭제하기

```sql
delete from WORKS_ON
where empl_id = 2;
```

✅ 조건절 키워드 `<>` , `!=`

- 연산자 뒤에 나오는 항목을 제외함을 의미

```sql
delete from WORKS_ON 
where proj_id = 2003 
and empl_id <> 3;
```

- `proj_id = 2003`인 여러 레코드 중에서, `empl_id = 3`인 레코드를 제외한 나머지 레코드 삭제

✅ delete 문 정리

```sql
 delete from {테이블명}
 where {조건}
```