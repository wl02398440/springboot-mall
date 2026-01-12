package com.sam.springbootmall.service;

import com.sam.springbootmall.dto.CreateOrderRequest;
import com.sam.springbootmall.dto.OrderItemQueryParams;
import com.sam.springbootmall.dto.OrderQueryParams;
import com.sam.springbootmall.model.Order;
import com.sam.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderService {

    Integer createOrder(Integer userId,String userName, CreateOrderRequest createOrderRequest);
    void updateOrder(Integer orderId);
    void updateOrder(Integer orderId, String status);
    void createOrderItem(Integer userId, CreateOrderRequest createOrderRequest);
    void updateOrderItem(Integer userId, CreateOrderRequest createOrderRequest);
    void deleteOrderItemByProductId(Integer productId);
    void deleteOrderItemByOrderId(Integer orderId);
    Order getOrderByOrderId(Integer orderId, Integer userId);
    List<Order> getOrders(OrderQueryParams orderQueryParams);
    List<OrderItem> getOrderItemList(OrderItemQueryParams orderItemQueryParams);
    List<OrderItem> getOrderItemList(Integer userId);
    Integer countOrder(OrderQueryParams orderQueryParams);
    Integer countOrderItem(OrderItemQueryParams orderlistQueryParams);
}
