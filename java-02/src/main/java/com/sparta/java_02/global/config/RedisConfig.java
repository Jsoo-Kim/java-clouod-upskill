package com.sparta.java_02.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class RedisConfig {

  // yaml 파일 참조하여 값을 가져옴! lombok 아니고 pringframework.beans.factory.annotation.Value;
  @Value("${spring.data.redis.host}")
  private String redisHost;

  @Value("${spring.data.redis.port}")
  private int redisPort;

  // 현재 password에 데이터가 없기 때문에 : 붙임. 데이터가 없을 수도 있다는 뜻 (비밀번호가 없으면 null 또는 빈 문자열)
  @Value("${spring.data.redis.password:}") // 기본값을 빈 문자열로 설정하여 비밀번호가 없을 때 오류 방지
  private String redisPassword;

  @Bean
  public Jedis jedis() {
    Jedis jedis = new Jedis(redisHost, redisPort);

    if (redisPassword != null && !redisPassword.isEmpty()) {
      jedis.auth(redisPassword); // 비밀번호가 있다면 인증
    }

    return jedis;
  }
}
