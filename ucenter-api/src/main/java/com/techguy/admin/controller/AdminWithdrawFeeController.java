package com.techguy.admin.controller;

import com.techguy.admin.vo.WithdrawFeeVo;
import com.techguy.entity.Withdraw;
import com.techguy.response.MessageResult;
import com.techguy.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/admin/api/withdraw")
@CrossOrigin(origins = {"http://154.39.248.73:8818","http://localhost:8080"})
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminWithdrawFeeController {

    private final WithdrawService withdrawService;

    @Autowired
    public AdminWithdrawFeeController(WithdrawService withdrawService){
        this.withdrawService = withdrawService;
    }

    @PostMapping("/add")
    public MessageResult<?> setFee(@RequestBody WithdrawFeeVo withdrawFeeVo){
        Withdraw withdraw = new Withdraw();
        withdraw.setId(withdrawFeeVo.getId());
        withdraw.setMinAmount(withdrawFeeVo.getMinAmount());
        withdraw.setMaxAmount(withdrawFeeVo.getMaxAmount());
        withdraw.setFee(withdrawFeeVo.getFee());
        withdraw.setCreateTime(new Date());

        withdrawService.save(withdraw);
        return new MessageResult<>().success("Operation success");
    }
    @PutMapping("/update")
    public MessageResult<?> update(@RequestParam("withdrawId")Long withdrawId,@RequestParam("minAmount")String minAmount,@RequestParam("maxAmount")String maxAmount,
                                   @RequestParam("fee")String fee){
        MessageResult<?> result =new MessageResult<>();
       Withdraw withdraw = withdrawService.findById(withdrawId);
       if(withdraw!=null){
           withdraw.setMinAmount(minAmount);
           withdraw.setMaxAmount(maxAmount);
           withdraw.setFee(fee);
           Withdraw withdrawUpdate = withdrawService.update(withdraw);
           result.success("Update success");
           result.setResult(withdrawUpdate);
           return  result;
       }
       return result;
    }
}
