package com.techguy.repository;

import com.techguy.entity.WithdrawRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawRecordRepository extends PagingAndSortingRepository<WithdrawRecord,Long> {

    Page<WithdrawRecord> findAllByStatus(Pageable pageable,Integer status);

    Page<WithdrawRecord> findAllByMemberId(Long memberId,Pageable pageable);
}
