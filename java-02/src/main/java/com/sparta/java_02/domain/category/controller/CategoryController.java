package com.sparta.java_02.domain.category.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.java_02.common.response.ApiResponse;
import com.sparta.java_02.domain.category.dto.CategoryRequest;
import com.sparta.java_02.domain.category.dto.CategoryResponse;
import com.sparta.java_02.domain.category.service.CategoryJdbcService;
import com.sparta.java_02.domain.category.service.CategoryService;
import jakarta.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService categoryService;
  private final CategoryJdbcService categoryJdbcService;

  @GetMapping
  public ApiResponse<List<CategoryResponse>> getCategories() throws JsonProcessingException {
    return ApiResponse.success(categoryService.findCategoryStructCacheAside());
  }

  @PostMapping
  public ApiResponse<Void> save(@Valid @RequestBody CategoryRequest request) {
    categoryService.save(request);
    return ApiResponse.success();
  }

  @PatchMapping("/{id}/name")
  public ApiResponse<Void> updateByName(
      @RequestParam Long id,
      @RequestBody CategoryRequest request) throws SQLException {
    categoryJdbcService.updateCategory(id, request.getName());
    return ApiResponse.success();
  }
}
