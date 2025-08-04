package com.ecommerce.order.controller;

import com.ecommerce.order.event.OrderEventPublisher;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@Api(value = "Order Service API")
public class OrderController {
    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Autowired
    private OrderService orderService;

    @PostMapping
    @ApiOperation(value = "Create a new order")
    public ResponseEntity<String> createOrder(@RequestBody Order order) {

        order.setStatus("PENDING");
        orderService.createOrder(order);
        orderEventPublisher.publishOrderCreated(order.getId());

        return ResponseEntity.ok("Order Created: " + order.getId().toString());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get order by ID")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
}
