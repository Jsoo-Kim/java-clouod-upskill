package com.sparta.java_02.domain.category.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.category.dto.CategoryRequest;
import com.sparta.java_02.domain.category.dto.CategoryResponse;
import com.sparta.java_02.domain.category.entity.Category;
import com.sparta.java_02.domain.category.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import redis.clients.jedis.Jedis;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

  private final Jedis jedis;
  private final ObjectMapper objectMapper;
//  private final JedisUtil jedisUtil;

  private final CategoryRepository categoryRepository;

  private static final String CACHE_KEY_CATEGORY_STRUCT = "categoryStruct";
  private static final int CACHE_EXPIRE_SECONDS = 3600; // 1시간


  // private으로 하고 Transactional 안 붙임! 밑에 캐시를 조회하는 기능 추가! 캐시 어사이드 패턴
  private List<CategoryResponse> findCategoryStruct() {
    List<Category> categories = categoryRepository.findAll();

    Map<Long, CategoryResponse> categoryResponseMap = new HashMap<>();

    for (Category category : categories) {
      CategoryResponse response = CategoryResponse.builder()
          .id(category.getId())
          .name(category.getName())
          .categories(new ArrayList<>())
          .build();
      categoryResponseMap.put(category.getId(), response);
    }

    List<CategoryResponse> rootCategories = new ArrayList<>();
    for (Category category : categories) {
      CategoryResponse categoryResponse = categoryResponseMap.get(category.getId());

      if (ObjectUtils.isEmpty(category.getParent())) {
        rootCategories.add(categoryResponse);
      } else {
        CategoryResponse parentResponse = categoryResponseMap.get(category.getParent().getId());
        if (parentResponse != null) {
          parentResponse.getCategories().add(categoryResponse);
        }
      }
    }

    return rootCategories;
  }


  // 일단 모든 public 메서드에는 Transactional 붙인다 정도로 이해
  @Transactional(readOnly = true)
  public List<CategoryResponse> findCategoryStructCacheAside() throws JsonProcessingException {
    // 1. 캐시에서 카테고리 구조 데이터 조회 시도
    String cachedCategories = jedis.get(CACHE_KEY_CATEGORY_STRUCT);

    // 2. 캐시 히트
    if (!ObjectUtils.isEmpty(cachedCategories)) {
      System.out.println("Cache Hit: categoryStruct for key " + CACHE_KEY_CATEGORY_STRUCT);
      return objectMapper.readValue(cachedCategories, new TypeReference<>() {
      });
    }

    // 3. 캐시 미스, 데이터베이스에서 조회 (findCategoryStruct() 호출)
    System.out.println("Cache Miss: categoryStruct for key " + CACHE_KEY_CATEGORY_STRUCT);
    List<CategoryResponse> rootCategories = findCategoryStruct();

    // 4. 데이터베이스에서 조회한 데이터를 캐시에 저장
    if (!ObjectUtils.isEmpty(rootCategories)) {
      String jsonString = objectMapper.writeValueAsString(rootCategories);
      jedis.setex(CACHE_KEY_CATEGORY_STRUCT, CACHE_EXPIRE_SECONDS,
          jsonString); // setex는 set과 다르게 만료 시간도 같이 적음
    }

    return rootCategories; // 데이터베이스에서 조회한 데이터 반환
  }

  @Transactional
  public void saveWriteThrough(CategoryRequest request) { // Category 먼저 (DB 먼저) 갱신 후 캐시 갱신
    try {
      Category newCategory = Category.builder().name(request.getName()).build();
      categoryRepository.save(newCategory); // DB에 먼저 save

      updateCategoryStructCache(); // 캐시 업데이트 메서드 호출

    } catch (Exception e) {
      log.error("Failed to save category with Write-through: {}", e.getMessage(), e);
    }
  }

  private void updateCategoryStructCache() {
    try {
      List<CategoryResponse> rootCategories = findCategoryStruct();

      if (!ObjectUtils.isEmpty(rootCategories)) {
        String jsonString = objectMapper.writeValueAsString(rootCategories);
        jedis.setex(CACHE_KEY_CATEGORY_STRUCT, CACHE_EXPIRE_SECONDS, jsonString);
      }
    } catch (Exception e) {
      log.error("Error updating cache key {}: {}", CACHE_KEY_CATEGORY_STRUCT, e.getMessage());
    }
  }


  // Redis부터 빠르게 갱신, DB에 쓰기 작업은 별도 스레드나 프로세스 or 메시지 큐 등을 통해 나중에 비동기적으로 수행 (배치 처리 방식 사용하기도 함)
  @Transactional
  public Boolean saveWriteBack(CategoryRequest request) {
    try {
      // 1. 캐시에서 현재 카테고리 구조를 조회
      String cachedData = jedis.get(CACHE_KEY_CATEGORY_STRUCT);
      List<CategoryResponse> categories;

      if (cachedData != null && !cachedData.isEmpty()) {
        categories = objectMapper.readValue(cachedData,
            new TypeReference<List<CategoryResponse>>() {
            });
      } else {
        categories = new ArrayList<>();
      }

      // 2. 캐시에 새로운 카테고리 데이터 추가
      CategoryResponse newCategory = CategoryResponse.builder()
          .name(request.getName())
          .categories(new ArrayList<>())
          .build();

      // TODO: 신규 카테고리는 부모 클래스 하위로 들어가도록 수정되어야 함
      categories.add(newCategory);

      String jsonString = objectMapper.writeValueAsString(categories);
      jedis.setex(CACHE_KEY_CATEGORY_STRUCT, CACHE_EXPIRE_SECONDS, jsonString);

      // 3. 데이터베이스 저장 작업은 비동기로 처리
      saveToDatabaseAsync(request);

      return true;
    } catch (Exception e) {
      log.error("Write-back 패턴 저장 실패: {}", e.getMessage(), e);
      return false;
    }
  }

  @Async
  public void saveToDatabaseAsync(CategoryRequest request) {
    try {
      Thread.sleep(2000);

      Category parentCategory = ObjectUtils.isEmpty(request.getCategoryId())
          ? null
          : categoryRepository.findById(request.getCategoryId())
              .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_DATA));

      Category newCategory = Category.builder()
          .name(request.getName())
          .build();

      categoryRepository.save(newCategory);

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.error("비동기 DB 저장 중 스레드 인터럽트: {}", e.getMessage(), e);
    } catch (Exception e) {
      // 캐시 롤백 혹은 retry 로직 필요 (단, 복구 로직은 부담 있음)
      // MSA 서비스에선 이벤트 큐 -> 저장 이벤트 발송 -> 배치로직에 의해서 저장 (가장 안전)
      log.error("비동기 DB 저장 실패: {}", e.getMessage(), e);
    }
  }
}


