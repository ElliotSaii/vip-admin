package com.techguy.service.impl;

import com.techguy.entity.WithdrawRecord;
import com.techguy.repository.WithdrawRecordRepository;
import com.techguy.service.WithdrawRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WithdrawRecordServiceImpl implements WithdrawRecordService {
    private final WithdrawRecordRepository withdrawRecordRepository;

    @Autowired
    public WithdrawRecordServiceImpl(WithdrawRecordRepository withdrawRecordRepository) {
        this.withdrawRecordRepository = withdrawRecordRepository;
    }

    @Override
    public WithdrawRecord save(WithdrawRecord withdrawRecord) {
        return withdrawRecordRepository.save(withdrawRecord);
    }

    @Override
    public Page<WithdrawRecord> findAll(Pageable page,Integer status) {
        return withdrawRecordRepository.findAllByStatus(page,status);
    }

    @Override
    public WithdrawRecord findById(Long recordId) {
        return withdrawRecordRepository.findById(recordId).orElseThrow(()->new IllegalArgumentException(
                String.format("Withdraw Record id %s not found",recordId)
        ));
    }

    @Override
    public WithdrawRecord update(WithdrawRecord withdrawRecord) {
        WithdrawRecord withdrawRecord1 = withdrawRecordRepository.findById(withdrawRecord.getId()).orElseThrow(() -> new IllegalArgumentException(
                String.format("Given withdraw record id %s not found", withdrawRecord.getId())
        ));
        withdrawRecord1.setReason(withdrawRecord.getReason());
        withdrawRecord1.setStatus(withdrawRecord.getStatus());
        withdrawRecord1.setOneTimePassword(withdrawRecord.getOneTimePassword());
        return  withdrawRecordRepository.save(withdrawRecord1);
    }

    @Override
    public Page<WithdrawRecord> findByMemberId(Long memberId, Pageable page) {
        return withdrawRecordRepository.findAllByMemberId(memberId,page);
    }

    @Override
    public Page<WithdrawRecord> findByStartAndEnd(Long memberId, String startDate, String endDate, Integer status, Pageable page) {
        return withdrawRecordRepository.findBetweenStarAndEnd(memberId,startDate,endDate,status,page);
    }

    @Override
    public Page<WithdrawRecord> findByStartAndEnd(Long memberId, String startDate, String endDate, Pageable page) {
        return withdrawRecordRepository.findBetweenStarAndEnd(memberId,startDate,endDate,page);
    }

    @Override
    public Page<WithdrawRecord> findByDay(Long memberId, String day, Integer status, Pageable page) {
        return withdrawRecordRepository.findByDay(memberId,day,status,page);
    }

    @Override
    public Page<WithdrawRecord> findByStatus(Long memberId,Integer status, Pageable page) {
        return withdrawRecordRepository.findByMemberIdAndStatus(memberId,status,page);
    }
}
