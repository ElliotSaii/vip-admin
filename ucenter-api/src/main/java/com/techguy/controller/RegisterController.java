package com.techguy.controller;

import com.techguy.code.OnlyCodeUtils;
import com.techguy.config.LocaleMessageSourceService;
import com.techguy.constant.CommonConstant;
import com.techguy.constant.ErrorConstantMsg;
import com.techguy.constant.SysConstant;
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
    private final LocaleMessageSourceService messageSourceService;

    @PostMapping(value = "/register1")
    public MessageResult<Member> register(@RequestParam(value = "email")String email,@RequestParam(value = "code")String code,@RequestParam(value = "password")String password,@RequestParam(value = "invCode",required = false)String upId){
        MessageResult<Member> result = new MessageResult<>();
        boolean valid = ValidateUtil.validate(email);
//      String realKey = MD5Util.MD5Encode(code , "utf-8");

      Object checkCode = redisTemplate.opsForValue().get(SysConstant.EMAIL_BIND_CODE_PREFIX+email);
        code = code.toLowerCase();
      //check code
      if(checkCode==null || checkCode.equals("") || !code.equals(checkCode)) {
          result.error500(messageSourceService.getMessage("VERIFY_CODE_WRONG"));
          result.setResult(null);
          return result;
      }
      if(!valid){
          result.error500(messageSourceService.getMessage("ENTER_VALID_EMAIL"));
          return result;
      }
      if (!(password.length() >=5)){
          result.error500(messageSourceService.getMessage("PASSWORD_LENGTH"));
          return result;
      }
      Member newMember = memberService.findByEmail(email);

        if (newMember != null) {
          result.error500(messageSourceService.getMessage("MAIL_ALREADY_EXIT"));
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

                        result.success(messageSourceService.getMessage("REGISTER_SUCCESS"));
                        result.setResult(mem);
                        log.info("Member Register {}", mem.getEmail());
                        return result;
                    } else {
                        result.error500("OPERATION_FAIL");
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

                    result.success(messageSourceService.getMessage("REGISTER_SUCCESS"));
                    result.setResult(mem);
                    log.info("Member Register {}", mem.getEmail());
                    return result;
                }
            }
            return result;
        }
  }
}
