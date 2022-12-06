package com.techguy.admin.controller;

import com.techguy.entity.Member;
import com.techguy.entity.WithdrawRecord;
import com.techguy.response.MessageResult;
import com.techguy.service.MemberService;
import com.techguy.service.WithdrawRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/api/withdraw")
@CrossOrigin(origins = {"http://154.39.248.73:8818","http://localhost:3000"})
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminWithdrawController {
    private final WithdrawRecordService withdrawRecordService;
    private final MemberService memberService;

    @Autowired
    public AdminWithdrawController(WithdrawRecordService withdrawRecordService, MemberService memberService){
        this.withdrawRecordService=withdrawRecordService;
        this.memberService = memberService;
    }
    @GetMapping("/list")
    public MessageResult<?> list(@RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize){
        MessageResult<?> result =new MessageResult<>();
        pageNo = (pageNo-1);
        Pageable page = PageRequest.of(pageNo,pageSize, Sort.by("createTime").descending());
        Map<String, Object> map =new HashMap<>();
       Integer status = 1;
      Page<WithdrawRecord> withdrawRecordPage= withdrawRecordService.findAll(page,status);
        List<WithdrawRecord> withdrawRecordList = withdrawRecordPage.getContent();
        long totalElements = withdrawRecordPage.getTotalElements();

        if(withdrawRecordList.size()>0){
            map.put("totalElements",totalElements);
            map.put("list",withdrawRecordList);
            result.success("Withdraw list");
            result.setResult(map);
            return result;
        }else {
            result.error500("No withdraw list");
            return result;
        }
    }

    @PutMapping("/auth")

    public MessageResult<?> authWithdrawStatus(@RequestParam("recordId")Long recordId,@RequestParam(value = "oneTimePassword",required = false)String oneTimePassword,
    @RequestParam("status")Integer status,@RequestParam(value = "reason",required = false)String reason){
        MessageResult<?> result = new MessageResult<>();
      WithdrawRecord  withdrawRecord = withdrawRecordService.findById(recordId);

      if(withdrawRecord!=null){
          withdrawRecord.setStatus(status);
          if(status==1){
              result.error500("Nothing change");
              return result;
          }
          else if(status==3 && !reason.isEmpty()) {
              withdrawRecord.setReason(reason);

              String amount = withdrawRecord.getAmount();
              Long memberId = withdrawRecord.getMemberId();

              Member member = memberService.findByMemberId(memberId);
              String cnyBalance = member.getCnyBalance();

              BigDecimal cnyAmount =null;
              if(cnyBalance==null){
                   cnyAmount =new BigDecimal("0.00");
              } else {
                  cnyAmount = new BigDecimal(cnyBalance);
              }
              BigDecimal returnAmount = new BigDecimal(amount);

              cnyAmount = cnyAmount.add(returnAmount);

              member.setCnyBalance(String.valueOf(cnyAmount));

              memberService.update(member);

              WithdrawRecord updateRecord =  withdrawRecordService.update(withdrawRecord);
              result.success("Authenticated status");
              return result;
          }else if(status==2 && withdrawRecord.getType()==1 && !oneTimePassword.isEmpty()){

              withdrawRecord.setReason(null);
              withdrawRecord.setOneTimePassword(oneTimePassword);
              withdrawRecordService.update(withdrawRecord);
            result.success("Authenticated status");
            return result;
          } else if(status==2 && withdrawRecord.getType()==2){
              withdrawRecordService.update(withdrawRecord);
              result.success("Authenticated status");
              return result;
          }
          return result;
      }else {
          result.error500("Operation failed");
          return result;
      }

    }
}
