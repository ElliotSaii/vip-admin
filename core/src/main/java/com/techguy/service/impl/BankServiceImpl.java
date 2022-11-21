package com.techguy.service.impl;

import com.techguy.entity.Bank;
import com.techguy.repository.BankRepository;
import com.techguy.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;

    @Autowired
    public BankServiceImpl(BankRepository bankRepository){
        this.bankRepository = bankRepository;
    }
    @Override
    public void save(Bank bank) {
        bankRepository.save(bank);
    }

    @Override
    public List<Bank> getBankList() {
        return bankRepository.findAll();
    }

    @Override
    public Bank findById(Long bankId) {

        return bankRepository.findById(bankId).orElseThrow(()->new IllegalArgumentException(
                String.format("Bank with id: %s ",bankId)
        ));}



    @Override
    public void update(Bank bank) {
        Bank bank1 =bankRepository.findById(bank.getId()).orElseThrow(()->new IllegalArgumentException(
                String.format("Bank id %s not found",bank.getId())
        ));
        bank1.setAccountName(bank.getAccountName());
        bank1.setAccountNo(bank.getAccountNo());
        bank1.setBranchName(bank.getBranchName());
        bank1.setName(bank.getName());
        bank1.setBanKVersion(bank.getBanKVersion());

        bankRepository.save(bank1);

    }
}
