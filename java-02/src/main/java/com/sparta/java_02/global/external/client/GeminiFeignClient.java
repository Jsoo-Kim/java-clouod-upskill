package com.sparta.java_02.global.external.client;

import com.sparta.java_02.global.config.OpenFeignConfig;
import com.sparta.java_02.global.external.dto.GeminiDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "gemini-api",
    url = "${gemini.api.url}",
    configuration = OpenFeignConfig.class
)
public interface GeminiFeignClient {

  @PostMapping(value = "/v1beta/models/gemini-2.0-flash:generateContent")
  GeminiDto.Response generateContent(
      @RequestParam("key") String apiKey,
      @RequestBody GeminiDto.Request request
  );
}
