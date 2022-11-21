package com.techguy.controller;
import com.techguy.entity.Member;
import com.techguy.entity.MemberBank;
import com.techguy.entity.Withdraw;
import com.techguy.entity.WithdrawRecord;
import com.techguy.response.MessageResult;
import com.techguy.service.MemberBankService;
import com.techguy.service.MemberService;
import com.techguy.service.WithdrawRecordService;
import com.techguy.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/member/bank")
public class MemberBankController {
    private final MemberService memberService;
    private final MemberBankService memberBankService;
    private final WithdrawService withdrawService;
    private final WithdrawRecordService withdrawRecordService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberBankController(MemberService memberService, MemberBankService memberBankService, WithdrawService withdrawService, WithdrawRecordService withdrawRecordService, PasswordEncoder passwordEncoder){

        this.memberService = memberService;
        this.memberBankService = memberBankService;
        this.withdrawService = withdrawService;
        this.withdrawRecordService = withdrawRecordService;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/bind")
    public MessageResult<?> binding(@RequestParam("memberId")Long memberId,@RequestParam("type")Integer type,
                                     @RequestParam(value = "alipayAccName",required = false)String alipayAccName,@RequestParam(value = "alipayAccNo",required = false)String alipayAccNo,
                                     @RequestParam(value = "bankAccName",required = false)String bankAccName,@RequestParam(value = "bankAccNo",required = false)String bankAccNo,
                                     @RequestParam(value = "bankName",required = false)String bankName,@RequestParam(value = "bankBranchName",required = false)String bankBranchName){
        MessageResult<?> result = new MessageResult<>();
        Member member = memberService.findByMemberId(memberId);
        if(member!=null){
            MemberBank memberBank = new MemberBank();
            if(type==1){
                memberBank.setType(1);
                memberBank.setMemberId(memberId);
                memberBank.setAlipayAccName(alipayAccName);
                memberBank.setAlipayAccNo(alipayAccNo);
                memberBank.setCreateTime(new Date());
                memberBankService.save(memberBank);
                result.success("Binding alipay success!");
                result.setResult(memberBank);
                return result;
            }else if(type==2){
                memberBank.setType(2);
                memberBank.setMemberId(memberId);
                memberBank.setBankAccName(bankAccName);
                memberBank.setBankAccNo(bankAccNo);
                memberBank.setBankName(bankName);
                memberBank.setBankBranchName(bankBranchName);
                memberBank.setCreateTime(new Date());
                memberBankService.save(memberBank);
                result.success("Binding bank success!");
                result.setResult(memberBank);
                return result;
            }
        }
        return result;
    }



    @RequestMapping("/update")
    public MessageResult<?> update(@RequestParam("memberId")Long memberId,@RequestParam("type")Integer type,@RequestParam("bankId")Long bankId,
                                    @RequestParam(value = "alipayAccName",required = false)String alipayAccName,@RequestParam(value = "alipayAccNo",required = false)String alipayAccNo,
                                    @RequestParam(value = "bankAccName",required = false)String bankAccName,@RequestParam(value = "bankAccNo",required = false)String bankAccNo,
                                    @RequestParam(value = "bankName",required = false)String bankName,@RequestParam(value = "bankBranchName",required = false)String bankBranchName) {
        MessageResult<?> result = new MessageResult<>();
        Member member = memberService.findByMemberId(memberId);

        if (member != null) {
            MemberBank memberBank = memberBankService.findByMemberId(memberId, type, bankId);

            MemberBank bank = new MemberBank();
            if (memberBank != null) {
                if (type == 1) {
                    bank.setId(bankId);
                    bank.setType(memberBank.getType());
                    bank.setAlipayAccName(alipayAccName);
                    bank.setAlipayAccNo(alipayAccNo);
                    MemberBank updateBank = memberBankService.update(bank);
                    result.success("Update alipay success");
                    result.setResult(updateBank);
                    return result;

                } else if (type == 2) {
                    bank.setId(bankId);
                    bank.setType(memberBank.getType());
                    bank.setBankAccName(bankAccName);
                    bank.setBankAccNo(bankAccNo);
                    bank.setBankName(bankName);
                    bank.setBankBranchName(bankBranchName);
                    MemberBank updateBank = memberBankService.update(bank);
                    result.success("Update bank success");
                    result.setResult(updateBank);
                    return result;
                }
            }
        }
        return result;
    }

    @RequestMapping(value = "/delete")
    public MessageResult<?> delete(@RequestParam("bankId")Long bankId){
        MessageResult<?> result = new MessageResult<>();
        MemberBank memberBank =  memberBankService.findByBankId(bankId);
       if(memberBank!=null){
           memberBankService.deleteById(bankId);
           result.success("Deleted bank");
           return result;
       }
       return result;
    }

    @GetMapping("/list")
    public MessageResult<?> list(@RequestParam("memberId")Long memberId){
        MessageResult<?> result =new MessageResult<>();
        memberService.findByMemberId(memberId);
       List<MemberBank> memberBankList= memberBankService.findAllByMemberId(memberId);

       if(memberBankList.size()>0){
           result.success("Member Banks List");
           result.setResult(memberBankList);
           return result;
       }else {
           result.error500("Not binding yet");
           return result;
       }
    }

    @PostMapping("/withdraw")
    public MessageResult<?> withdraw(@RequestParam("memberId")Long memberId,@RequestParam("amount")String amount,@RequestParam("type")Integer type,@RequestParam("bankId")Long bankId,
                                     @RequestParam("fundPassword")String fundPassword,@RequestParam("remark")String remark){


        Member member = memberService.findByMemberId(memberId);
        MemberBank memberBank = memberBankService.findByBank(bankId,memberId,type);
        String withdrawId ="101";
        Withdraw withdraw = withdrawService.findById(Long.parseLong(withdrawId));

        MessageResult<?> result = new MessageResult<>();

        if(member!=null && memberBank!=null){

            if(!passwordEncoder.matches(fundPassword,member.getFundPassword())){
                result.error500("Password not matches");
                return result;
            }

            String cnyBalance = member.getCnyBalance();
            BigDecimal memBalance;
            if(cnyBalance==null || cnyBalance.isEmpty()){
                memBalance = new BigDecimal("0.00");
            }
            else {
                memBalance = new BigDecimal(cnyBalance);
            }
            BigDecimal withdrawAmount = new BigDecimal(amount);
            //todo define by admin
            BigDecimal minAmount = new BigDecimal(withdraw.getMinAmount());
            BigDecimal maxAmount = new BigDecimal(withdraw.getMaxAmount());
            BigDecimal hundred = new BigDecimal("100");
            BigDecimal fee = new BigDecimal(withdraw.getFee());


            if(lessThan(memBalance,withdrawAmount)){
                result.error500("Balance is not enough!");
                return result;
            }else if(lessThan(withdrawAmount,minAmount)){
                result.error500("Min amount 100!");
                return result;
            }else if (!lessThan(withdrawAmount,maxAmount)){
                result.error500("Max amount 900!");
                return result;
            }
            MathContext mc = new MathContext(9);
            BigDecimal realFee = (withdrawAmount.divide(hundred,mc)).multiply(fee);


            memBalance = memBalance.subtract(withdrawAmount);
            member.setCnyBalance(String.valueOf(memBalance));
            //actual withdraw amount
            withdrawAmount = withdrawAmount.subtract(realFee);
            if(type==1) {
                //update member balance
                memberService.update(member);
                WithdrawRecord withdrawRecord = new WithdrawRecord();
                withdrawRecord.setAmount(amount);
                withdrawRecord.setRemark(remark);
                withdrawRecord.setActualAmount(String.valueOf(withdrawAmount));
                withdrawRecord.setMemberId(member.getId());
                withdrawRecord.setStatus(1);
                withdrawRecord.setType(type);
                withdrawRecord.setFee(String.valueOf(realFee));
                withdrawRecord.setAlipayAccName(memberBank.getAlipayAccName());
                withdrawRecord.setAlipayAccNo(memberBank.getAlipayAccNo());
                withdrawRecord.setCreateTime(new Date());
                WithdrawRecord saveWithdrawRecord = withdrawRecordService.save(withdrawRecord);

                result.success("Operation success");
                result.setResult(saveWithdrawRecord);
                return result;
            }
            if(type==2) {
                memberService.update(member);
                WithdrawRecord withdrawRecord = new WithdrawRecord();
                withdrawRecord.setAmount(amount);
                withdrawRecord.setRemark(remark);
                withdrawRecord.setActualAmount(String.valueOf(withdrawAmount));
                withdrawRecord.setMemberId(member.getId());
                withdrawRecord.setStatus(1);
                withdrawRecord.setType(type);
                withdrawRecord.setFee(String.valueOf(realFee));
                withdrawRecord.setBankAccName(memberBank.getBankAccName());
                withdrawRecord.setBankAccNo(memberBank.getBankAccNo());
                withdrawRecord.setBankName(memberBank.getBankName());
                withdrawRecord.setBankBranchName(memberBank.getBankBranchName());
                withdrawRecord.setCreateTime(new Date());

                WithdrawRecord saveWithdrawRecord = withdrawRecordService.save(withdrawRecord);
                result.success("Operation success");
                result.setResult(saveWithdrawRecord);
                return result;
            }
            return result;
        }
        else {
            result.error500("Operation failed");
            return result;
        }
    }

    @GetMapping("/transfer/rate")
    public MessageResult<?> fee(){
        MessageResult<?> result =new MessageResult<>();
        String withdrawId ="101";
        Withdraw withdraw = withdrawService.findById(Long.parseLong(withdrawId));
        if(withdraw!=null){
            result.success("");
            result.setResult(withdraw);
            return  result;
        }
        return result;
    }

    public static boolean lessThan(BigDecimal left, BigDecimal right) {
        return left.compareTo(right) < 0;
    }
}
