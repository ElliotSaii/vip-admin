package com.techguy.service;

import com.techguy.entity.product.SubProductRecord;

import java.util.List;


public interface SubProductRecordService {
    SubProductRecord save(SubProductRecord record);

    List<SubProductRecord> findByProductId(Long productId, Long memberId);

}
