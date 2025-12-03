package com.sam.springbootmall.dao;

import com.sam.springbootmall.dto.ProductQueryParams;
import com.sam.springbootmall.dto.ProductRequest;
import com.sam.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {

    Integer countProduct(ProductQueryParams productQueryParams);

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProduct(Integer productId);

    List<Product> getProducts(ProductQueryParams productQueryParams);

    void updateStock(Integer productId, Integer stock);

}
