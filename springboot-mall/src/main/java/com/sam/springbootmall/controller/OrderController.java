package com.sam.springbootmall.controller;

import com.sam.springbootmall.dto.CreateOrderRequest;
import com.sam.springbootmall.model.Order;
import com.sam.springbootmall.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<?> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderRequest createdOrderRequest) {
        Integer orderId = orderService.createOrder(userId, createdOrderRequest);

        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/users/{userId}/{orderId}")
    public ResponseEntity<?> getOrders(@PathVariable Integer userId,
                                       @PathVariable Integer orderId) {
        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.OK).body(order);

    }
}
