package com.techguy.file;

import com.techguy.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@AllArgsConstructor
public class AvatarUploadServiceImpl implements UploadService{
    private final MemberService memberService;
    @Override
    public void uploadImg(MultipartFile multipartFile) throws IOException {
//        memberService.
    }
}
