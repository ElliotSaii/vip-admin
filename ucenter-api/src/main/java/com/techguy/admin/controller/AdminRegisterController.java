package com.techguy.admin.controller;

import com.techguy.constant.CommonConstant;
import com.techguy.constant.ErrorConstantMsg;
import com.techguy.entity.Member;
import com.techguy.entity.admin.Admin;
import com.techguy.response.MessageResult;
import com.techguy.service.AdminService;
import com.techguy.service.MemberService;
import com.techguy.utils.MD5Util;
import com.techguy.utils.ValidateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/admin/api")
@CrossOrigin(origins = {"http://154.39.248.73:8818","http://localhost:8080"})
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Slf4j
@AllArgsConstructor
public class AdminRegisterController {
    private final RedisTemplate redisTemplate;
    private PasswordEncoder passwordEncoder;
    private AdminService adminService;

    private static final String  secrectkey= "ima34rw3r3wrsdfsefesfd324324324*&^$%w#";

    @PostMapping("/register")
    public MessageResult<Admin> register(@RequestParam(value = "email")String email, @RequestParam(value = "code")String code, @RequestParam(value = "password")String password) {

        MessageResult<Admin> result = new MessageResult<>();
        boolean valid = ValidateUtil.validate(email);

        // check code
        code = code.toLowerCase();
        String realKey = MD5Util.MD5Encode(code, "utf-8");
        Object checkCode = redisTemplate.opsForValue().get(realKey);
        //check code
        if (checkCode == null || checkCode.equals("") || !code.equals(checkCode)) {
            result.setMessage(ErrorConstantMsg.VERIFY_CODE_WRONG);
            result.setCode(CommonConstant.INTERNAL_SERVER_ERROR_500);
            result.setResult(null);
            return result;
        }
        if (!valid) {
            result.setSuccess(false);
            result.setMessage("Enter valid email");
            result.setResult(null);
            return result;
        }
        if (!(password.length() >= 5)) {
            result.setSuccess(false);
            result.setMessage("password must have 6 character at least");
            result.setResult(null);
            return result;
        }
        Admin admin = adminService.findByEmail(email);

        if (admin != null) {
            result.setSuccess(false);
            result.setMessage("Email already exits");
            result.setResult(null);
            return result;
        } else {
            String encodePW = passwordEncoder.encode(password);
            String verySecrectKey = passwordEncoder.encode(secrectkey);
            Admin admin1 = adminService.register(encodePW, email,verySecrectKey);

            if (admin1 != null) {
                redisTemplate.delete(checkCode);
                result.setCode(CommonConstant.OK_200);
                result.setMessage("Registered success!");
                result.setSuccess(true);
                result.setResult(admin1);
                log.info("Admin Register {}", admin1.getEmail());
                return result;
            }

            return result;
        }
    }
}
