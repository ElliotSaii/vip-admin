package com.techguy.admin.controller;

import com.techguy.entity.IncomeRecord;
import com.techguy.entity.Member;
import com.techguy.entity.RewardRecord;
import com.techguy.entity.product.ProductRecord;
import com.techguy.response.MessageResult;
import com.techguy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/admin/api/order")
@CrossOrigin(origins = {"http://154.39.248.73:8818","http://localhost:8080"})
public class AdminOrderController {
    private final MemberService memberService;
    private final ProductService productService;
    private final ProductRecordService productRecordService;
    private final IncomeRecordService incomeRecordService;
    private final RewardRecordService rewardRecordService;

    @Autowired
    public AdminOrderController(MemberService memberService, ProductService productService, ProductRecordService productRecordService, IncomeRecordService incomeRecordService, RewardRecordService rewardRecordService){
        this.memberService=memberService;
        this.productService=productService;
        this.productRecordService=productRecordService;
        this.incomeRecordService = incomeRecordService;
        this.rewardRecordService = rewardRecordService;
    }

    @PutMapping("/auth")
    public MessageResult<?> authOrder(@RequestParam("status") Integer status, @RequestParam("productRecordId") Long productRecordId, @RequestParam("memberId") Long memberId) {
        MessageResult<?> result = new MessageResult<>();
        Member member = memberService.findByMemberId(memberId);
        ProductRecord productRecord = productRecordService.findById(productRecordId);

        if (productRecord == null || member==null || Objects.equals(productRecord.getBuyStatus(), status) ) {
            result.error500("Operation failed");
            return result;
        }
        if (productRecord.getProductType() == 1) {
            productRecord.setBuyStatus(status);
            productRecordService.update(productRecord);
            result.success("Operation success");
            return result;
        }

        if (productRecord.getProductType() == 2) {
            productRecord.setBuyStatus(status);
            BigDecimal unitPrice = productRecord.getUnitPrice();
            String cnyBalance = member.getCnyBalance();

            BigDecimal memberCny;
            if(cnyBalance==null || cnyBalance.isEmpty()){
                memberCny = new BigDecimal("0.00");
            }
            else {
                memberCny = new BigDecimal(cnyBalance);
            }
            //check upId if exit to 10% to him
            String invCode = member.getUpId();
            if(!invCode.isEmpty() && invCode!=null){
                Member inviter=  memberService.findByInvCode(invCode);
                if(inviter!=null){
                    String inviterCnyBalance = inviter.getCnyBalance();
                    BigDecimal inviterCny;
                    if(inviterCnyBalance==null){
                        inviterCny = new BigDecimal("0.00");
                    }else {
                        inviterCny = new BigDecimal(inviterCnyBalance);
                    }
                    BigDecimal tenPercent = new BigDecimal("10");
                    BigDecimal hurnderd = new BigDecimal("100");

                    tenPercent =tenPercent.divide(hurnderd);

                    BigDecimal inviterReward = unitPrice.multiply(tenPercent);

                    inviterCny = inviterCny.add(inviterReward);

                    inviter.setCnyBalance(inviterCny.toString());
                    memberService.update(inviter);

                    memberCny = memberCny.add(unitPrice);

                    member.setCnyBalance(String.valueOf(memberCny));
                    productRecordService.update(productRecord);
                    memberService.update(member);

                    IncomeRecord incomeRecord = new IncomeRecord();
                    incomeRecord.setMemberId(memberId);
                    incomeRecord.setAdminImgUrl(productRecord.getAdminImgUrl());
                    incomeRecord.setImgUrl(productRecord.getImageUrl());
                    incomeRecord.setName(productRecord.getName());
                    incomeRecord.setAmount(unitPrice);
                    incomeRecord.setFromImgUrl(productRecord.getFromImgUrl());
                    incomeRecord.setCreateTime(new Date());

                    incomeRecordService.save(incomeRecord);
                   //save reward record
                    RewardRecord rewardRecord =new RewardRecord();
                    rewardRecord.setMemberId(inviter.getId());
                    rewardRecord.setAmount(inviterReward);
                    rewardRecord.setFromUserName(member.getEmail());
                    rewardRecord.setInvCode(invCode);
                    rewardRecord.setCreatTime(new Date());

                    rewardRecordService.save(rewardRecord);

                    result.success("Operation success");
                    return result;
                }else {
                    result.error500("Operation failed");
                    return result;
                }

            }else {

                memberCny = memberCny.add(unitPrice);

                member.setCnyBalance(String.valueOf(memberCny));
                productRecordService.update(productRecord);
                memberService.update(member);

                IncomeRecord incomeRecord = new IncomeRecord();
                incomeRecord.setMemberId(memberId);
                incomeRecord.setAdminImgUrl(productRecord.getAdminImgUrl());
                incomeRecord.setImgUrl(productRecord.getImageUrl());
                incomeRecord.setName(productRecord.getName());
                incomeRecord.setAmount(unitPrice);
                incomeRecord.setFromImgUrl(productRecord.getFromImgUrl());
                incomeRecord.setCreateTime(new Date());

                incomeRecordService.save(incomeRecord);

                result.success("Operation success");
                return result;
            }
        }
        return result;
    }

/*
    @PostMapping("/search")
    public MessageResult<?> searchOrder(@RequestParam("name")String name){
        MessageResult<?> result = new MessageResult<>();


        List<ProductRecord> productRecordList = productRecordService.findByName(name);

        if(!(productRecordList.size() >0)){
            result.error500("Operation failed");
            return result;
        }
        result.success("Operation success");
        result.setResult(productRecordList);
        return result;
    }
*/

    @GetMapping("/filterStatus")
    public MessageResult<?> filter(@RequestParam("status")Integer status,@RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize){
       Map<String,Object> map =new HashMap<>();
       MessageResult<?> result =new MessageResult<>();
        pageNo = pageNo-1;
        Pageable page = PageRequest.of(pageNo,pageSize, Sort.by("createTime").descending());

        Page<ProductRecord> productRecordPage = productRecordService.findAllPage(page,status);

        List<ProductRecord> productRecordList = productRecordPage.getContent();
        long totalElements = productRecordPage.getTotalElements();
        map.put("totalElements",totalElements);
         if(productRecordList.size()>0){
             map.put("list",productRecordList);
             result.success("Operation success");
             result.setResult(map);
             return result;
         }else {
             result.error500("Operation failed");
             return result;
         }

    }
}
