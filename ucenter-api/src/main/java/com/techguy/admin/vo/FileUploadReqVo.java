package com.techguy.admin.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUploadReqVo {

    private MultipartFile file;

}