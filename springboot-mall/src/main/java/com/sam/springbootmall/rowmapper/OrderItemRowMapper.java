package com.sam.springbootmall.rowmapper;

import com.sam.springbootmall.model.OrderItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemRowMapper implements RowMapper<OrderItem> {

    @Override
    public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderItem orderItem = new OrderItem();
//order_item表
        orderItem.setOrderItemId(rs.getInt("order_item_id"));
        orderItem.setUserId(rs.getInt("user_id"));
        orderItem.setProductId(rs.getInt("product_id"));
        orderItem.setCount(rs.getInt("count"));
        orderItem.setAmount(rs.getInt("amount"));
//product表
        orderItem.setProductName(rs.getString("product_name"));
        orderItem.setImageUrl(rs.getString("image_url"));
        orderItem.setPrice(rs.getInt("price"));
        orderItem.setStock(rs.getInt("stock"));

        return orderItem;
    }
}
