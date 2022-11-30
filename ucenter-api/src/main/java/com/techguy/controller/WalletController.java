package com.techguy.controller;

import com.techguy.config.LocaleMessageSourceService;
import com.techguy.entity.WithdrawRecord;
import com.techguy.response.MessageResult;
import com.techguy.service.MemberService;
import com.techguy.service.WithdrawRecordService;
import com.techguy.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    private final MemberService memberService;
    private final WithdrawService withdrawService;
    private final WithdrawRecordService withdrawRecordService;
    private final LocaleMessageSourceService messageSourceService;

    @Autowired
    public WalletController(MemberService memberService, WithdrawService withdrawService, WithdrawRecordService withdrawRecordService, LocaleMessageSourceService messageSourceService){
        this.memberService= memberService;
        this.withdrawService = withdrawService;
        this.withdrawRecordService = withdrawRecordService;
        this.messageSourceService = messageSourceService;
    }

    @GetMapping("/withdraw/record")
    public MessageResult<?> record(@RequestParam("memberId")Long memberId,@RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize){
        MessageResult<?> result =new MessageResult<>();
        Pageable page = PageRequest.of(pageNo,pageSize, Sort.by("createTime").descending());
       Page<WithdrawRecord> withdrawRecordPage= withdrawRecordService.findByMemberId(memberId,page);
        List<WithdrawRecord> withdrawRecordList = withdrawRecordPage.getContent();

        if(withdrawRecordList.size()>0){
            result.success("Withdraw Record List");
            result.setResult(withdrawRecordList);
            return  result;
        }else {
            result.error500(messageSourceService.getMessage("NO_RESULT_FOUND"));
            return result;
        }

    }

}
