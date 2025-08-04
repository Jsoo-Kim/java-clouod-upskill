# MSA 애플리케이션 배포 자동화 CI/CD 구축 실습

# 컨테이너 빌드 및 실행
1. Blue 버전의 화면이 출력됩니다
```sh
docker-compose up -d

open http://localhost:8080
```
2. Green 환경으로 변경
- `nginx.conf`에서 `proxy_pass http://blue;` → `proxy_pass http://green;` 으로 변경

3. Nginx 컨테이너를 재시작
```bash
docker-compose restart nginx
```

4. 다시 확인 (Green 환경)
```bash
open http://localhost:8080
```
이제 Green 버전이 표시됩니다.  
