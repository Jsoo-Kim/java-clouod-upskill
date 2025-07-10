package com.sparta.java_02.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Slf4j
// 기능 로직들은 주로 @Service 사용. @Component로는 못 쓰는 기능들이 있기 때문
// 단, 맥락 상 Common의 Utils에 @Service를 쓰는 게 맥락 상 맞나 하는 고민
@Service
@RequiredArgsConstructor
public class JedisUtil { // 공통 코드 (예: 파싱 작업)

  private final Jedis jedis;
  private final ObjectMapper objectMapper;
  //  private final ObjectMapper objectMapper = new ObjectMapper(); // config에서 빈으로 등록 안 하면 주입 안 되니까 이렇게 해 줘야 함

  public <T> void saveObject(String key, T object) {
    try {
      String jsonString = objectMapper.writeValueAsString(object);

      jedis.set(key, jsonString);
    } catch (Exception e) {
      log.error("[RedisService] saveObject {}c: {}", key, e.getMessage());
    }
  }

  public <T> void saveObject(String key, T object, int ttlInSeconds) {
    try {
      String jsonString = objectMapper.writeValueAsString(object);

      jedis.setex(key, ttlInSeconds, jsonString);
    } catch (Exception e) {
      log.error("[RedisService] saveObject {}c: {}", key, e.getMessage());
    }
  }

  public void setKeyTtl(String key, int ttlInSeconds) { // 기존에 있는 key에 ttl 다시 세팅 가능
    try {
      jedis.expire(key, ttlInSeconds);
    } catch (Exception e) {
      log.error("[RedisService] setKeyTtl {} : {}", key, e.getMessage());
    }
  }

  public Optional<Long> getKeyTtl(String key) { // ttl이 얼마나 남았는지 확인 가능
    try {
      return Optional.of(jedis.ttl(key));
    } catch (Exception e) {
      log.error("[RedisService] getKeyTtl {} : {}", key, e.getMessage());
    }
    return Optional.empty();
  }

}
