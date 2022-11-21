
package com.techguy.service.impl;

import com.techguy.entity.product.ProductRecord;


import com.techguy.repository.ProductRecordRepository;
import com.techguy.service.ProductRecordService;
import org.apache.el.stream.StreamELResolverImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class ProductRecordServiceImpl implements ProductRecordService {
    private final ProductRecordRepository productRecordRepository;

    @Autowired
    public ProductRecordServiceImpl(ProductRecordRepository productRecordRepository){
        this.productRecordRepository = productRecordRepository;
    }
    @Override
    public ProductRecord save(ProductRecord productRecord) {
        return productRecordRepository.save(productRecord);
    }

    @Override
    public ProductRecord getProductRecord() {
        return null;
    }

    @Override
    public ProductRecord findByMemberIdAndProductId(Long memberId,Long productId,Integer type) {
        return productRecordRepository.findByMemberIdAndProductIdAndProductType(memberId,productId,type);
    }

//    @Override
//    public List<ProductRecord> findByMemberId(Long memberId) {
//        List<ProductRecord> productRecordList =  productRecordRepository.findByMemberId(memberId);
//
//        return productRecordList;
//
//
//    }

    @Override
    public ProductRecord findProductRecordByMemberId(Long memberId) {
        return productRecordRepository.findByMemberId(memberId);
    }

    @Override
    public ProductRecord findByProductId(Long productId) {
        List<ProductRecord> productRecordList = (List<ProductRecord>) productRecordRepository.findAll();
        for(ProductRecord record:productRecordList){
            if (Objects.equals(record.getProductId(), productId)) {
                return record;
            }
        }
        return null;


    }

    @Override
    public ProductRecord findByProductId(Long productId, Long memberId) {
        return productRecordRepository.findDistinctByProductIdAndMemberId(productId,memberId);
    }

    @Override
    public List<ProductRecord> findProductRecordByMemId(Long memberId, Pageable page) {
        return productRecordRepository.findByMemberId(memberId,page);
    }

    @Override
    public List<ProductRecord> findAll() {
        return (List<ProductRecord>) productRecordRepository.findAll(Sort.by(Sort.Order.desc("create_time")));
    }

    @Override
    public ProductRecord findSubProductInRecordById(Long subProductId) {
        return  productRecordRepository.findBySubProductId(subProductId);

    }

    @Override
    public List<ProductRecord> findSubProductRecordList(Long productId) {
        return productRecordRepository.findByProductId(productId);
    }

    @Override
    public ProductRecord findSubProductByProductIdAndSubId(Long productId, Long subProductId) {
        return productRecordRepository.findByProductIdAndSubProductId(productId,subProductId);
    }

    @Override
    public List<ProductRecord> findByProductIdAndMemberId(Long productId, Long memberId) {
        return productRecordRepository.findByProductIdAndMemberId(productId,memberId);
    }

    @Override
    public ProductRecord update(ProductRecord productRecord) {
        ProductRecord record = productRecordRepository.findById(productRecord.getId()).orElseThrow(
                ()->new IllegalArgumentException(String.format("Product Record id %s",productRecord.getId()))
        );
        record.setProductId(productRecord.getProductId());
        record.setBuyStatus(productRecord.getBuyStatus());
        record.setUpdateTime(productRecord.getUpdateTime());
        record.setProductType(productRecord.getProductType());
        record.setName(productRecord.getName());
        record.setImageUrl(productRecord.getImageUrl());
        record.setTotalUnitPrice(productRecord.getTotalUnitPrice());
        record.setMemberId(productRecord.getMemberId());

        return productRecordRepository.save(record);
    }

    @Override
    public Page<ProductRecord> findAllPage(Pageable pageable) {
        return productRecordRepository.findAll(pageable);
    }

    @Override
    public Page<ProductRecord> findAllPage(Pageable pageable, Integer status) {
        return productRecordRepository.findAllByBuyStatus(pageable,status);
    }

    @Override
    public ProductRecord findById(Long productRecordId) {
        return productRecordRepository.findById(productRecordId).orElseThrow(()->
                new IllegalArgumentException(String.format("product record id %s not found",productRecordId)));
    }

    @Override
    public Page<ProductRecord> findProductByMemberId(Long memberId, Pageable page) {
        return productRecordRepository.findProductRecordByMemberId(memberId,page);
    }

//    @Override
//    public List<ProductRecord> findByName(String name) {
//        return productRecordRepository.findByNameContains(name);
//    }

    @Override
    public ProductRecord findByMemberIdAndSubProductId(Long memberId, Long subProductId) {
        return productRecordRepository.findByMemberIdAndSubProductId(memberId,subProductId);
    }

    @Override
    public ProductRecord findSubInRecord(Long subId,Long productId,Long memberId,Integer type) {
        return productRecordRepository.findBySubProductIdAndProductIdAndMemberIdAndProductType(subId,productId,memberId,type);
    }

    @Override
    public Page<ProductRecord> filterByName(String name,Pageable pageable) {
        return productRecordRepository.findByNameContains(name,pageable);
    }

}

