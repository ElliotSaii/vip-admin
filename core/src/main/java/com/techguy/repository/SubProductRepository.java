package com.techguy.repository;

import com.techguy.entity.product.SubProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubProductRepository extends JpaRepository<SubProduct,Long> {

 Page<SubProduct> findByProductId(Long productId, Pageable page);

 List<SubProduct> findByProductId(Long productId);

 Page<SubProduct> findAllByProductId(Long productId,Pageable pageable);

}
