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
    void deleteOrderItem(Integer productId);
    Order getOrderByOrderId(Integer orderId);
    List<OrderItem> getOrderItemListByUserId(Integer userId);
    List<OrderItem> getOrderItemListByUserId(OrderItemQueryParams orderItemQueryParams);
    Integer countOrder(OrderQueryParams orderQueryParams);
    Integer countOrderItem(OrderItemQueryParams orderItemQueryParams);
    List<Order> getOrders(OrderQueryParams orderQueryParams);
}
