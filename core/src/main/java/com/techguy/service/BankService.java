package com.techguy.service;

import com.techguy.entity.Bank;

import java.util.List;

public interface BankService {
    void save(Bank bank);

    List<Bank> getBankList();

    Bank findById(Long bankId);

    void update(Bank bank);
}
