package com.techguy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class MemberDTO {

    private Long id;
    private Long productId;
    private String username;

    private String email;
    private String phone;
    private String backAccount;
    private String sex;
    private String cnyBalance;

    private String invCode;
    private String upId;


    private String token;

    //0:not set 1: pending 2:set
    private Integer realNameStatus=0;
    private Integer buyStatus;

    private String name;
    private String cardNumber;
    private String front;
    private String back;
    private String face;


    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String level;
    private String headIcon;




}
