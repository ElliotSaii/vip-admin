package com.techguy.repository;
import com.techguy.entity.Member;
import com.techguy.entity.MemberBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MemberBankRepository extends JpaRepository<MemberBank,Long> {
    MemberBank findByMemberIdAndTypeAndId(Long memberId,Integer type,Long bankId);

    List<MemberBank> findAllByMemberId(Long memberId);

    MemberBank findByIdAndMemberIdAndType(Long bankId,Long memberId,Integer type);

}
