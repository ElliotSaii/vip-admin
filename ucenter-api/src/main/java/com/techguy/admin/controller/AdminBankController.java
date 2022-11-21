package com.techguy.admin.controller;

import com.techguy.admin.vo.BankVo;
import com.techguy.constant.CommonConstant;
import com.techguy.entity.Bank;
import com.techguy.response.MessageResult;
import com.techguy.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(value = "/admin/api/bank")
@CrossOrigin(origins = {"http://154.39.248.73:8818","http://localhost:8080"})
public class AdminBankController {

    private final BankService bankService;
    @Autowired
    public AdminBankController(BankService bankService){
        this.bankService = bankService;
    }
    @PostMapping(value = "/add")
    public MessageResult<Bank> add(@RequestBody BankVo bankVo){
        MessageResult<Bank> result = new MessageResult<>();
        Bank bank =new Bank();
        bank.setName(bankVo.getName());
        bank.setAccountName(bankVo.getAccountName());
        bank.setAccountNo(bankVo.getAccountNo());
        bank.setBranchName(bankVo.getBranchName());
        bank.setBanKVersion(bankVo.getBankVersion());
        bank.setCreateTime(new Date());

        bankService.save(bank);

       result.success("Bank added");
        return result;
    }

    @PutMapping("/update")
    public MessageResult<?> update(@RequestBody BankVo bankVo){
        MessageResult<?> result =new MessageResult<>();
        Bank bank = bankService.findById(bankVo.getId());
        if(bank!=null){
            bank.setName(bankVo.getName());
            bank.setBanKVersion(bankVo.getBankVersion());
            bank.setBranchName(bankVo.getBranchName());
            bank.setAccountNo(bankVo.getAccountNo());
            bank.setAccountName(bankVo.getAccountName());

            bankService.update(bank);
            result.success("Operation success");
            return result;
        }else {
            result.error500("Operation failed");
            return result;
        }

    }

    @GetMapping("/list")
    public MessageResult<?> list() {
        MessageResult<?> result = new MessageResult<>();
        List<Bank> bankList = bankService.getBankList();

        if (bankList.size() > 0) {
            result.success("Bank List");
            result.setResult(bankList);
            return result;
        } else {
            result.error500("No Bank available now");
            return result;
        }

    }

}
