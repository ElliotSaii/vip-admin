package com.techguy.vo;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class RegisterVo {

    private String email;
    @NotNull
    private String code;
    @NotNull
    private String password;
    private String key;
    private String invCode;
}
