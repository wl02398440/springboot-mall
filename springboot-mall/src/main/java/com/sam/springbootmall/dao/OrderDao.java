package com.sam.springbootmall.dao;

import com.sam.springbootmall.dto.OrderItemQueryParams;
import com.sam.springbootmall.dto.OrderQueryParams;
import com.sam.springbootmall.model.Order;
import com.sam.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    Integer createOrder(Integer userId, Integer totalAmount);

    void createOrderItem(Integer userId, OrderItem orderItem);
    void updateOrderItemByMall(Integer userId, OrderItem orderItem);
    void updateOrderItemByShopCart(Integer userId, OrderItem orderItem);
    void deleteOrderItemByProductId(Integer productId);
    void deleteOrderItemByOrderId(Integer orderId);
    void updateOrderItemOrderId(Integer orderId, Integer productId);
    void updateOrder(Integer orderId);
    void updateOrder(Integer orderId, String status);
    void createBuyItem(Integer orderId);
    Order getOrderByOrderId(Integer orderId);
    List<OrderItem> getOrderItemListByUserId(Integer userId);
    List<OrderItem> getOrderItemListByUserId(OrderItemQueryParams orderItemQueryParams);
    List<OrderItem> getOrderItemListByOrderId(Integer orderId);
    List<OrderItem> getBuyItemListByOrderId(Integer orderId);
    Integer countOrder(OrderQueryParams orderQueryParams);
    Integer countOrderItem(OrderItemQueryParams orderItemQueryParams);
    List<Order> getOrders(OrderQueryParams orderQueryParams);
}
