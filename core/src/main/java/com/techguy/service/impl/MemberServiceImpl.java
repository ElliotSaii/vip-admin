package com.techguy.service.impl;

import com.techguy.code.OnlyCodeUtils;
import com.techguy.entity.Member;
import com.techguy.repository.MemberRepository;
import com.techguy.role.Roles;
import com.techguy.security.SecurityUserDetails;
import com.techguy.service.MemberService;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService, Serializable {

    private final MemberRepository memberRepository;



    @Override
    public Member register(Member member,String password,String reqEmail,String upId) {
        Member saveMember=null;
        String uuid = OnlyCodeUtils.creatUUID();
        if(member==null){
                Member mem = new Member();
                mem.setCnyBalance("0");
                mem.setInvCode(uuid);
                mem.setEmail(reqEmail);
                mem.setPassword(password);
                mem.setUpId(upId);
                mem.setCreateTime(new Date());
                mem.setRoles(Roles.USER);
                saveMember = memberRepository.save(mem);
        }
        return saveMember;
    }

    @Override
    public Member findByEmail(String email) {
        try{
            return memberRepository.findByEmail(email);
        }catch (NullPointerException e){
            return null;
        }
    }
    @Override
    public Member findPassword(String password, String email){
        return memberRepository.findByPasswordAndEmail(password,email);
    }

    @Override
    public void save(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member update(Member member) {
        Member mem = memberRepository.findById(member.getId()).orElseThrow(()->new IllegalArgumentException("update failed"));
        Member appMember = null;
        if (mem !=null){
             mem.setUstdBalance(member.getUstdBalance());
             mem.setToken(member.getToken());
             mem.setPassword(member.getPassword());
             mem.setFundPassword(member.getFundPassword());
             mem.setPlainFundPassword(member.getPlainFundPassword());
             mem.setBuyStatus(member.getBuyStatus());
             mem.setRealNameStatus(member.getRealNameStatus());
             mem.setName(member.getName());
             mem.setCardNumber(member.getCardNumber());
             mem.setFront(member.getFront());
             mem.setBack(member.getBack());
             mem.setFace(member.getFace());
             mem.setHeadIcon(member.getHeadIcon());
             mem.setEmail(member.getEmail());
             mem.setUsername(member.getUsername());
             mem.setSex(member.getSex());
             mem.setCnyBalance(member.getCnyBalance());
            appMember = memberRepository.save(mem);
        }
        return appMember;
    }

    @Override
    public Member findByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(()->new IllegalArgumentException(
                String.format("member with id %s not found",memberId)
        ));
    }

    @Override
    public Member findByToken(String token) {
        return memberRepository.findByToken(token);
    }

    @Override
    public Page<Member> findAll(Pageable page) {
        return memberRepository.findAll(page);
    }

    @Override
    public Member findByInvCode(String invCode) {
        return memberRepository.findByInvCode(invCode);
    }



    @Override
    public Page<Member> searchName(String name, Pageable page) {
        return memberRepository.findByUsernameIsContaining(name,page);
    }

    @Override
    public Page<Member> findRealNameRequest(Integer status, Pageable page) {
        return memberRepository.findAllByRealNameStatusEquals(status,page);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);
        return new SecurityUserDetails(member.getId(),member.getEmail(),member.getPassword(),member.getRoles());
    }

}
