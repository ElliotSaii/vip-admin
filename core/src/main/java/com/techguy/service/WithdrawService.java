package com.techguy.service;

import com.techguy.entity.Withdraw;

public interface WithdrawService {
    void save(Withdraw withdraw);

    Withdraw findById(Long withdrawId);

    Withdraw update(Withdraw withdraw);
}
