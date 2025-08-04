# MSA User API 
MSA 강좌 중 예제 User API 소스코드입니다.

# Getting Started
1. 로컬 컴퓨터에서 docker-compose 로 mysql 설치
```shell
docker-compose up --build
```

2. MySQL docker container 접속 후 샘플 데이터 insert
```shell
docker exec -it user-api-user-db-1 /bin/bash

# container
bash-5.1# mysql -u root -p
mysql>  insert into users (email, password, username) values ("aaa@gmail.com", "1234", "wonder"); 
```

3. Application 실행 및 API 접속 테스트 
```shell
$ open http://localhost:8081/api/users/1
```
# Swagger 
```shell
open http://localhost:8081/swagger-ui/index.html
```