//package com.sparta.java_02.global.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class BeanConfig {
//
//  // 스프링부트에게 UserService는 UserServiceImplV2 라는 것을 알려주고
//  // Bean으로 등록함 (인스턴스화 대상)
//  // 프로그램 시작하자마자 Bean부터 읽음
//  // Bean들은 싱글톤 패턴으로 관리됨 (이 프로젝트에 UserServiceImplV2 라는 인스턴스는 유일무이하다는 전략)
//  @Bean
//  public UserService userService() {
//    return new UserServiceImplV2();
//  }
//
//}
