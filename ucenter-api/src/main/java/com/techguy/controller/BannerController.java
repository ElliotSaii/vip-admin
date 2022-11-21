package com.techguy.controller;
import com.techguy.entity.BannerImage;
import com.techguy.response.MessageResult;
import com.techguy.service.BannerImageService;
import com.techguy.vo.BannerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/banner")
public class BannerController {
    private final BannerImageService bannerImageService;

    @Autowired
    public BannerController(BannerImageService bannerImageService) {
        this.bannerImageService = bannerImageService;
    }

    @GetMapping("/list")
    public MessageResult<?>  list(){
        MessageResult<?> result =new MessageResult<>();
        List<BannerImage> bannerImageList= bannerImageService.findAll();

        if(bannerImageList.size()>0){
            result.success("Banner List");
            result.setResult(bannerImageList);
            return result;
        }else {
            result.error500("No Banner Imges");
            return  result;
        }
    }
}
