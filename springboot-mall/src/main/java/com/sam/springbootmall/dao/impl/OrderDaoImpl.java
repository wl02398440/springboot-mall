package com.sam.springbootmall.dao.impl;


import com.sam.springbootmall.dao.OrderDao;
import com.sam.springbootmall.dto.OrderItemQueryParams;
import com.sam.springbootmall.dto.OrderQueryParams;
import com.sam.springbootmall.model.Order;
import com.sam.springbootmall.model.OrderItem;
import com.sam.springbootmall.rowmapper.OrderItemRowMapper;
import com.sam.springbootmall.rowmapper.OrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    //----------------------------------------------order-------------------------------------------

    //創建order
    @Override
    public Integer createOrder(Integer userId,String userName, Integer totalAmount) {
        String sql = "insert into orders(user_id,user_name,total_amount,created_date,last_modified_date) " +
                "values (:userId,:userName,:totalAmount,:createdDate,:lastModifiedDate)";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("userName", userName);
        map.put("totalAmount", totalAmount);
        Date date = new Date();
        map.put("lastModifiedDate", date);
        map.put("createdDate", date);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        Integer orderId = keyHolder.getKey().intValue();

        return orderId;
    }

    //update訂單狀態(付款後)
    @Override
    public void updateOrder(Integer orderId) {
        String sql = "update orders set status = '已付款'," +
                " last_modified_date = :lastModifiedDate" +
                " where order_id = :orderId;";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        Date date = new Date();
        map.put("lastModifiedDate", date);
        namedParameterJdbcTemplate.update(sql, map);
    }

    //update訂單狀態(取消、出貨)
    @Override
    public void updateOrder(Integer orderId, String status) {
        String sql = "update orders set status = :status," +
                " last_modified_date = :lastModifiedDate" +
                " where order_id = :orderId;";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        switch (status) {
            case ("cancel"):
                status = "已取消";
                break;
            case ("shipped"):
                status = "已出貨";
                break;
        }
        map.put("status", status);
        Date date = new Date();
        map.put("lastModifiedDate", date);
        namedParameterJdbcTemplate.update(sql, map);
    }

    //取得order資訊
    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        String sql = "select order_id, user_id, user_name, total_amount, created_date," +
                " last_modified_date, status from orders where 1 = 1";
        Map<String, Object> map = new HashMap<>();
        //查詢條件
        sql = addFilteringSql(orderQueryParams, map, sql);
        //排序
        sql = sql + " order by order_id asc";
        //分頁
        sql = sql + " limit :limit offset :offset";
        map.put("limit", orderQueryParams.getLimit());
        map.put("offset", orderQueryParams.getOffset());

        List<Order> orders = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

        if (orders.isEmpty()) {
            return null;
        }
        return orders;
    }

    //取得order資訊by orderId
    @Override
    public Order getOrderByOrderId(Integer orderId) {
        String sql = "select order_id, user_id, user_name, total_amount, created_date, last_modified_date" +
                ", status from orders where order_id = :orderId";
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

        if(orderList.isEmpty()){
            return null;
        }
        return orderList.get(0);
    }

    //-----------------------------------------------orderItem-------------------------------

    //創建OrderItem
    @Override
    public void createOrderItem(Integer userId, OrderItem orderItem) {
        String sql = "insert into order_item(user_id, product_id, count, amount)" +
                "values (:userId,:productId,:count,:amount)";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("productId", orderItem.getProductId());
        map.put("count", orderItem.getCount());
        map.put("amount", orderItem.getAmount());
        namedParameterJdbcTemplate.update(sql, map);
    }

    //update OrderItem(mall)
    @Override
    public void updateOrderItemByMall(Integer userId, OrderItem orderItem) {
        String sql = "update order_item set count = count + :count" +
                ", amount = amount + :amount where product_id = :productId" +
                " and user_id = :userId;";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("productId", orderItem.getProductId());
        map.put("count", orderItem.getCount());
        map.put("amount", orderItem.getAmount());
        namedParameterJdbcTemplate.update(sql, map);
    }

    //update OrderItem(shopCart)
    @Override
    public void updateOrderItemByShopCart(Integer userId, OrderItem orderItem) {
        String sql = "update order_item set count = :count , amount = :amount " +
                "where product_id = :productId and user_id = :userId;";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("productId", orderItem.getProductId());
        map.put("count", orderItem.getCount());
        map.put("amount", orderItem.getAmount());
        namedParameterJdbcTemplate.update(sql, map);
    }

    //更改購物車的orderId(創建訂單時)
    @Override
    public void updateOrderItemOrderId(Integer orderId, Integer productId) {
        String sql = "update order_item set order_id = :orderId" +
                " where product_id = :productId;";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("productId", productId);
        namedParameterJdbcTemplate.update(sql, map);
    }

    //查詢排序分頁orderItemList
    @Override
    public List<OrderItem> getOrderItemListByUserId(OrderItemQueryParams orderItemQueryParams) {
        String sql = "select oi.order_item_id, oi.user_id, oi.product_id, oi.count, oi.amount, oi.order_id," +
                " p.product_name, p.image_url, p.price, p.stock from order_item as oi" +
                " join product as p on p.product_id = oi.product_id " +
                "where oi.user_id = :userId";
        Map<String, Object> map = new HashMap<>();
        //查詢條件
        sql = addFilteringSql(orderItemQueryParams, map, sql);
        //排序
        sql = sql + " order by oi.product_id desc";
        //分頁
        sql = sql + " limit :limit offset :offset";
        map.put("limit", orderItemQueryParams.getLimit());
        map.put("offset", orderItemQueryParams.getOffset());
        map.put("userId", orderItemQueryParams.getUserId());
        List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper());

        return orderItemList;
    }

    //查詢orderItemList by userId
    @Override
    public List<OrderItem> getOrderItemListByUserId(Integer userId) {
        String sql = "select oi.order_item_id, oi.user_id, oi.product_id, oi.count, oi.amount, oi.order_id," +
                " p.product_name, p.image_url, p.price, p.stock from order_item as oi" +
                " join product as p on p.product_id = oi.product_id " +
                "where oi.user_id = :userId";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper());

        return orderItemList;
    }

    //查詢orderItemList by orderId
    @Override
    public List<OrderItem> getOrderItemListByOrderId(Integer orderId) {
        String sql = "select oi.order_item_id, oi.user_id, oi.product_id, oi.count, oi.amount, oi.order_id," +
                " p.product_name, p.image_url, p.price, p.stock from order_item as oi" +
                " join product as p on p.product_id = oi.product_id" +
                " where oi.order_id = :orderId";
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper());

        return orderItemList;
    }

    //delete OrderItem by productId
    @Override
    public void deleteOrderItemByProductId(Integer productId) {
        String sql = "delete from order_item where product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        namedParameterJdbcTemplate.update(sql, map);
    }

    //delete OrderItem by orderId
    @Override
    public void deleteOrderItemByOrderId(Integer orderId) {
        String sql = "delete from order_item where order_id = :orderId";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        namedParameterJdbcTemplate.update(sql, map);
    }

    //--------------------------------------------buyItem-------------------------------


    // 創建BuyItem
    @Override
    public void createBuyItem(Integer orderId) {
        String sql = "insert into buy_item  select * from order_item " +
                "where order_id = :orderId;";
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        namedParameterJdbcTemplate.update(sql, map);
    }

    // 取得BuyItem數據
    @Override
    public List<OrderItem> getBuyItemListByOrderId(Integer orderId) {
        String sql = "select bi.order_item_id, bi.user_id, bi.product_id, bi.count, bi.amount, bi.order_id," +
                " p.product_name, p.image_url, p.price, p.stock from buy_item as bi" +
                " join product as p on p.product_id = bi.product_id" +
                " where bi.order_id = :orderId";
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        List<OrderItem> buyItemList = namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper());
        return buyItemList;
    }

    //------------------------------------------util------------------------------------------

    //order總數
    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        String sql = "select count(*) from orders where 1 = 1";
        Map<String, Object> map = new HashMap<>();

        sql = addFilteringSql(orderQueryParams, map, sql);
        return namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
    }

    //orderItem總數
    @Override
    public Integer countOrderItem(OrderItemQueryParams orderItemQueryParams) {
        String sql = "select count(*) from order_item where 1 = 1";
        Map<String, Object> map = new HashMap<>();

        sql = addFilteringSql(orderItemQueryParams, map, sql);
        return namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
    }

    //查詢條件
    private String addFilteringSql(OrderQueryParams orderQueryParams, Map<String, Object> map, String sql) {
        if (orderQueryParams.getUserId() != null) {
            sql = sql + " and user_id = :userId";
            map.put("userId", orderQueryParams.getUserId());
        }

        return sql;
    }

//    @Override
//    public void createOrderItemList(Integer userId, List<OrderItem> orderItemList) {
//        String sql = "insert into order_item(user_id, product_id, count, amount)" +
//                "values (:userId,:productId,:count,:amount)";
//
//        MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[orderItemList.size()];
//        for (int i = 0; i < orderItemList.size(); i++) {
//            OrderItem orderItem = orderItemList.get(i);
//            parameterSources[i] = new MapSqlParameterSource();
//            parameterSources[i].addValue("userId", userId);
//            parameterSources[i].addValue("productId", orderItem.getProductId());
//            parameterSources[i].addValue("count", orderItem.getCount());
//            parameterSources[i].addValue("amount", orderItem.getAmount());
//
//        }
//        namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);
//
//    }
}
