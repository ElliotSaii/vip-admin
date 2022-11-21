package com.techguy.repository;

import com.techguy.entity.RewardRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardRecordRepository extends PagingAndSortingRepository<RewardRecord,Long> {

    Page<RewardRecord> findByMemberId(Long memberId, Pageable pageable);
}
