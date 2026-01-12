package com.sam.springbootmall.controller;

import com.sam.springbootmall.dto.CreateOrderRequest;
import com.sam.springbootmall.dto.OrderItemQueryParams;
import com.sam.springbootmall.dto.OrderQueryParams;
import com.sam.springbootmall.model.Order;
import com.sam.springbootmall.model.OrderItem;
import com.sam.springbootmall.service.OrderService;
import com.sam.springbootmall.service.ProductService;
import com.sam.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    //-----------------------------------------------------------------order------------------

    //shopCart(checkout)
    //創建order
    @PostMapping("/{userId}/{userName}/orders")
    public ResponseEntity<Order> createOrder
            (@PathVariable Integer userId,
             @PathVariable String userName,
             @RequestBody @Valid CreateOrderRequest createdOrderRequest) {
        Integer orderId = orderService.createOrder(userId, userName, createdOrderRequest);

        Order order = orderService.getOrderByOrderId(orderId, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    //payment(confirmPayment)
    //付款後，更新order、product、orderList
    @PutMapping("/updateOrder/{orderId}")
    public ResponseEntity<String> updateOrder(
            @PathVariable Integer orderId) {
        // 調整商品數量
        productService.updateStock(orderId);
        // 調整order (已付款) (檢查庫存是否足夠)
        orderService.updateOrder(orderId);
        // 刪除orderItem 訂購商品
        orderService.deleteOrderItemByOrderId(orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body("付款成功");
    }

    //orderManage(changeOrderStatus)
    //更新order狀態(取消、出貨)
    @PutMapping("/updateOrder/{orderId}/{status}")
    public ResponseEntity<String> updateOrder(
            @PathVariable Integer orderId,
            @PathVariable String status) {
        orderService.updateOrder(orderId, status);;

        return ResponseEntity.status(HttpStatus.CREATED).body("更改成功");
    }

    //orderManage(fetchOrders)
    //取得order
    @GetMapping("orders")
    public ResponseEntity<Page<Order>> getOrders
    (@RequestParam(defaultValue = "50") @Max(1000) @Min(0) Integer limit,
     @RequestParam(defaultValue = "0") @Min(0) Integer offset) {
        OrderQueryParams orderQueryParams = new OrderQueryParams();
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

    //orderUser(fetchUserOrders)
    //取得userOrder
    @GetMapping("orders/{userId}")
    public ResponseEntity<Page<Order>> getOrdersByUserId
    (@PathVariable Integer userId,
     @RequestParam(defaultValue = "50") @Max(1000) @Min(0) Integer limit,
     @RequestParam(defaultValue = "0") @Min(0) Integer offset) {
        OrderQueryParams orderQueryParams = new OrderQueryParams();
        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);
        orderQueryParams.setUserId(userId);

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

    //-------------------------------------------------------------------orderList-------------

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
    //更新orderList
    @PutMapping("/updateOrderList/{userId}")
    public ResponseEntity<List<OrderItem>> updateOrderList(
            @PathVariable Integer userId,
            @RequestBody @Valid CreateOrderRequest createdOrderRequest) {
        orderService.updateOrderItem(userId, createdOrderRequest);

        List<OrderItem> orderItemList = orderService.getOrderItemList(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderItemList);
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

        //取得orderItem總數
        Integer count = orderService.countOrderItem(orderItemQueryParams);

        Page<OrderItem> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(count);
        page.setResults(orderList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    //shopCart(handleDelete)
    //刪除orderList
    @DeleteMapping("/deleteOrderList/{userId}/{productId}")
    public ResponseEntity<List<OrderItem>> deleteOrderList(
            @PathVariable Integer userId,
            @PathVariable Integer productId){
        orderService.deleteOrderItemByProductId(productId);
        List<OrderItem> orderItemList = orderService.getOrderItemList(userId);
        return ResponseEntity.status(HttpStatus.OK).body(orderItemList);
    }

}
