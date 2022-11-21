package com.techguy.service.impl;

import com.techguy.entity.IncomeRecord;
import com.techguy.repository.IncomeRecordRepository;
import com.techguy.service.IncomeRecordService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IncomeRecordServiceImpl implements IncomeRecordService {
    private final IncomeRecordRepository incomeRecordRepository;

    @Override
    public void save(IncomeRecord incomeRecord) {
        incomeRecordRepository.save(incomeRecord);
    }

    @Override
    public Page<IncomeRecord> findByMemberId(Long memberId, Pageable page) {
        return incomeRecordRepository.findByMemberId(memberId,page);
    }
}
