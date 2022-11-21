package com.techguy.service.impl;

import com.techguy.entity.product.SubProductRecord;
import com.techguy.repository.SubProductRecordRepository;
import com.techguy.service.SubProductRecordService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SubProductRecordServiceImpl implements SubProductRecordService {
    private final SubProductRecordRepository subProductRecordRepository;
    @Override
    public SubProductRecord save(SubProductRecord record) {
        return subProductRecordRepository.save(record);
    }

    @Override
    public List<SubProductRecord> findByProductId(Long productId,Long memberId) {
        return subProductRecordRepository.findByProductIdAndMemberId(productId,memberId);
    }
}
