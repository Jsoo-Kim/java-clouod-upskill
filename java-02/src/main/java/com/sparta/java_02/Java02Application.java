package com.sparta.java_02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class

Java02Application {

  public static void main(String[] args) {
    SpringApplication.run(Java02Application.class, args);
  }

}
