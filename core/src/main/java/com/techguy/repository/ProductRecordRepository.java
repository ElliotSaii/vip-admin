package com.techguy.repository;

import com.techguy.entity.product.ProductRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRecordRepository extends PagingAndSortingRepository<ProductRecord,Long>{

    List<ProductRecord> findByMemberId(Long memberId, Pageable page);

    ProductRecord findBySubProductId(Long subProductId);

    List<ProductRecord> findByProductId(Long productId);

    ProductRecord findByProductIdAndSubProductId(Long productId, Long subProductId);

    List<ProductRecord> findByProductIdAndMemberId(Long productId, Long memberId);

//    List<ProductRecord> findByMemberId(Long memberId);

    ProductRecord findByMemberIdAndProductIdAndProductType(Long memberId,Long productId,Integer type);

    ProductRecord findByMemberId(Long memberId);

    Page<ProductRecord> findProductRecordByMemberId(Long memberId, Pageable page);

//    List<ProductRecord> findByNameContains(String name);

    ProductRecord findByMemberIdAndSubProductId(Long memberId,Long subId);

//    Page<ProductRecord> findProductRecordByBuyStatus(Pageable pageable,Integer status);

  ProductRecord findBySubProductIdAndProductIdAndMemberIdAndProductType(Long subId,Long productId,Long memberId,Integer type);

  ProductRecord findDistinctByProductIdAndMemberId(Long productId,Long memberId);

  Page<ProductRecord> findAllByBuyStatus(Pageable page,Integer status);

  Page<ProductRecord> findByNameContains(String name, Pageable pageable);
}
