package com.sparta.java_02.domain.purchase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PurchaseControllerTest {

  @Autowired
  private MockMvc mockMvc; // API 테스트를 위한 핵심 객체

  @Autowired
  private ObjectMapper objectMapper; // 객체를 JSON 문자열로 변환하기 위한 객체

//  @Test
//  void 주문_생성() throws Exception {
//    // given
//    List<PurchaseProductRequestTest> purchaseProductRequestTests = new ArrayList<>();
//    purchaseProductRequestTests.add(new PurchaseProductRequestTest(1L, 10));
//
//    PurchaseRequestTest purchaseRequestTest = new PurchaseRequestTest(1L,
//        purchaseProductRequestTests);
//
//    String requestBody = new ObjectMapperConfig().writeValueAsString(purchaseRequestTest);
//
//    // when & then
//    mockMvc.perform(
//            MockMvcRequestBuilders.post("/api/purchase")
//                .contentType(MediaType.APPLICATION_JSON.toString())
//                .content(requestBody)
//                .accept(MediaType.APPLICATION_JSON.toString()))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.result").value(true));
//  }

//  @Test
//  void 유저_없음_체크() throws Exception {
//    // given
//    List<PurchaseProductRequestTest> purchaseProductRequestTests = new ArrayList<>();
//    purchaseProductRequestTests.add(new PurchaseProductRequestTest(1L, 10));
//
//    PurchaseRequestTest purchaseRequestTest = new PurchaseRequestTest(null,
//        purchaseProductRequestTests);
//
//    String requestBody = new ObjectMapperConfig().writeValueAsString(purchaseRequestTest);
//
//    // when & then
//    mockMvc.perform(
//            MockMvcRequestBuilders.post("/api/purchase")
//                .contentType(MediaType.APPLICATION_JSON.toString())
//                .content(requestBody)
//                .accept(MediaType.APPLICATION_JSON.toString()))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.result").value("NOT_FOUND_USER"));
//
////    mockMvc.perform(
////            MockMvcRequestBuilders.post("/api/purchase")
////                .contentType(MediaType.APPLICATION_JSON.toString())
////                .content(requestBody)
////                .accept(MediaType.APPLICATION_JSON.toString()))
////        .andExpect(MockMvcResultMatchers.status().isBadRequest())
////        .andExpect(MockMvcResultMatchers.jsonPath("$.error.errorCode").value("VALIDATE_ERROR"));
//  }

//  @Test
//  void 수량_체크() throws Exception {
//    // given: 재고(예: 5개)보다 많은 수량(예: 10개)을 주문하는 DTO
//    List<PurchaseProductRequestTest> purchaseProductRequestTests = new ArrayList<>();
//    purchaseProductRequestTests.add(new PurchaseProductRequestTest(1L, 100000));
//
//    PurchaseRequestTest purchaseRequestTest = new PurchaseRequestTest(1L,
//        purchaseProductRequestTests);
//
//    String requestBody = objectMapper.writeValueAsString(purchaseRequestTest);
//
//    // when & then
//    mockMvc.perform(
//            MockMvcRequestBuilders.post("/api/purchase")
//                .contentType(MediaType.APPLICATION_JSON.toString())
//                .content(requestBody)
//                .accept(MediaType.APPLICATION_JSON.toString()))
//        .andExpect(MockMvcResultMatchers.status().isOk())
////        .andExpect(MockMvcResultMatchers.status().isBadRequest()) // ServiceException도 400으로 처리한다고 가정
//        .andExpect(MockMvcResultMatchers.jsonPath("$.error.code").value("OUT_OF_STOCK_PRODUCT"));
//  }


}