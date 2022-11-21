package com.techguy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "member")
public class Member implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long productId;
    private String username;
    @Email
    private String email;
//    @Pattern(regexp = "")
    private String phone;
    private String backAccount;
    private String sex;
    private String ustdBalance;
    private String cnyBalance;

    private String invCode;
    private String upId;
    @Column(nullable = false)
    private String password;
    private String areaCode;
    private String token;
    private String fundPassword;
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




    public Member(String username, String email, String phone, String backAccount, String sex, String ustdBalance,
                  String cnyBalance, String invCode, String upId, String password, String areaCode, String token, Date createTime, Date updateTime, String level) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.backAccount = backAccount;
        this.sex = sex;
        this.ustdBalance = ustdBalance;
        this.cnyBalance = cnyBalance;
        this.invCode = invCode;
        this.upId = upId;
        this.password = password;
        this.areaCode = areaCode;
        this.token = token;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Member member = (Member) o;
        return id != null && Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
