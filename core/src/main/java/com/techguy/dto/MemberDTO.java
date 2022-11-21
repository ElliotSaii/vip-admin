package com.techguy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import java.util.Date;

@Data
@AllArgsConstructor
public class MemberDTO {
    private String id;
    private String email;
    private String password;
    private String username;

    //    @Pattern(regexp = "")
    private String phone;
    private String backAccount;
    private String sex;
    private String ustdBalance;
    private String cnyBalance;

    private String invCode;
    private String upId;
    private String areaCode;
    private String token;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String level;

    public MemberDTO(String id,String email,String password){
        this.id = id;
        this.email =email;
        this.password = password;
    }

}
