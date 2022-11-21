package com.techguy.service;

import com.techguy.entity.WithdrawRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WithdrawRecordService {
    WithdrawRecord save(WithdrawRecord withdrawRecord);

    Page<WithdrawRecord> findAll(Pageable page,Integer status);

    WithdrawRecord findById(Long recordId);

    WithdrawRecord update(WithdrawRecord withdrawRecord);

    Page<WithdrawRecord> findByMemberId(Long memberId, Pageable page);

}
