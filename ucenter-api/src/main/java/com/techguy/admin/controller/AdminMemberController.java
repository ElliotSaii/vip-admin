package com.techguy.admin.controller;

import com.techguy.entity.Member;
import com.techguy.response.MessageResult;
import com.techguy.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/api")
@CrossOrigin(origins = {"http://154.39.248.73:8818","http://localhost:3000"})
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Slf4j
public class AdminMemberController {
    private final MemberService memberService;

    @Autowired
    public AdminMemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @GetMapping("/realName-request")
    public MessageResult<Member> realName(@RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize){
        pageNo = (pageNo-1);

        Integer status = 1;

        Pageable page = PageRequest.of(pageNo, pageSize, Sort.by("createTime").ascending());
        MessageResult<Member> result = new MessageResult<>();
        Map<String,Object> map = new HashMap<>();
        Page<Member> memberPage = memberService.findRealNameRequest(status,page);

        long totalElements = memberPage.getTotalElements();
        List<Member>memberList = memberPage.getContent();

        List<Member>memberListDto =new ArrayList<>();

        if ((memberList.size() >0)){

            for(Member member: memberList){
                member.setFundPassword(null);
                member.setPassword(null);
                member.setToken(null);
                member.setPlainFundPassword(null);

                memberListDto.add(member);
            }

          map.put("totalElements",totalElements);
          map.put("list",memberListDto);
          result.success("Real Name request members");
          result.setResult(map);
          return result;
        }
        else {
            result.error500("No realName request");
            return  result;
        }

    }

    @PostMapping("/realName/auth")
    public  MessageResult<?> authRealName(@RequestParam("memberId")Long memberId,@RequestParam("status")Integer status){
        MessageResult<?> result =new MessageResult<>();
        Member member = memberService.findByMemberId(memberId);

        if(member!=null){
            if(status==3){

                member.setName(null);
                member.setUsername(null);
            }
            member.setRealNameStatus(status);
            memberService.update(member);
            result.success("Operation Success");
            return result;
        }
        return result;
    }

    @PutMapping("/editMember")
    public MessageResult<?> editMember(@RequestParam("memberId")Long memberId,@RequestParam("cnyBalance")String cnyBalance){
        MessageResult<?>result =new MessageResult<>();
        Member member = memberService.findByMemberId(memberId);
        if(member==null){
            result.error500("Operation Failed");
            return result;
        }
        log.trace("edit {}  original cny {} to => {}",member.getEmail(),member.getCnyBalance(),cnyBalance);
        member.setCnyBalance(cnyBalance);
        memberService.update(member);
        result.success("Operation Success");
        return result;
    }

    @GetMapping("/list")
    public MessageResult<?> list(@RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize,@RequestParam("name")String name){
        MessageResult<?> result=new MessageResult<>();
        Map<String,Object>map =new HashMap<>();
        pageNo=pageNo-1;
        Pageable page =PageRequest.of(pageNo,pageSize);
        Page<Member> memberPage=null;

        if(name!=null && !name.isEmpty()){
            memberPage = memberService.searchName(name,page);
        }else {
             memberPage = memberService.findAll(page);
        }

        List<Member> memberList = memberPage.getContent();
        long totalElements = memberPage.getTotalElements();
        List<Member> memberListDto =new ArrayList<>();
        if(memberList.size()>0){

            memberList.forEach(member -> {

                member.setPassword(null);
                member.setToken(null);
                member.setFundPassword(null);

                memberListDto.add(member);
            });

            map.put("totalElements",totalElements);
            map.put("list",memberListDto);
            result.success("Member List");
            result.setResult(map);
            return result;
        }else {
            result.error500("Operation failed");
            return result;
        }
    }
}
