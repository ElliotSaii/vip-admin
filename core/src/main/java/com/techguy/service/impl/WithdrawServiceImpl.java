package com.techguy.service.impl;

import com.techguy.entity.Withdraw;
import com.techguy.repository.WithdrawRepository;
import com.techguy.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WithdrawServiceImpl implements WithdrawService {
    private  final WithdrawRepository withdrawRepository;
    @Autowired
    public WithdrawServiceImpl(WithdrawRepository withdrawRepository){
        this.withdrawRepository=withdrawRepository;
    }

    @Override
    public void save(Withdraw withdraw) {
        withdrawRepository.save(withdraw);
    }

    @Override
    public Withdraw findById(Long withdrawId) {
        return withdrawRepository.findById(withdrawId).orElseThrow(()->new IllegalArgumentException(
                String.format("Given id %s not found",withdrawId)
        ));
    }

    @Override
    public Withdraw  update (Withdraw withdraw) {
        Withdraw withdraw1 = withdrawRepository.findById(withdraw.getId()).orElseThrow(() -> new IllegalArgumentException(
                String.format("Given id %s not found", withdraw.getId())
        ));
        withdraw1.setMinAmount(withdraw.getMinAmount());
        withdraw1.setMaxAmount(withdraw.getMaxAmount());
        withdraw1.setFee(withdraw.getFee());

    return withdrawRepository.save(withdraw1);
    }

    @Override
    public Withdraw viewFee() {
      return withdrawRepository.findById(101L).orElseThrow(()-> new IllegalArgumentException(
                 String.format("Given id %s not found",101)
         ));
    }
}
