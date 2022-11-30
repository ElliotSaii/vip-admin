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

    @Override
    public Page<IncomeRecord> findByName(Long memberId, String name, Pageable page) {
        return incomeRecordRepository.findByMemberIdAndNameContaining(memberId,name,page);
    }

    @Override
    public Page<IncomeRecord> findStardAndEnd(Long memberId, String startDate, String endDate, Pageable page) {
        return incomeRecordRepository.findStartAndEndDate(memberId,startDate,endDate,page);
    }

    @Override
    public Page<IncomeRecord> findByDate(Long memberId, String startDate, Pageable page) {
        return incomeRecordRepository.findByMemberIdAndCreateTime(memberId,startDate,page);
    }

    @Override
    public Page<IncomeRecord> findStardAndEndAndName(Long memberId, String name, String startDate, String endDate, Pageable page) {
        return incomeRecordRepository.findStartAndEndDateAndName(memberId,name,startDate,endDate,page);
    }
}
