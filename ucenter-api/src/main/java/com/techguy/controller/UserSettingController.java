package com.techguy.controller;

import cn.hutool.core.util.ObjectUtil;
import com.techguy.config.LocaleMessageSourceService;
import com.techguy.constant.CommonConstant;
import com.techguy.constant.SysConstant;
import com.techguy.entity.Member;
import com.techguy.response.MessageResult;
import com.techguy.service.MemberService;
import com.techguy.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/user/setting")
public class UserSettingController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String,Object> redisTemplate;
    private final LocaleMessageSourceService messageSourceService;

   @Autowired
   public UserSettingController(MemberService memberService, PasswordEncoder passwordEncoder, RedisTemplate<String, Object> redisTemplate, LocaleMessageSourceService messageSourceService){
       this.memberService = memberService;
       this.passwordEncoder = passwordEncoder;
       this.redisTemplate = redisTemplate;
       this.messageSourceService = messageSourceService;
   }
    @PostMapping(value = "/realName")
    public MessageResult<Member> setRealName(@RequestParam("memberId") Long memberId,@RequestParam("name")String name,@RequestParam("cardNumber")String cardNumber,
                                             @RequestParam("front") String front,@RequestParam("back")String back,@RequestParam("face")String face){
        MessageResult<Member> result = new MessageResult<>();
        Member member = memberService.findByMemberId(memberId);

        if (member != null) {
            Integer realNameStatus = member.getRealNameStatus();
            if(realNameStatus==2){
                result.error500(messageSourceService.getMessage("REALNAME_AUTHENTICATED"));
                return result;
            }


            member.setRealNameStatus(1);
            member.setName(name);
            member.setCardNumber(cardNumber);
            member.setUsername(name);
            member.setFront(front);
            member.setBack(back);
            member.setFace(face);

            Member appMember = memberService.update(member);

            if (appMember != null) {

                result.setSuccess(true);
                result.setMessage(messageSourceService.getMessage("PENDING_REQ"));
                result.setResult(appMember.getRealNameStatus());
                return  result;
            }
            else {
                result.error500(messageSourceService.getMessage("OPERATION_FAIL"));
                return result;
            }
        }
       return  result;
    }
     //Generate transaction password
    @PostMapping(value = "/fund-password")
    public MessageResult<?> setFundPassword(@RequestParam("memberId")Long memberId,@RequestParam("password")String password){
        MessageResult<?> result = new MessageResult<>();

        Member member = memberService.findByMemberId(memberId);
        if (member != null && member.getFundPassword()==null) {
            if (password.length()>5){
                String fundPW =passwordEncoder.encode(password);
                member.setFundPassword(fundPW);
                member.setPlainFundPassword(password);
                Member appMember = memberService.update(member);




                result.setSuccess(true);
                result.setCode(CommonConstant.OK_200);
                result.setMessage(messageSourceService.getMessage("GENERATED_FUN_PSW"));
               //result.setResult(appMember);
                return result;
            }else {
                result.setSuccess(false);
                result.setCode(500);
                result.setMessage(messageSourceService.getMessage("PASSWORD_LENGTH"));
                result.setResult(null);
                return  result;
            }
        }
        else {
            result.error500(messageSourceService.getMessage("OPERATION_FAIL"));

        }
        return result;

    }
    @GetMapping(value = "/realNameStatus")
    public MessageResult<Member> getRealName(@RequestParam("memberId")Long memberId){
       MessageResult<Member> result = new MessageResult<>();
        Member member = memberService.findByMemberId(memberId);

        if (member != null) {
            result.success(messageSourceService.getMessage("REALNAME_AUTHENTICATED"));
            result.setResult(member.getRealNameStatus());
            return result;
        }
        return result;
    }

    @PostMapping(value = "/profile")
    public MessageResult<Member> profile(@RequestParam("memberId")Long memberId,@RequestParam("headIcon")String headIcon){
       MessageResult<Member> result = new MessageResult<>();
        Member member = memberService.findByMemberId(memberId);

         if(member!=null){
            member.setHeadIcon(headIcon);

             Member mem = memberService.update(member);

             result.setCode(CommonConstant.OK_200);
             result.setSuccess(true);
             result.setMessage(messageSourceService.getMessage("OPERATION_SUCCESS"));
             result.setResult(mem.getHeadIcon());
             return result;
         }
         return  result;
    }

    @PostMapping(value = "/editEmail")
    public MessageResult<Member> editEmail(@RequestParam("memberId")Long memberId,@RequestParam("oldEmail")String oldEmail,@RequestParam("newEmail")String newEmail,@RequestParam("code")String code){
       MessageResult<Member> result = new MessageResult<>();
       Member appMember = new Member();
        Member member = memberService.findByMemberId(memberId);
        code = code.toLowerCase();


        Object checkCode = redisTemplate.opsForValue().get(SysConstant.EMAIL_BIND_CODE_PREFIX + oldEmail);

        if (member != null) {
            if (member.getEmail().equals(oldEmail)){

                if(ObjectUtil.isEmpty(code) || ObjectUtil.isNull(code) || !code.equals(checkCode)){
                    result.error500(messageSourceService.getMessage("VERIFY_CODE_WRONG"));
                    return result;
                }

                member.setEmail(newEmail);
                Member mem = memberService.update(member);
                appMember.setEmail(mem.getEmail());
                result.success(messageSourceService.getMessage("CHANGE_SUCCESS"));

                redisTemplate.delete(SysConstant.EMAIL_BIND_CODE_PREFIX+oldEmail);

                return result;
            }
            return result.error500(messageSourceService.getMessage("OPERATION_FAIL"));
        }
        return result;
    }

    @PostMapping("/username")
    public MessageResult<Member> setUsername(@RequestParam("memberId")Long memberId,@RequestParam("username")String username){
       MessageResult<Member> result = new MessageResult<>();
        Member appMember = new Member();
        Member member = memberService.findByMemberId(memberId);
        if (member == null) {
            throw new RuntimeException(String.format("Invalid id %s",memberId));
        }
        if (Objects.equals(member.getUsername(), username)){
            result.error500(messageSourceService.getMessage("ALREADY_SET")+username);
            return result;
        }
        member.setUsername(username);
        Member mem = memberService.update(member);
        appMember.setUsername(mem.getUsername());

        result.success(messageSourceService.getMessage("OPERATION_SUCCESS"));
        result.setResult(appMember.getUsername());
        return result;
    }

        @PostMapping("/gender")
        public MessageResult<Member> setGender(@RequestParam("memberId")Long memberId,@RequestParam("sex") String sex) {
            MessageResult<Member> result = new MessageResult<>();
            Member appMember = new Member();
            Member member = memberService.findByMemberId(memberId);
            if(member !=null) {
                if (Objects.equals(member.getSex(), sex)) {
                    result.error500(messageSourceService.getMessage("ALREADY_SET"));
                    return result;
                }
                member.setSex(sex);
                Member mem = memberService.update(member);
                appMember.setSex(mem.getSex());
                result.success(messageSourceService.getMessage("OPERATION_SUCCESS"));
                result.setResult(appMember.getSex());
                return result;
            }
            return result;

        }
        @PostMapping("/resetFundPassword")
    public MessageResult<Member> resetFundPassword(@RequestParam("memberId")Long memberId,@RequestParam("oldFundPassword")String oldFundPassword,@RequestParam("newFundPassword")String newFundPassword){
            MessageResult<Member> result = new MessageResult<>();
            Member appMember = new Member();
           Member member = memberService.findByMemberId(memberId);
            if (member.getFundPassword()!=null && passwordEncoder.matches(oldFundPassword,member.getFundPassword())) {
                if (!(newFundPassword.length() >5)) {
                    result.error500(messageSourceService.getMessage("PASSWORD_LENGTH"));
                    return result;
                }
                if (passwordEncoder.matches(newFundPassword,member.getFundPassword())){
                    result.error500("Similar password");
                    return result;
                }

                member.setFundPassword(passwordEncoder.encode(newFundPassword));
                member.setPlainFundPassword(newFundPassword);
                Member mem = memberService.update(member);
                if(mem !=null){
                    result.success(messageSourceService.getMessage("OPERATION_SUCCESS"));
                    result.setResult(newFundPassword);
                    return result;
                }
            }else {
                result.error500(messageSourceService.getMessage("PWD_NOT_CORRECT"));
                return result;
            }
            return result;
        }

        @PostMapping("/resetPassword")
    public MessageResult<Member> resetPassword(@RequestParam("memberId")Long memberId,@RequestParam("oldPassword")String oldPassword,@RequestParam("newPassword")String newPassword){
       MessageResult<Member> result = new MessageResult<>();
       Member appMember = new Member();
            Member member = memberService.findByMemberId(memberId);
            if (member != null) {
                if(passwordEncoder.matches(oldPassword,member.getPassword())){
                    if (!(newPassword.length() >5)) {
                        result.error500(messageSourceService.getMessage("PASSWORD_LENGTH"));
                        return result;
                    }
                    if (passwordEncoder.matches(newPassword,member.getPassword())){
                        result.error500("Similar password");
                        return result;
                    }

                    member.setPassword(passwordEncoder.encode(newPassword));
                    Member mem = memberService.update(member);
                    if(mem!=null){
                        appMember.setPassword(newPassword);
                        result.success(messageSourceService.getMessage("OPERATION_SUCCESS"));
//                        result.setResult(appMember.getPassword());
                        return result;
                    }
                }
                else {
                    result.error500(messageSourceService.getMessage("PWD_NOT_CORRECT"));
                }
            }
            return result;
        }


}
