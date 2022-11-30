package com.techguy.service;

import com.techguy.entity.product.ProductRecord;
import lombok.Lombok;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface ProductRecordService {
    ProductRecord save(ProductRecord productRecord);

    ProductRecord getProductRecord();

    ProductRecord findByMemberIdAndProductId(Long memberId,Long productId,Integer type);

//    List<ProductRecord> findByMemberId(Long memberId);?

    ProductRecord findProductRecordByMemberId(Long memberId);

    ProductRecord findByProductId(Long productId);


    ProductRecord findByProductId(Long productId,Long memberId);


    Page<ProductRecord> findProductRecordByMemId(Long memberId,Integer status, Pageable page);

    List<ProductRecord> findAll();

    ProductRecord findSubProductInRecordById(Long subProductId);

    List<ProductRecord> findSubProductRecordList(Long productId);

    ProductRecord findSubProductByProductIdAndSubId(Long productId, Long subProductId);

    List<ProductRecord> findByProductIdAndMemberId(Long productId, Long memberId);

    ProductRecord update(ProductRecord productRecord);

    Page<ProductRecord> findAllPage(Pageable pageable);

    Page<ProductRecord> findAllPage(Pageable pageable,Integer status);

    ProductRecord findById(Long productRecordId);

    Page<ProductRecord> findProductByMemberId(Long memberId, Pageable page);

//    List<ProductRecord> findByName(String name);

    ProductRecord findByMemberIdAndSubProductId(Long memberId, Long subProductId);

   ProductRecord findSubInRecord(Long subId, Long productId,Long memberId, Integer type);

    Page<ProductRecord> filterByName(String name,Pageable pageable);

    Page<ProductRecord> findBetweenDateAndName(Long memberId,String name, String startDate, String endDate,Integer status, Pageable page);

    Page<ProductRecord> findByDay(Long memberId,String start,Integer status, Pageable page);

    Page<ProductRecord> findByName(Long memberId, String name,Integer status, Pageable page);

    Page<ProductRecord> findBetweenDate(Long memberId, String startDate, String endDate, Integer status, Pageable page);
}
