package com.techguy.controller;

import com.techguy.entity.IncomeRecord;
import com.techguy.entity.Member;
import com.techguy.entity.RewardRecord;
import com.techguy.response.MessageResult;
import com.techguy.service.IncomeRecordService;
import com.techguy.service.MemberService;
import com.techguy.service.ProductRecordService;
import com.techguy.service.RewardRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/income")
public class IncomeRecordController {
    private final IncomeRecordService incomeRecordService;
    private final ProductRecordService productRecordService;
    private final MemberService memberService;
    private final RewardRecordService rewardRecordService;

    @Autowired
    public IncomeRecordController(IncomeRecordService incomeRecordService, ProductRecordService productRecordService, MemberService memberService, RewardRecordService rewardRecordService){
        this.incomeRecordService = incomeRecordService;
        this.productRecordService = productRecordService;
        this.memberService = memberService;
        this.rewardRecordService = rewardRecordService;
    }
    @GetMapping("/list")
    public MessageResult<?> list(@RequestParam("memberId")Long memberId,@RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize){
        Pageable page = PageRequest.of(pageNo,pageSize, Sort.by("createTime").descending());

        MessageResult<?> result = new MessageResult<>();
        memberService.findByMemberId(memberId);
       Page<IncomeRecord> incomeRecordPage= incomeRecordService.findByMemberId(memberId,page);

        List<IncomeRecord> incomeRecordList = incomeRecordPage.getContent();
        if(incomeRecordList.size()>0){
            result.success("Income List");
            result.setResult(incomeRecordList);
            return  result;
        }
        return result;

    }
    @GetMapping("/reward/list")
    public MessageResult<?> rewards(@RequestParam("memberId")Long memberId ,@RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize){
        Pageable page =PageRequest.of(pageNo,pageSize,Sort.by("creatTime").descending());
        MessageResult<?> result =new MessageResult<>();

       Page<RewardRecord> rewardRecordPage = rewardRecordService.findByMemberId(memberId,page);
        List<RewardRecord> rewardRecordList = rewardRecordPage.getContent();

        if(rewardRecordList.size()>0){
            result.success("Reward list");
            result.setResult(rewardRecordList);
            return result;
        }else {
            result.error500("Operation failed");
            return  result;
        }
    }
}
