package com.example.ecommerce.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderDto {
    private Long userId;
    private String shippingAddress;
    private List<OrderItemDto> items;
}
