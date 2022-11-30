package com.techguy.service;

import com.techguy.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


public interface ProductService {
    List<Product> getProductList();

    Product findByMemberId(Long memberId);

    Product add(Product product);

    Product findByProductId(Long productId);

    Product update(Product product);

//    Page<Product> findAll(Pageable pageable,Long memberId);

    Page<Product> findAll(Pageable pageable);

    Page<Product> findProductBetweenStartTimeAndEndTime(String currentTime, Pageable pageable);




    List<Product> findAll();


    void delete(Long id);

    Page<Product> searchByName(String name, Pageable page);
}
