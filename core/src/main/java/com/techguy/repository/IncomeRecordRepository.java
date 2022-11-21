package com.techguy.repository;

import com.techguy.entity.IncomeRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRecordRepository extends PagingAndSortingRepository<IncomeRecord,Long> {

    Page<IncomeRecord> findByMemberId(Long memberId, Pageable pageable);
}
