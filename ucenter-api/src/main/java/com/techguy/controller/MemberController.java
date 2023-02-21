package com.techguy.controller;

import com.techguy.entity.Member;
import com.techguy.response.MessageResult;
import com.techguy.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    @Autowired
    public MemberController(MemberService memberService){
        this.memberService=memberService;
    }

    @GetMapping("/info")
    public MessageResult<?>info(@RequestParam("memberId")Long memberId){
        MessageResult<?> result =new MessageResult<>();
        Member member = memberService.findByMemberId(memberId);
        if(member!=null){
            member.setPassword(null);
            member.setPlainFundPassword(null);
            member.setRoles(null);
            result.success("User info");
            result.setResult(member);
        }
        return result;
    }
}
