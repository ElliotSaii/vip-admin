package com.techguy.controller;

import com.techguy.code.OnlyCodeUtils;
import com.techguy.constant.CommonConstant;
import com.techguy.constant.ErrorConstantMsg;
import com.techguy.utils.MD5Util;
import com.techguy.vo.RegisterVo;
import com.techguy.entity.Member;
import com.techguy.response.MessageResult;
import com.techguy.service.MemberService;
import com.techguy.utils.ValidateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/member")
@AllArgsConstructor
@Slf4j
public class RegisterController {
    private final RedisTemplate redisTemplate;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(value = "/register1")
    public MessageResult<Member> register(@RequestParam(value = "email")String email,@RequestParam(value = "code")String code,@RequestParam(value = "password")String password,@RequestParam(value = "invCode",required = false)String upId){
        MessageResult<Member> result = new MessageResult<>();
        boolean valid = ValidateUtil.validate(email);

      code = code.toLowerCase();
      String realKey = MD5Util.MD5Encode(code , "utf-8");
      Object checkCode = redisTemplate.opsForValue().get(realKey);
      //check code
      if(checkCode==null || checkCode.equals("") || !code.equals(checkCode)) {
          result.setMessage(ErrorConstantMsg.VERIFY_CODE_WRONG);
          result.setCode(CommonConstant.INTERNAL_SERVER_ERROR_500);
          result.setResult(null);
          return result;
      }
      if(!valid){
          result.setSuccess(false);
          result.setMessage("Enter valid email");
          result.setResult(null);
          return result;
      }
      if (!(password.length() >=5)){
          result.setSuccess(false);
          result.setMessage("password must have 6 character at least");
          result.setResult(null);
          return result;
      }
      Member newMember = memberService.findByEmail(email);

        if (newMember != null) {
          result.setSuccess(false);
          result.setMessage("Email already exits");
          result.setResult(null);
          return result;
         }
        else {
            if (!upId.isEmpty() && upId!=null) {
                Member inviterCode = memberService.findByInvCode(upId);
                if (inviterCode != null) {
                    String encodePW = passwordEncoder.encode(password);
                    Member mem = memberService.register(newMember, encodePW, email, upId);
                    if (mem != null) {
                        redisTemplate.delete(checkCode);
                        result.setCode(CommonConstant.OK_200);
                        result.setMessage("Registered success!");
                        result.setSuccess(true);
                        result.setResult(mem);
                        log.info("Member Register {}", mem.getEmail());
                        return result;
                    } else {
                        result.error500("Failed to register");
                        return result;
                    }
                }else {
                    result.error500("Invite code wrong");
                    return result;
                }
            } else {
                String encodePW = passwordEncoder.encode(password);
                Member mem = memberService.register(newMember, encodePW, email, upId);
                if (mem != null) {
                    redisTemplate.delete(checkCode);
                    result.setCode(CommonConstant.OK_200);
                    result.setMessage("Registered success!");
                    result.setSuccess(true);
                    result.setResult(mem);
                    log.info("Member Register {}", mem.getEmail());
                    return result;
                }
            }
            return result;
        }
  }
}
