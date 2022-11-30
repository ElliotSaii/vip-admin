package com.techguy.service;

import com.techguy.entity.IncomeRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IncomeRecordService {

    void save(IncomeRecord incomeRecord);

    Page<IncomeRecord> findByMemberId(Long memberId, Pageable page);

    Page<IncomeRecord> findByName(Long memberId, String name, Pageable page);

    Page<IncomeRecord> findStardAndEnd(Long memberId, String startDate, String endDate, Pageable page);

    Page<IncomeRecord> findByDate(Long memberId, String startDate, Pageable page);

    Page<IncomeRecord> findStardAndEndAndName(Long memberId, String name, String startDate, String endDate, Pageable page);
}
