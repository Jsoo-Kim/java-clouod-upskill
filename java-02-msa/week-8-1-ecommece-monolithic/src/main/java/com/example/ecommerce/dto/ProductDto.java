package com.example.ecommerce.dto;

import lombok.Data;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class ProductDto {
    @NotBlank
    private String name;
    
    private String description;
    
    @Positive
    private BigDecimal price;
    
    @Positive
    private Integer stock;
    
    private String category;
}
