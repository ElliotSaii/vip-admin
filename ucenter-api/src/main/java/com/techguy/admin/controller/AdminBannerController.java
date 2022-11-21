package com.techguy.admin.controller;

import com.techguy.entity.BannerImage;
import com.techguy.response.MessageResult;
import com.techguy.service.BannerImageService;
import com.techguy.vo.BannerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin/api/banner")
@CrossOrigin(origins = {"http://154.39.248.73:8818","http://localhost:8080"})
public class AdminBannerController {
    private final BannerImageService bannerImageService;

    @Autowired
    public AdminBannerController(BannerImageService bannerImageService) {
        this.bannerImageService = bannerImageService;
    }

    @PostMapping("/add")
    public MessageResult<?> add(@RequestBody BannerVo bannerVo){
        MessageResult<?>result =new MessageResult<>();
        BannerImage bannerImage =new BannerImage();

        bannerImage.setImageUrl(bannerVo.getImageUrl());
        bannerImage.setCreateTime(new Date());

        bannerImageService.save(bannerImage);

        result.success("Banner Added");
        return result;
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
    @DeleteMapping("/delete")
    public MessageResult<?> delete(@RequestParam("id")Long id){
        MessageResult<?> result =new MessageResult<>();
      BannerImage bannerImage=  bannerImageService.findById(id);

      if(bannerImage!=null){
          bannerImageService.delete(id);
          result.success("Deleted success");
          return result;
      }else {
          result.error500("Operation failed");
          return result;
      }
    }
}
