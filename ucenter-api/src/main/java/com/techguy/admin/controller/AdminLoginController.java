package com.techguy.admin.controller;

import com.techguy.constant.CommonConstant;
import com.techguy.entity.LoginAttempt;
import com.techguy.entity.admin.Admin;
import com.techguy.jwt.JWTUtility;
import com.techguy.response.MessageResult;
import com.techguy.service.AdminService;
import com.techguy.service.LoginAttemptService;
import com.techguy.service.impl.AdminServiceImpl;
import com.techguy.utils.ValidateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/api")
@CrossOrigin(origins = {"http://154.39.248.73:8818","http://localhost:3000"})
@AllArgsConstructor
@Slf4j
public class AdminLoginController {
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AdminServiceImpl adminServiceImpl;
    private final JWTUtility jwtUtility;
    private final LoginAttemptService loginAttemptService;

    @PostMapping("/login")
    public MessageResult<Admin> login(HttpServletRequest request, @RequestParam(value = "email")String email, @RequestParam(value = "password")String password) throws Exception {
        MessageResult<Admin> result = new MessageResult<>();
        boolean valid = ValidateUtil.validate(email);
        Admin admin = null;
        if (valid) {
            admin = adminService.findByEmail(email);
        } else {
            result.error500("Email format not provided");
            return result;
        }
        if (admin == null) {
            result.error500("Email has not register yet!");
            return result;
        }


        if (!passwordEncoder.matches(password,admin.getPassword())) {
            result.setSuccess(false);
            result.setCode(CommonConstant.INTERNAL_SERVER_ERROR_500);
            result.setMessage("Something wrong!");
            result.setResult(null);
            return result;

        } else {
            final UserDetails userDetails
                    = adminServiceImpl.loadUserByUsername(email);

            final String token =
                    jwtUtility.generateToken(userDetails);
            admin.setToken(token);

            Admin sysAdmin = adminService.update(admin);
            String ip = request.getRemoteAddr();

            log.info("Admin request login ip {}",ip);

            LoginAttempt loginAttempt = loginAttemptService.findByIpAddress(ip);
             if(loginAttempt==null){
                 result.error500("Undefined ip "+ip);
                 return result;
             }
             else {
                 loginAttempt.setAttemptTime(-1);
                 loginAttemptService.update(loginAttempt);
             }

            result.setSuccess(true);
            result.setCode(CommonConstant.OK_200);
            result.setMessage("Login success!");
            result.setResult(sysAdmin);

            return result;
        }
    }
}
