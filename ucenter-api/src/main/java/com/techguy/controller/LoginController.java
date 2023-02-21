package com.techguy.controller;

import com.techguy.config.LocaleMessageSourceService;
import com.techguy.constant.CommonConstant;
import com.techguy.entity.Member;
import com.techguy.jwt.JWTUtility;
import com.techguy.response.MessageResult;
import com.techguy.service.MemberService;
import com.techguy.service.impl.MemberServiceImpl;
import com.techguy.utils.ValidateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/member")
@AllArgsConstructor
@Slf4j
public class LoginController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MemberServiceImpl memberServiceImpl;
    private final LocaleMessageSourceService messageSourceService;
    private final JWTUtility jwtUtility;

    @PostMapping("/login")
    public MessageResult<Member> login(@RequestParam(value = "email")String email,@RequestParam(value = "password")String password) throws Exception {
        MessageResult<Member> result = new MessageResult<>();
        boolean valid = ValidateUtil.validate(email);
        Member member = null;
        if (valid) {
            member = memberService.findByEmail(email);
        }
        if (member == null) {
            result.setSuccess(false);
            result.setMessage(messageSourceService.getMessage("MAIL_NOT_REGISTER"));
            result.setResult(null);
            return result;
        }


        if (!passwordEncoder.matches(password,member.getPassword())) {
            result.setSuccess(false);
            result.setCode(CommonConstant.INTERNAL_SERVER_ERROR_500);
            result.setMessage(messageSourceService.getMessage("PWD_NOT_CORRECT"));
            result.setResult(null);
            return result;

        } else {
            //generate token
            // password must from db to be match with token user
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                email,
                                password
                        )
                );
            } catch (BadCredentialsException e) {
                throw new Exception("INVALID_CREDENTIALS", e);
            }

            final UserDetails userDetails
                    = memberServiceImpl.loadUserByUsername(email);

            final String token =
                    jwtUtility.generateToken(userDetails);

            member.setToken(token);
//            mem.setId(mem.getId());
//            mem.setEmail(mem.getEmail());
            String ustdBalance = member.getUstdBalance();

            member.setUstdBalance(ustdBalance == null ? String.valueOf(BigDecimal.ZERO) : ustdBalance);

//            appMember.setInvCode(mem.getInvCode());
            Member appMember = memberService.update(member);
            if (appMember == null) {
                result.setSuccess(false);
                result.setMessage(messageSourceService.getMessage("OPERATION_FAIL"));
                result.setResult(null);
                result.setCode(CommonConstant.INTERNAL_SERVER_ERROR_500);
                return result;
            }
            appMember.setPassword(null);
            appMember.setPlainFundPassword(null);
            appMember.setRoles(null);

            log.info("Member id: {} login success",appMember.getId());

            result.setSuccess(true);
            result.setCode(CommonConstant.OK_200);
            result.setMessage(messageSourceService.getMessage("LOGIN_SUCCESS"));
            result.setResult(appMember);

            return result;
        }
    }

}
