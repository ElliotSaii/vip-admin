package com.techguy.controller;

import com.techguy.config.LocaleMessageSourceService;
import com.techguy.entity.IncomeRecord;
import com.techguy.entity.WithdrawRecord;
import com.techguy.entity.product.ProductRecord;
import com.techguy.response.MessageResult;
import com.techguy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/filter/record")
public class FilterController {

    private final ProductRecordService productRecordService;
    private final MemberService memberService;
    private final WithdrawRecordService withdrawRecordService;
    private final IncomeRecordService incomeRecordService;
    private final LocaleMessageSourceService localeMessageSourceService;
   @Autowired
    public FilterController(ProductRecordService productRecordService, MemberService memberService, WithdrawRecordService withdrawRecordService, IncomeRecordService incomeRecordService, LocaleMessageSourceService localeMessageSourceService) {
        this.productRecordService = productRecordService;
        this.memberService = memberService;
       this.withdrawRecordService = withdrawRecordService;
       this.incomeRecordService = incomeRecordService;
       this.localeMessageSourceService = localeMessageSourceService;
   }

    @GetMapping("/product")
    public MessageResult<?> productRecord(@RequestParam("memberId")Long memberId,@RequestParam(value = "startDate",required = false)String startDate,
                                          @RequestParam(value = "endDate",required = false)String endDate,@RequestParam(value = "name",required = false)String name,
                                          @RequestParam("status")Integer status,
                                          @RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize){MessageResult<?>result =new MessageResult<>();
        Pageable page = PageRequest.of(pageNo,pageSize);
        Page<ProductRecord> productRecordPage = null;
        Map<String,Object> map =new HashMap<>();



        if(!startDate.isEmpty() && !endDate.isEmpty() && !name.isEmpty()){
            productRecordPage = productRecordService.findBetweenDateAndName(memberId,name,startDate,endDate,status,page);
        }else if(!startDate.isEmpty() && !endDate.isEmpty()){
            productRecordPage = productRecordService.findBetweenDate(memberId,startDate,endDate,status,page);
        }
       /* else  if(!startDate.isEmpty()){
            productRecordPage = productRecordService.findByDay(memberId,startDate,status,page);
        }*/
        else if(!name.isEmpty()){
            productRecordPage = productRecordService.findByName(memberId, name,status, page);
        }

        List<ProductRecord> productRecordList = productRecordPage.getContent();
        map.put("totalElements",productRecordPage.getTotalElements());
        if(productRecordList.size()>0){
            result.setSuccess(true);
            result.setMessage(localeMessageSourceService.getMessage("OPERATION_SUCCESS"));
            map.put("list",productRecordList);
            result.setResult(map);
            return result;
        }else {
            result.error500(localeMessageSourceService.getMessage("NO_RESULT_FOUND"));
            return result;
        }
    }
    @GetMapping("/withdraw")
    public MessageResult<?> withdrawRecord(@RequestParam("memberId")Long memberId,@RequestParam(value = "startDate",required = false)String startDate,
                                           @RequestParam(value = "endDate",required = false)String endDate,@RequestParam(value = "status",required = false)Integer status ,
                                           @RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize){
       MessageResult<?> result =new MessageResult<>();
       Map<String ,Object> map =new HashMap<>();
        Pageable page =PageRequest.of(pageNo,pageSize);
        Page<WithdrawRecord> withdrawRecordPage= null;

        if(!startDate.isEmpty() && !endDate.isEmpty() && !status.equals(0)){
            withdrawRecordPage = withdrawRecordService.findByStartAndEnd(memberId,startDate,endDate,status,page);
        }else if(startDate.isEmpty()&& endDate.isEmpty()&& !status.equals(0)){
            withdrawRecordPage = withdrawRecordService.findByStatus(memberId,status,page);
        }
        else if (status.equals(0)){
            withdrawRecordPage = withdrawRecordService.findByStartAndEnd(memberId,startDate,endDate,page);
        }
//        else  if(!startDate.isEmpty()){
//            withdrawRecordPage = withdrawRecordService.findByDay(memberId,startDate,status,page);
//        }


        List<WithdrawRecord> withdrawRecordList = withdrawRecordPage.getContent();
        if(withdrawRecordList.size()>0){
            result.success(localeMessageSourceService.getMessage("WITHDRAW_RECORDS"));
            map.put("list",withdrawRecordList);
            map.put("totalElements",withdrawRecordPage.getTotalElements());
            result.setResult(map);
            return result;

        }else {
            result.error500(localeMessageSourceService.getMessage("NO_RESULT_FOUND"));
            return result;
        }


    }
    @GetMapping("/income")
    public MessageResult<?> incomeRecord(@RequestParam("memberId")Long memberId ,@RequestParam(value = "name",required = false)String name,
                                         @RequestParam(value = "startDate",required = false)String startDate,@RequestParam(value = "endDate",required = false)String endDate,@RequestParam("pageNo")
                                         Integer pageNo,@RequestParam("pageSize")Integer pageSize){
       MessageResult<?> result =new MessageResult<>();
       Pageable page =PageRequest.of(pageNo,pageSize);
       Map<String,Object> map =new HashMap<>();
       Page<IncomeRecord> incomeRecordPages =null;

       if(!startDate.isEmpty() && !endDate.isEmpty() && !name.isEmpty()){
           incomeRecordPages = incomeRecordService.findStardAndEndAndName(memberId,name,startDate,endDate,page);
       }else if(!name.isEmpty()){
           incomeRecordPages= incomeRecordService.findByName(memberId,name,page);
       }
       else if(!startDate.isEmpty() && !endDate.isEmpty()){
           incomeRecordPages = incomeRecordService.findStardAndEnd(memberId,startDate,endDate,page);
       }



           List<IncomeRecord>  incomeRecordList = incomeRecordPages.getContent();
           if(incomeRecordList.size()>0){
               map.put("totalElements",incomeRecordPages.getTotalElements());
               map.put("list",incomeRecordList);
               result.success(localeMessageSourceService.getMessage("INCOME_RECORDS"));
               result.setResult(map);
               return result;
           }else {
               result.error500(localeMessageSourceService.getMessage("NO_RESULT_FOUND"));
               return result;
           }

    }

}
