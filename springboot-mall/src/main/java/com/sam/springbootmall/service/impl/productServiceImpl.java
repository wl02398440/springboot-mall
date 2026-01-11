package com.sam.springbootmall.service.impl;

import com.sam.springbootmall.dao.OrderDao;
import com.sam.springbootmall.dao.ProductDao;
import com.sam.springbootmall.dto.ProductQueryParams;
import com.sam.springbootmall.dto.ProductRequest;
import com.sam.springbootmall.model.OrderItem;
import com.sam.springbootmall.model.Product;
import com.sam.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class productServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        return productDao.countProduct(productQueryParams);
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        return productDao.getProducts(productQueryParams);
    }

    @Override
    public Product getProductById(Integer productId) {
        return productDao.getProductById(productId);
    }

    public Integer createProduct(ProductRequest productRequest) {
        return productDao.createProduct(productRequest);
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        productDao.updateProduct(productId, productRequest);
    }

    @Override
    public void deleteProduct(Integer productId) {
        productDao.deleteProduct(productId);
    }

    //訂單付款調整庫存
    @Override
    public void updateStock(Integer orderId) {

        List<OrderItem> orderItemList =
                orderDao.getOrderItemListByOrderId(orderId);

        for (OrderItem orderItem : orderItemList) {
            Integer productId = orderItem.getProductId();
            Product product = productDao.getProductById(productId);
            Integer count = product.getStock() - orderItem.getCount();
            productDao.updateStock(productId, count);
        }
    }
}


