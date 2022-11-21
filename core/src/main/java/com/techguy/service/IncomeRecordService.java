package com.techguy.service;

import com.techguy.entity.IncomeRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IncomeRecordService {

    void save(IncomeRecord incomeRecord);

    Page<IncomeRecord> findByMemberId(Long memberId, Pageable page);
}
