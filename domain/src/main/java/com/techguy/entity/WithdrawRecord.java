package com.techguy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class WithdrawRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long memberId;
    private String amount;
    private String actualAmount;
    private String fee;
    private String remark;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Integer type;
    private Integer status;
    private String alipayAccName;
    private String alipayAccNo;
    private String bankAccName;
    private String bankAccNo;
    private String bankName;
    private String bankBranchName;

    //failed reason
    private String reason;

    private String oneTimePassword;
}
