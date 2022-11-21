package com.techguy.service.impl;

import com.techguy.entity.RewardRecord;
import com.techguy.repository.RewardRecordRepository;
import com.techguy.service.RewardRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RewardRecordServiceImpl implements RewardRecordService {
    private final RewardRecordRepository rewardRecordRepository;

    public RewardRecordServiceImpl(RewardRecordRepository rewardRecordRepository) {
        this.rewardRecordRepository = rewardRecordRepository;
    }

    @Override
    public void save(RewardRecord rewardRecord) {
        rewardRecordRepository.save(rewardRecord);
    }

    @Override
    public Page<RewardRecord> findByMemberId(Long memberId, Pageable pageable) {
        return rewardRecordRepository.findByMemberId(memberId,pageable);
    }
}
