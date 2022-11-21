package com.techguy.service.impl;
import com.techguy.entity.MemberBank;
import com.techguy.repository.MemberBankRepository;

import com.techguy.service.MemberBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberBankServiceImpl implements MemberBankService {
    private final MemberBankRepository memberBankRepository;

    @Autowired
    public MemberBankServiceImpl(MemberBankRepository memberBankRepository){
        this.memberBankRepository=memberBankRepository;
    }
    @Override
    public void save(MemberBank memberBank) {
        memberBankRepository.save(memberBank);
    }

    @Override
    public MemberBank findByMemberId(Long memberId, Integer type,Long bankId) {
        return memberBankRepository.findByMemberIdAndTypeAndId(memberId,type,bankId);
    }

    @Override
    public MemberBank update(MemberBank bank) {
        MemberBank memberBank = memberBankRepository.findById(bank.getId()).orElseThrow(() -> new IllegalArgumentException(
                String.format("Given bank id %s not found", bank.getId())
        ));
        memberBank.setBankAccName(bank.getBankAccName());
        memberBank.setBankName(bank.getBankName());
        memberBank.setBankAccNo(bank.getBankAccNo());
        memberBank.setBankBranchName(bank.getBankBranchName());
        memberBank.setAlipayAccName(bank.getAlipayAccName());
        memberBank.setAlipayAccNo(bank.getAlipayAccNo());
        memberBank.setType(bank.getType());

    return memberBankRepository.save(memberBank);
    }

    @Override
    public List<MemberBank> findAllByMemberId(Long memberId) {
        return memberBankRepository.findAllByMemberId(memberId);
    }

    @Override
    public MemberBank findByBankId(Long bankId) {
        return memberBankRepository.findById(bankId).orElseThrow(()->new IllegalArgumentException(
                String.format("Given bank id % not found",bankId)
        ));
    }

    @Override
    public void deleteById(Long bankId) {
        memberBankRepository.deleteById(bankId);
    }

    @Override
    public MemberBank findByBank(Long bankId,Long memberId, Integer type) {
        return memberBankRepository.findByIdAndMemberIdAndType(bankId,memberId,type);
    }
}
