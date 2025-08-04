FROM openjdk:11-jdk-slim AS builder

WORKDIR /app

# Gradle Wrapper와 의존성 파일들을 먼저 복사 (캐시 최적화)
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# gradlew 실행 권한 부여 및 줄바꿈 문제 해결
RUN chmod +x ./gradlew && \
    sed -i 's/\r$//' ./gradlew

# 의존성 다운로드 (소스 변경 시 캐시 활용)
RUN ./gradlew dependencies --no-daemon || true

# 소스 코드 복사
COPY src src

# 애플리케이션 빌드
RUN ./gradlew bootJar --no-daemon

FROM openjdk:11-jre-slim

WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 로그 디렉토리 생성
RUN mkdir -p /app/logs

# 환경 변수 설정
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8083

# 포트 노출
EXPOSE 8083

# 애플리케이션 실행 (더 나은 JVM 옵션 포함)
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]