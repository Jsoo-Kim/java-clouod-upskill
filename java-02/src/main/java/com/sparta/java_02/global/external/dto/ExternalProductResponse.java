package com.sparta.java_02.global.external.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExternalProductResponse {

  /**
   * 최상위 응답 객체로, 성공 여부(result), 에러 정보(ExternalError), 그리고 상품 관련 메시지(ExternalPage)를 포함합니다.
   */

  private Boolean result;
  private ExternalError error;
  private ExternalPage message;

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class ExternalError {

    private String errorCode;
    private String errorMessage;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class ExternalPage {

    /**
     * 실제 상품 목록(contents)과 페이징 정보(ExternalPageable)를 담고 있습니다.
     */

    private List<ExternalResponse> contents;
    private ExternalPageable pageable;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class ExternalPageable {

    private Long offset;
    private Long pageNumber;
    private Long pageSize;
    private Long pageElements;
    private Long totalPages;
    private Long totalElements;
    private boolean first;
    private boolean last;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class ExternalResponse {

    /**
     * 개별 상품에 대한 상세 정보를 포함합니다.
     */

    Long id;
    String name;
    String description;
    BigDecimal price;
    Integer stock;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
  }
}
