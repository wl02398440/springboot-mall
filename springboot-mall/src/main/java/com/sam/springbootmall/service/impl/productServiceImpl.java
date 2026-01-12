package com.sam.springbootmall.service.impl;

import com.sam.springbootmall.dao.OrderDao;
import com.sam.springbootmall.dao.ProductDao;
import com.sam.springbootmall.dto.ProductQueryParams;
import com.sam.springbootmall.dto.ProductRequest;
import com.sam.springbootmall.model.OrderItem;
import com.sam.springbootmall.model.Product;
import com.sam.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Transactional
@Service
public class productServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        return productDao.createProduct(productRequest);
    }

    //訂單付款調整庫存
    @Override
    public void updateStock(Integer orderId) {
        List<OrderItem> buyItemList =
                orderDao.getBuyItemListByOrderId(orderId);

        for (OrderItem buyItem : buyItemList) {
            //檢查庫存是否足夠
            if (buyItem.getCount() > buyItem.getStock()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        buyItem.getProductName() + "庫存剩" + buyItem.getStock() +"個");
            }
            productDao.updateStock(buyItem.getProductId(),
                    buyItem.getStock() - buyItem.getCount());
        }
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        productDao.updateProduct(productId, productRequest);
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        return productDao.getProducts(productQueryParams);
    }

    @Override
    public Product getProductById(Integer productId) {
        return productDao.getProductById(productId);
    }

    @Override
    public void deleteProduct(Integer productId) {
        productDao.deleteProduct(productId);
    }

    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        return productDao.countProduct(productQueryParams);
    }

}


