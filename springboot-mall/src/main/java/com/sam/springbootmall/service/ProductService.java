package com.sam.springbootmall.service;

import com.sam.springbootmall.dto.ProductQueryParams;
import com.sam.springbootmall.dto.ProductRequest;
import com.sam.springbootmall.model.Product;

import java.util.List;

public interface ProductService {

    Integer countProduct(ProductQueryParams productQueryParams);


    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProduct(Integer productId);

    List<Product> getProducts(ProductQueryParams productQueryParams);
}


