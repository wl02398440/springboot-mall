package com.sam.springbootmall.service.impl;


import com.sam.springbootmall.dao.OrderDao;
import com.sam.springbootmall.dao.ProductDao;
import com.sam.springbootmall.dao.UserDao;
import com.sam.springbootmall.dto.BuyItem;
import com.sam.springbootmall.dto.CreateOrderRequest;
import com.sam.springbootmall.dto.OrderItemQueryParams;
import com.sam.springbootmall.dto.OrderQueryParams;
import com.sam.springbootmall.model.Order;
import com.sam.springbootmall.model.OrderItem;
import com.sam.springbootmall.model.Product;
import com.sam.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    //------------------------------------------order--------------------------------------------

    //創建 order
    @Override
    public Integer createOrder(Integer userId,String userName, CreateOrderRequest createOrderRequest) {

        int totalAmount = 0;
        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());

            //計算總價
            int amount = product.getPrice() * buyItem.getCount();
            totalAmount += amount;
        }
        //創建訂單
        Integer orderId = orderDao.createOrder(userId, userName, totalAmount);

        //更改orderItem的orderId
        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());
            orderDao.updateOrderItemOrderId(orderId, product.getProductId());
        }
        //創建buy_item商品數據
        orderDao.createBuyItem(orderId);

        return orderId;

    }

    //update訂單狀態(未付款改為已付款)
    @Override
    public void updateOrder(Integer orderId) {
        orderDao.updateOrder(orderId);
    }

    //update訂單狀態(取消訂單、出貨)
    @Override
    public void updateOrder(Integer orderId, String status) {
        orderDao.updateOrder(orderId, status);
    }

    //取得order
    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        List<Order> orders = orderDao.getOrders(orderQueryParams);

        for (Order order : orders) {
            order.setOrderItems(orderDao.getBuyItemListByOrderId(order.getOrderId()));
        }
        return orders;
    }

    //取得order by orderId
    @Override
    public Order getOrderByOrderId(Integer orderId, Integer userId) {
        Order order = orderDao.getOrderByOrderId(orderId);

        List<OrderItem> orderItemList = orderDao.getOrderItemListByOrderId(orderId);
        order.setOrderItems(orderItemList);
        return order;
    }

    //----------------------------------------------orderItem-------------------------------

    //創建或更改OrderItem
    @Override
    public void createOrderItem(Integer userId, CreateOrderRequest createOrderRequest) {
        //取得購物資訊
        BuyItem buyItem = createOrderRequest.getBuyItemList().get(0);
        Product product = productDao.getProductById(buyItem.getProductId());

        //計算總價
        int amount = product.getPrice() * buyItem.getCount();
        //創建OrderItem存入購買商品資訊
        OrderItem orderItemUser = new OrderItem();
        orderItemUser.setProductId(buyItem.getProductId());
        orderItemUser.setCount(buyItem.getCount());
        orderItemUser.setAmount(amount);

        //判斷是否有同樣商品
        List<OrderItem> orderItemList = orderDao.getOrderItemListByUserId(userId);
        if (!orderItemList.isEmpty()) {
            for (OrderItem orderItem : orderItemList) {
                if (orderItem.getProductId().equals(orderItemUser.getProductId())
                        && orderItem.getUserId().equals(userId)) {
                    //更新商品訂單
                    orderDao.updateOrderItemByMall(userId, orderItemUser);
                    return;
                }
            }
            //插入商品訂單
            orderDao.createOrderItem(userId, orderItemUser);
        } else {
            orderDao.createOrderItem(userId, orderItemUser);
        }

    }

    //更改OrderItem(shopCart)
    @Override
    public void updateOrderItem(Integer userId, CreateOrderRequest createOrderRequest) {
        //取得更改資訊
        BuyItem buyItem = createOrderRequest.getBuyItemList().get(0);
        Product product = productDao.getProductById(buyItem.getProductId());
        //計算總價
        int amount = product.getPrice() * buyItem.getCount();
        //創建OrderItem存入購買商品資訊
        OrderItem orderItemUser = new OrderItem();
        orderItemUser.setProductId(buyItem.getProductId());
        orderItemUser.setCount(buyItem.getCount());
        orderItemUser.setAmount(amount);
        //更新商品訂單
        orderDao.updateOrderItemByShopCart(userId, orderItemUser);
    }

    //取得orderItemList
    @Override
    public List<OrderItem> getOrderItemList(OrderItemQueryParams orderItemQueryParams) {
        List<OrderItem> ordeItemrList = orderDao.getOrderItemListByUserId(orderItemQueryParams);

        return ordeItemrList;
    }

    //取得orderItemList by userId
    @Override
    public List<OrderItem> getOrderItemList(Integer userId) {
        List<OrderItem> orderItemList = orderDao.getOrderItemListByUserId(userId);
        return orderItemList;
    }

    //delete OrderItem by productId
    @Override
    public void deleteOrderItemByProductId(Integer productId) {
        orderDao.deleteOrderItemByProductId(productId);
    }

    //delete OrderItem by orderId
    @Override
    public void deleteOrderItemByOrderId(Integer orderId) {
        orderDao.deleteOrderItemByOrderId(orderId);
    }

    //------------------------------------------------------util-----------------------------------

    //計算order數量
    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        return orderDao.countOrder(orderQueryParams);
    }

    //計算orderList數量
    @Override
    public Integer countOrderItem(OrderItemQueryParams orderItemQueryParams) {
        return orderDao.countOrderItem(orderItemQueryParams);
    }

    //    //創建 order
//    @Transactional
//    @Override
//    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
//        User user = userDao.getUserById(userId);
//        if (user == null) {
//            log.warn("該userId {} 不存在", userId);
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//        }
//
//        int totalAmount = 0;
//        List<OrderItem> orderItemList = new ArrayList<>();
//
//        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
//            Product product = productDao.getProductById(buyItem.getProductId());
//            //檢查product庫存是否足夠
//            if (product == null) {
//                log.warn("商品 {} 不存在", buyItem.getProductId());
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//            }
//            if (product.getStock() < buyItem.getCount()) {
//                log.warn("商品 {} 庫存不足，庫存數量{}，欲購買數量{}",
//                        buyItem.getProductId(), product.getStock(), buyItem.getCount());
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//            }
//
//            //扣除商品庫存
//            productDao.updateStock(product.getProductId(), product.getStock() - buyItem.getCount());
//
//            //計算總價
//            int amount = product.getPrice() * buyItem.getCount();
//            totalAmount += amount;
//
//            OrderItem orderItem = new OrderItem();
//            orderItem.setProductId(buyItem.getProductId());
//            orderItem.setCount(buyItem.getCount());
//            orderItem.setAmount(amount);
//            orderItemList.add(orderItem);
//        }
//        //創建訂單
//        Integer orderId = orderDao.createOrder(userId, totalAmount);
//        orderDao.createOrderItemList(userId, orderItemList);
//        return orderId;
//
//    }

}
