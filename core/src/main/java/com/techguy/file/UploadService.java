package com.techguy.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Service
public interface UploadService {

    void uploadImg(MultipartFile multipartFile)throws IOException;
}
