package com.techguy.controller;

import cn.hutool.core.util.ObjectUtil;
import com.techguy.constant.CommonConstant;
import com.techguy.entity.Member;
import com.techguy.response.MessageResult;
import com.techguy.service.MemberService;
import com.techguy.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/user/setting")
public class UserSettingController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String,Object> redisTemplate;

   @Autowired
   public UserSettingController(MemberService memberService, PasswordEncoder passwordEncoder, RedisTemplate<String, Object> redisTemplate){
       this.memberService = memberService;
       this.passwordEncoder = passwordEncoder;
       this.redisTemplate = redisTemplate;
   }
    @PostMapping(value = "/realName")
    public MessageResult<Member> setRealName(@RequestParam("memberId") Long memberId,@RequestParam("name")String name,@RequestParam("cardNumber")String cardNumber,
                                             @RequestParam("front") String front,@RequestParam("back")String back,@RequestParam("face")String face){
        MessageResult<Member> result = new MessageResult<>();
        Member member = memberService.findByMemberId(memberId);

        if (member != null) {
            Integer realNameStatus = member.getRealNameStatus();
            if(realNameStatus==2){
                result.error500("RealName Already authenticated!");
                return result;
            }
            member.setRealNameStatus(1);
            member.setName(name);
            member.setCardNumber(cardNumber);
            member.setFront(front);
            member.setBack(back);
            member.setFace(face);

            Member appMember = memberService.update(member);

            if (appMember != null) {
                result.setSuccess(true);
                result.setMessage("Pending real name");
                result.setResult(appMember.getRealNameStatus());
                return  result;
            }
            else {
                result.error500("Invalid user id: "+memberId);
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
        if (member != null) {
            if (password.length()>5){
                String fundPW =passwordEncoder.encode(password);
                member.setFundPassword(fundPW);
                memberService.update(member);

                result.setSuccess(true);
                result.setCode(CommonConstant.OK_200);
                result.setMessage("Generated fund password");
                result.setResult(password);
                return result;
            }else {
                result.setSuccess(false);
                result.setCode(500);
                result.setMessage("Fund Password must have 6 character");
                result.setResult(null);
                return  result;
            }
        }
        return result;

    }
    @GetMapping(value = "/realNameStatus")
    public MessageResult<Member> getRealName(@RequestParam("memberId")Long memberId){
       MessageResult<Member> result = new MessageResult<>();
        Member member = memberService.findByMemberId(memberId);

        if (member != null) {
            result.setSuccess(true);
            result.setCode(CommonConstant.OK_200);
            result.setResult(member.getRealNameStatus());
            return result;
        }
        return result;
    }

    @PostMapping(value = "/profile")
    public MessageResult<Member> profile(@RequestParam("memberId")Long memberId,@RequestParam("headIcon")String headIcon){
       MessageResult<Member> result = new MessageResult<>();
        Member member = memberService.findByMemberId(memberId);
        Member appMember = new Member();
         if(member!=null){
            member.setHeadIcon(headIcon);

             Member mem = memberService.update(member);

             result.setCode(CommonConstant.OK_200);
             result.setSuccess(true);
             result.setMessage("Set Profile Successful!");
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
        String realKey = MD5Util.MD5Encode(code, "utf-8");

        Object checkCode = redisTemplate.opsForValue().get(realKey);

        if (member != null) {
            if (member.getEmail().equals(oldEmail)){
                if(ObjectUtil.isEmpty(code) || ObjectUtil.isNull(code) || !code.equals(checkCode)){
                    result.setSuccess(false);
                    result.setCode(CommonConstant.INTERNAL_SERVER_ERROR_500);
                    result.setMessage(CommonConstant.VERIFY_CODE_WRONG);
                    result.setResult(null);
                    return result;
                }

                member.setEmail(newEmail);
                Member mem = memberService.update(member);
                appMember.setEmail(mem.getEmail());
                result.setMessage("Change email success!");
                result.setResult(appMember.getEmail());
                redisTemplate.delete(realKey);
                return result;
            }
            result.error500("Email not register yet!");
            return result;
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
            result.error500("Already set! "+username);
            return result;
        }
        member.setUsername(username);
        Member mem = memberService.update(member);
        appMember.setUsername(mem.getUsername());

        result.success("Set username success");
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
                    result.error500("Already set! "+sex);
                    return result;
                }
                member.setSex(sex);
                Member mem = memberService.update(member);
                appMember.setSex(mem.getSex());
                result.success("Set gender success");
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
                if (!(newFundPassword.length() >4)) {
                    result.error500("Password not strong enough!");
                    return result;
                }
                if (passwordEncoder.matches(newFundPassword,member.getFundPassword())){
                    result.error500("Similar password");
                    return result;
                }

                member.setFundPassword(passwordEncoder.encode(newFundPassword));
                Member mem = memberService.update(member);
                if(mem !=null){
                    appMember.setFundPassword(mem.getFundPassword());
                    result.success("Change Fund password success");
                    result.setResult(appMember.getFundPassword());
                    return result;
                }
            }
            result.error500("Password is not correct!");
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
                        result.error500("Password not strong enough!");
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
                        result.success("Change Login Password Success!");
                        result.setResult(appMember.getPassword());
                        return result;
                    }
                }
                else {
                    result.error500("Password is not correct!");
                }
            }
            return result;
        }


}
