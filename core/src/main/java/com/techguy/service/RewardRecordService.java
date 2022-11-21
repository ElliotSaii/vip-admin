package com.techguy.service;

import com.techguy.entity.RewardRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RewardRecordService {
    void save(RewardRecord rewardRecord);

    Page<RewardRecord> findByMemberId(Long memberId, Pageable page);
}
