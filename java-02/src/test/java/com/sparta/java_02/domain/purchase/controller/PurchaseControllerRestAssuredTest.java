package com.sparta.java_02.domain.purchase.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PurchaseControllerRestAssuredTest {

  @LocalServerPort
  private int port; // 실행된 서버의 포트 번호를 주입받음

  @BeforeEach
  void setUp() {
    // 모든 테스트 실행 전, Rest Assured가 요청을 보낼 포트를 설정
    RestAssured.port = port;
  }

//  @Test
//  void 주문_성공() {
//    // given: 요청 Body 준비
//    String requestBody = """
//        {
//            "userId": 1,
//            "purchaseProducts": [
//                {
//                    "productId": 1,
//                    "quantity": 10
//                }
//            ]
//        }
//        """;
//
//    // when & then
//    RestAssured.given().log().all()                // (요청 로깅)
//        .contentType(ContentType.JSON)             // 요청 헤더의 Content-Type 설정
//        .body(requestBody)                         // 요청 Body 데이터 추가
//        .when()
//        .post("/api/purchase")                     // POST 요청 실행
//        .then().log().all()                        // (응답 로깅)
//        .statusCode(201)                           // 응답 상태 코드가 201 Created 인지 검증
//        .body("result", IsEqual.equalTo(true));    // 응답 Body의 'result' 필드 값이 true인지 검증
////        .body("data.purchaseId", notNullValue());  // 'data.purchaseId' 필드가 null이 아닌지 검증
//  }

}
