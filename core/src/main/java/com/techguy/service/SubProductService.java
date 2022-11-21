package com.techguy.service;

import com.techguy.entity.product.SubProduct;
import com.techguy.repository.SubProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface SubProductService  {
    SubProduct add(SubProduct subProduct);

    Page<SubProduct> findSubProductByProductId(Long id, Pageable page);

    List<SubProduct> findSubProductByProductId(Long id);

    SubProduct findBySubProductId(Long subProductId);

    SubProduct update(SubProduct subProduct);

    List<SubProduct> findByProductId(Long id);

    void delete(Long id);

    Page<SubProduct> findSub(Long productId, Pageable page);
}
