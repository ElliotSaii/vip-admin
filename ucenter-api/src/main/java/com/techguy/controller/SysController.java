package com.techguy.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.techguy.code.OnlyCodeUtils;
import com.techguy.constant.CommonConstant;
import com.techguy.entity.Member;
import com.techguy.jwt.JWTUtility;
import com.techguy.mail.Email;
import com.techguy.mail.EmailService;
import com.techguy.response.MessageResult;
import com.techguy.service.MemberService;
//import com.techguy.utils.CommonUtils;
import com.techguy.utils.CommonUtils;
import com.techguy.utils.MD5Util;
import com.techguy.utils.RandImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.function.ServerRequest;

import javax.mail.Header;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/sys")

@Slf4j
public class SysController {
    private static final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

    private final RedisTemplate<String,Object>  redisTemplate;

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JWTUtility jwtUtility;




    @Value(value="${vip.uploadType}")
    private String uploadType;

    @Value(value = "${vip.path.upload}")
    private String uploadpath;
    @Value(value = "${spring.mail.username}")
    private String from;

    public SysController(RedisTemplate<String, Object> redisTemplate, MemberService memberService, PasswordEncoder passwordEncoder, EmailService emailService, JWTUtility jwtUtility) {
        this.redisTemplate = redisTemplate;
        this.memberService = memberService;
        this.passwordEncoder =passwordEncoder;
        this.emailService = emailService;
        this.jwtUtility = jwtUtility;
    }



    @GetMapping(value = "/randomImage/{key}")
    public MessageResult<String> randomImage(HttpServletResponse response, @PathVariable String key){
        MessageResult<String> res = new MessageResult<String>();
        try {
            String code = RandomUtil.randomString(BASE_CHECK_CODES,4);

            String lowerCaseCode = code.toLowerCase();
            String realKey = MD5Util.MD5Encode(lowerCaseCode+key, "utf-8");
            redisTemplate.opsForValue().set(realKey,lowerCaseCode,90,TimeUnit.SECONDS);

            String base64 = RandImageUtil.generate(code);

            res.setSuccess(true);
            res.setResult(base64);
        } catch (Exception e) {
            res.error500("获取验证码出错"+e.getMessage());
            e.printStackTrace();
        }
        return res;
    }
    @PostMapping(value = "/forgetpassword")
    public MessageResult<?> forgetPassword(@RequestParam(value = "email")String email,@RequestParam(value = "code")String code,
                                           @RequestParam(value = "password")String password){
      MessageResult<?> result = new MessageResult<>();
        Member appMember = memberService.findByEmail(email);

        if (appMember == null) {
            result.setSuccess(false);
            result.setMessage(CommonConstant.ACCOUNT_NOT_FOUND);
            result.setResult(null);
            result.setCode(CommonConstant.INTERNAL_SERVER_ERROR_500);
            return result;
        }
        if (password.length()<6) {
            result.setSuccess(false);
            result.setCode(CommonConstant.INTERNAL_SERVER_ERROR_500);
            result.setMessage("Password must have 6 character");
            result.setResult(null);
            return result;
        }
        String lowerCase = code.toLowerCase();
        String realKey = MD5Util.MD5Encode(lowerCase , "utf-8");
        Object checkCode = redisTemplate.opsForValue().get(realKey);

        if (ObjectUtil.isEmpty(code) || ObjectUtil.isNull(code) || !lowerCase.equals(checkCode)) {
            result.setSuccess(false);
            result.setMessage(CommonConstant.VERIFY_CODE_WRONG);
            result.setResult(null);
            return result;
        }

        appMember.setPassword(passwordEncoder.encode(password));
        Member member = memberService.update(appMember);
        result.success("reset password successfully");
        result.setResult(member);
        redisTemplate.delete(realKey);
        return result;
    }

    @PostMapping("/sendMail")
    public MessageResult<Email> sendMail (@RequestParam("email")String email){
        MessageResult<Email> result = new MessageResult<>();

        Member member = memberService.findByEmail(email);
        if(member.getEmail().equals(email)){
            result.error500("Email already exits");
            return result;
        }

        String name= null;
        if (email.endsWith("@gmail.com")) {
             name = email.replace("@gmail.com", "");
        }
        
            String code = OnlyCodeUtils.creatUUID();
            //store in  redis 3 mins
        String lowerCase = code.toLowerCase();
        String realKey = MD5Util.MD5Encode(lowerCase, "utf-8");
            redisTemplate.opsForValue().set(realKey,lowerCase,3,TimeUnit.MINUTES);

            Email mail = new Email();
            mail.setTo(email);
            mail.setFrom(from);
            mail.setSubject("VIP Email");
            mail.setTemplate("mail.html");
            Map<String, Object> properties = new HashMap<>();
            properties.put("name", "Dear "+name);
            properties.put("date", LocalDate.now().toString());
            properties.put("code",code);
//            properties.put("technologies", Arrays.asList("Python", "Go", "C#"));
            mail.setProperties(properties);

            try {
                emailService.sendMail(mail);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            result.setMessage("Sending Email Successfully!");
            result.setCode(CommonConstant.OK_200);
            result.setSuccess(true);
            result.setResult(mail);

        return result;
    }


  @PostMapping(value = "/upload")
  public MessageResult<?> upload(HttpServletRequest request){

//        memberService.findByMemberId(memberId);

     /* HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);*/
      MessageResult<?> result =new MessageResult<>();
      String savePath = "";
//      request.getParameter("biz");
      String bizPath = "";
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      MultipartFile file = multipartRequest.getFile("biz");




      if(ObjectUtil.isEmpty(bizPath)){
              if(CommonConstant.UPLOAD_TYPE_OSS.equals(uploadType)){
            bizPath="upload";
          }else {
         bizPath = "";
          }
      }
      if(CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)){
      savePath = this.uploadLocal(file,bizPath);
      } else {
            savePath = CommonUtils.upload(file,bizPath,uploadType);
      }
      if(ObjectUtil.isNotEmpty(savePath)){
          result.setMessage("upload success!");
          result.setSuccess(true);
          result.setCode(CommonConstant.OK_200);
          result.setResult(savePath);
          return result;
      } else {
        result.error500("upload failed");
          return result;
      }
  }

    private String uploadLocal(MultipartFile mf,String bizPath){
        try {
            String ctxPath = uploadpath;
            String fileName = null;
            File file = new File(ctxPath + File.separator + bizPath + File.separator );
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }
            String orgName = mf.getOriginalFilename();// 获取文件名
            orgName = CommonUtils.getFileName(orgName);
            if(orgName.indexOf(".")!=-1){
                fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.lastIndexOf("."));
            }else{
                fileName = orgName+ "_" + System.currentTimeMillis();
            }
            String savePath = file.getPath() + File.separator + fileName;
            File savefile = new File(savePath);
            FileCopyUtils.copy(mf.getBytes(), savefile);
            String dbpath = null;
            if(ObjectUtil.isNotEmpty(bizPath)){
                dbpath = bizPath + File.separator + fileName;
            }else{
                dbpath = fileName;
            }
            if (dbpath.contains("\\")) {
                dbpath = dbpath.replace("\\", "/");
            }
            return dbpath;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }
}
