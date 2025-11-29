package com.sam.springbootmall.service.impl;

import com.sam.springbootmall.constant.ProductCategory;
import com.sam.springbootmall.dao.ProductDao;
import com.sam.springbootmall.dto.ProductRequest;
import com.sam.springbootmall.model.Product;
import com.sam.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class productServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public List<Product> getProducts(ProductCategory category, String search) {
        return productDao.getProducts(category,  search);
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
}
