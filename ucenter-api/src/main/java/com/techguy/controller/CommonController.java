package com.techguy.controller;

import cn.hutool.core.util.ObjectUtil;
import com.techguy.constant.CommonConstant;
import com.techguy.entity.Bank;
import com.techguy.response.MessageResult;
import com.techguy.service.BankService;
import com.techguy.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/common")
public class CommonController {
    private final BankService bankService;

    @Autowired
    public CommonController(BankService bankService){
        this.bankService = bankService;
    }
    @GetMapping(value = "/list")
    public MessageResult<Bank> getBankList(){
        MessageResult<Bank> result = new MessageResult<>();

        List<Bank> bankList = bankService.getBankList();
        if (bankList.size()>0){
            result.setSuccess(true);
            result.setCode(CommonConstant.OK_200);
            result.setMessage("Banks list");
            result.setResult(bankList);
        }
        return result;
    }

//    @RequestMapping(value = "/admin/api/sys")
//    @Slf4j
//    public static class AdminSysController {
//
//
//        @Value(value="${vip.uploadType}")
//        private String uploadType;
//
//        @Value(value = "${vip.path.upload}")
//        private String uploadpath;
//
//        @PostMapping(value = "/upload")
//        public MessageResult<?> upload(HttpServletRequest request){
//         /* HttpHeaders headers = new HttpHeaders();
//          headers.setContentType(MediaType.MULTIPART_FORM_DATA);*/
//            MessageResult<?> result =new MessageResult<>();
//            String savePath = "";
//    //      request.getParameter("biz");
//            String bizPath = "";
//            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//            MultipartFile file = multipartRequest.getFile("biz");
//
//            String contentType = file.getContentType();
//
//
//            if(ObjectUtil.isEmpty(bizPath)){
//                if(CommonConstant.UPLOAD_TYPE_OSS.equals(uploadType)){
//                    bizPath="upload";
//                }else {
//                    bizPath = "";
//                }
//            }
//            if(CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)){
//                savePath = this.uploadLocal(file,bizPath);
//            } else {
//                savePath = CommonUtils.upload(file,bizPath,uploadType);
//            }
//            if(ObjectUtil.isNotEmpty(savePath)){
//                result.setMessage("upload success!");
//                result.setSuccess(true);
//                result.setCode(CommonConstant.OK_200);
//                result.setResult(savePath);
//            } else {
//                result.setMessage("upload failed");
//                result.setSuccess(false);
//            }
//            return result;
//        }


//        private String uploadLocal(MultipartFile mf,String bizPath){
//            try {
//                String ctxPath = uploadpath;
//                String fileName = null;
//                File file = new File(ctxPath + File.separator + bizPath + File.separator );
//                if (!file.exists()) {
//                    file.mkdirs();// 创建文件根目录
//                }
//                String orgName = mf.getOriginalFilename();// 获取文件名
//                orgName = CommonUtils.getFileName(orgName);
//                if(orgName.indexOf(".")!=-1){
//                    fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.lastIndexOf("."));
//                }else{
//                    fileName = orgName+ "_" + System.currentTimeMillis();
//                }
//                String savePath = file.getPath() + File.separator + fileName;
//                File savefile = new File(savePath);
//                FileCopyUtils.copy(mf.getBytes(), savefile);
//                String dbpath = null;
//                if(ObjectUtil.isNotEmpty(bizPath)){
//                    dbpath = bizPath + File.separator + fileName;
//                }else{
//                    dbpath = fileName;
//                }
//                if (dbpath.contains("\\")) {
//                    dbpath = dbpath.replace("\\", "/");
//                }
//                return dbpath;
//            } catch (IOException e) {
//                log.error(e.getMessage(), e);
//            }
//            return "";
//        }
//    }
}
