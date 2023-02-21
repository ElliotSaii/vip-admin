package com.techguy.service;

import com.techguy.entity.MemberBank;

import java.util.List;

public interface MemberBankService {
    MemberBank save(MemberBank memberBank);

    MemberBank findByMemberId(Long memberId, Integer type,Long bankId);

    MemberBank update(MemberBank bank);

    List<MemberBank> findAllByMemberId(Long memberId);

    MemberBank findByBankId(Long bankId);

    void deleteById(Long bankId);

    MemberBank findByBank(Long bankId,Long memberId, Integer type);
}
