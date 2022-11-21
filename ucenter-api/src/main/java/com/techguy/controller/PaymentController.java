package com.techguy.controller;

import com.techguy.constant.CommonConstant;
import com.techguy.entity.Bank;
import com.techguy.entity.Member;
import com.techguy.entity.Payment;
import com.techguy.entity.product.Product;
import com.techguy.entity.product.ProductRecord;
import com.techguy.response.MessageResult;
import com.techguy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/payment")

public class PaymentController {
    private final MemberService memberService;
    private final BankService bankService;
    private final ProductService productService;
    private final PaymentServie paymentService;
    private final ProductRecordService productRecordService;
    private final SubProductRecordService subProductRecordService;

    @Autowired
    public PaymentController(MemberService memberService, BankService bankService, ProductService productService, PaymentServie paymentService, ProductRecordService productRecordService, SubProductRecordService subProductRecordService){
        this.memberService = memberService;
        this.bankService = bankService;
        this.productService = productService;
        this.paymentService = paymentService;
        this.productRecordService = productRecordService;
        this.subProductRecordService = subProductRecordService;
    }

    @PostMapping("/submit")
    public MessageResult<?> submit(@RequestParam("productId")Long productId, @RequestParam("memberId")Long memberId, @RequestParam("bankId")Long bankId,
                                   @RequestParam("voucher")String voucher){
        MessageResult<?> result = new MessageResult<>();
        Member member = memberService.findByMemberId(memberId);
        Product product = productService.findByProductId(productId);
        ProductRecord productRecord = productRecordService.findByProductId(productId,memberId);
        Bank bank = bankService.findById(bankId);

        if(bank ==null) {
            result.error500("Bank is not valid!");
            return result;
        }

            if (productRecord != null) {
                if (Objects.equals(productRecord.getProductId(), productId) && productRecord.getBuyStatus() == 3) {
                    // save in product record
                    productRecord.setBuyAmount(product.getBuyAmount());
                    productRecord.setProductId(product.getId());
                    productRecord.setCreateTime(new Date());
                    productRecord.setMemberId(member.getId());
                    productRecord.setName(product.getName());
                    productRecord.setBuyStatus(1);
                    productRecord.setProductType(1);
                    productRecord.setImageUrl(voucher);
                    productRecord.setTotalUnitPrice(product.getTotalUnitPrice());

                    productRecordService.update(productRecord);

                    Payment payment = new Payment();
                    payment.setProductId(productId);
                    payment.setMemberId(member.getId());
                    payment.setAccountName(bank.getAccountName());
                    payment.setAccountNo(bank.getAccountNo());
                    payment.setBranchName(bank.getBranchName());

                    payment.setBuyAmount(product.getBuyAmount());
                    payment.setTotalUnitPrice(product.getTotalUnitPrice());
                    payment.setVoucher(voucher);
                    Payment savePayment = paymentService.save(payment);
                    result.setSuccess(true);
                    result.setMessage("Paid Success");
                    result.setCode(CommonConstant.OK_200);
                    result.setResult(savePayment);
                } else {
                    result.error500("Product already bought");
                    return result;
                }
                return result;
            }
            else {
                ProductRecord record = new ProductRecord();
                record.setBuyAmount(product.getBuyAmount());
                record.setProductId(product.getId());
                record.setCreateTime(new Date());
                record.setMemberId(member.getId());
                record.setName(product.getName());
                record.setBuyStatus(1);
                record.setProductType(1);
                record.setImageUrl(voucher);
                record.setTotalUnitPrice(product.getTotalUnitPrice());
                productRecordService.save(record);
                Payment payment = new Payment();
                payment.setProductId(productId);
                payment.setMemberId(member.getId());
                payment.setAccountName(bank.getAccountName());
                payment.setAccountNo(bank.getAccountNo());
                payment.setBranchName(bank.getBranchName());
                payment.setBuyAmount(product.getBuyAmount());
                payment.setTotalUnitPrice(product.getTotalUnitPrice());
                payment.setVoucher(voucher);
                Payment savePayment = paymentService.save(payment);
                result.setSuccess(true);
                result.setMessage("Paid Success");
                result.setCode(CommonConstant.OK_200);
                result.setResult(savePayment);
                return result;
            }
    }
    @GetMapping("/history")
    public MessageResult<?> pending(@RequestParam("memberId")Long memberId,@RequestParam("status")Integer status,
                                    @RequestParam(value = "pageNo",defaultValue = "0")Integer pageNo,@RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize) {

        Pageable page = PageRequest.of(pageNo,pageSize,Sort.by("createTime").descending());

        MessageResult<ProductRecord> result = new MessageResult<>();
        List<ProductRecord> productRecordList = productRecordService.findProductRecordByMemId(memberId,page);

        List<ProductRecord> records = new ArrayList<>();

        if(!(productRecordList.size() >0)){
            result.error500("No pending");
            return result;
        }
       for( ProductRecord record: productRecordList){
           if (Objects.equals(record.getBuyStatus(), status)) {
               records.add(record);
           }
         }
        result.setSuccess(true);
        result.setCode(CommonConstant.OK_200);
        result.setMessage("Pending Record");
        result.setResult(records);
        return result;

    }


}
