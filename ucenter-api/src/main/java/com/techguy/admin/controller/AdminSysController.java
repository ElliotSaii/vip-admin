package com.techguy.admin.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.techguy.admin.vo.FileUploadReqVo;
import com.techguy.code.OnlyCodeUtils;
import com.techguy.constant.CommonConstant;
import com.techguy.entity.Member;
import com.techguy.jwt.JWTUtility;
import com.techguy.mail.Email;
import com.techguy.mail.EmailService;
import com.techguy.response.MessageResult;
import com.techguy.service.MemberService;
import com.techguy.utils.CommonUtils;
import com.techguy.utils.MD5Util;
import com.techguy.utils.RandImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/admin/api/sys")
@CrossOrigin(origins = {"http://154.39.248.73:8818","http://localhost:3000"})
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Slf4j
public class AdminSysController {
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

    public AdminSysController(RedisTemplate<String, Object> redisTemplate, MemberService memberService, PasswordEncoder passwordEncoder, EmailService emailService, JWTUtility jwtUtility) {
        this.redisTemplate = redisTemplate;
        this.memberService = memberService;
        this.passwordEncoder =passwordEncoder;
        this.emailService = emailService;
        this.jwtUtility = jwtUtility;
    }

  @PostMapping(value = "/upload")
  public MessageResult<?> upload(FileUploadReqVo reqVo, HttpServletRequest request){
     /* HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);*/
      MessageResult<?> result =new MessageResult<>();
      String savePath = "";
//      request.getParameter("biz");
      String bizPath = "";
//      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      MultipartFile file = reqVo.getFile();

//      String contentType = file.getContentType();


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
