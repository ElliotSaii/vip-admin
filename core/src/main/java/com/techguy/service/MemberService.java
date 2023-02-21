package com.techguy.service;


import com.techguy.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface MemberService extends UserDetailsService {
    Member register(Member member, String password, String email,String upId);

    Member findByEmail(String email);

    Member findPassword(String password, String email);

    void save(Member member);

    Member update(Member member);

    Member findByMemberId(Long memberId);

    Member findByToken(String token);

    Page<Member> findAll(Pageable page);

    Member findByInvCode(String invCode);


    Page<Member> searchName(String name, Pageable page);

    Page<Member> findRealNameRequest(Integer status, Pageable page);
}
