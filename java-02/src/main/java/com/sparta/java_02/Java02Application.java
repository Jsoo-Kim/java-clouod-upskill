package com.sparta.java_02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableScheduling
@EnableAsync
@EnableRedisHttpSession // 컨트롤러에서 세션을 쓰면 전부 레디스로 넘어가게 해 줌
@SpringBootApplication
public class

Java02Application {

  public static void main(String[] args) {
    SpringApplication.run(Java02Application.class, args);
  }

}
