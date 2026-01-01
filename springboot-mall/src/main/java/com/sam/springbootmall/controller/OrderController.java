package com.sam.springbootmall.controller;

import com.sam.springbootmall.dto.CreateOrderRequest;
import com.sam.springbootmall.dto.OrderItemQueryParams;
import com.sam.springbootmall.dto.OrderQueryParams;
import com.sam.springbootmall.model.Order;
import com.sam.springbootmall.model.OrderItem;
import com.sam.springbootmall.service.OrderService;
import com.sam.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

//創建order
    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<Order> createOrder
            (@PathVariable Integer userId,
             @RequestBody @Valid CreateOrderRequest createdOrderRequest) {
        Integer orderId = orderService.createOrder(userId, createdOrderRequest);

        Order order = orderService.getOrderByOrderId(orderId, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
//取得order
    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<Page<Order>> getOrders
            (@PathVariable Integer userId,
             @RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,
             @RequestParam(defaultValue = "0") @Min(0) Integer offset) {
        OrderQueryParams orderQueryParams = new OrderQueryParams();
        orderQueryParams.setUserId(userId);
        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);

        List<Order> orderList = orderService.getOrders(orderQueryParams);

        //取得order總數
        Integer count = orderService.countOrder(orderQueryParams);

        Page<Order> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(count);
        page.setResults(orderList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    //productMall(addToCart)
    //創建或更新orderList
    @PostMapping("/createOrderList/{userId}")
    public ResponseEntity<List<OrderItem>> createOrderList(
            @PathVariable Integer userId,
            @RequestBody @Valid CreateOrderRequest createdOrderRequest) {
        orderService.createOrderItem(userId, createdOrderRequest);

        List<OrderItem> orderItemList = orderService.getOrderItemList(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderItemList);
    }

    //shopCart(updateCount)
    //更新orderList(shopCart)
    @PutMapping("/updateOrderList/{userId}")
    public ResponseEntity<List<OrderItem>> updateOrderList(
            @PathVariable Integer userId,
            @RequestBody @Valid CreateOrderRequest createdOrderRequest) {
        orderService.updateOrderItem(userId, createdOrderRequest);

        List<OrderItem> orderItemList = orderService.getOrderItemList(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderItemList);
    }

    //shopCart(handleDelete)
    //刪除orderList
    @DeleteMapping("/deleteOrderList/{userId}/{productId}")
    public ResponseEntity<List<OrderItem>> deleteOrderList(
            @PathVariable Integer userId,
            @PathVariable Integer productId){
        orderService.deleteOrderItem(productId);
        List<OrderItem> orderItemList = orderService.getOrderItemList(userId);
        return ResponseEntity.status(HttpStatus.OK).body(orderItemList);
    }

    //productMall(fetchBuyItemList)
    //shopCart(fetchBuyItemList)
    //取得orderListByUser
    @GetMapping("/getOrderList/{userId}")
    public ResponseEntity<Page<OrderItem>> getOrderListByUserId
            (@PathVariable Integer userId,
             @RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,
             @RequestParam(defaultValue = "0") @Min(0) Integer offset) {
        OrderItemQueryParams orderItemQueryParams = new OrderItemQueryParams();
        orderItemQueryParams.setUserId(userId);
        orderItemQueryParams.setLimit(limit);
        orderItemQueryParams.setOffset(offset);

        List<OrderItem> orderList = orderService.getOrderItemList(orderItemQueryParams);

        //取得order總數
        Integer count = orderService.countOrderItem(orderItemQueryParams);

        Page<OrderItem> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(count);
        page.setResults(orderList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }
}
