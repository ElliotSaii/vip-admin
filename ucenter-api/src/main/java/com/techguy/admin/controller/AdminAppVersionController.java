package com.techguy.admin.controller;

import com.techguy.admin.vo.AppVersionVo;
import com.techguy.entity.AppVersion;
import com.techguy.response.MessageResult;
import com.techguy.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin/api/app/version")
@CrossOrigin(origins = {"http://154.39.248.73:8818","http://localhost:8080"})
public class AdminAppVersionController {

    private final AppVersionService appVersionService;

     @Autowired
    public AdminAppVersionController(AppVersionService appVersionService) {
        this.appVersionService = appVersionService;
    }

    @PostMapping("/add")
    public MessageResult<?>add(@RequestBody AppVersionVo appVersionVo){
        MessageResult<?> result =new MessageResult<>();
        AppVersion appVersion =new AppVersion();

        appVersion.setPlatform(appVersionVo.getPlatform());
        appVersion.setName(appVersionVo.getName());
        appVersion.setLink(appVersionVo.getLink());
        appVersion.setDescription(appVersionVo.getDescription());
        appVersion.setVersion(appVersionVo.getVersion());
        appVersion.setCreateTime(new Date());

        appVersionService.save(appVersion);
        result.success("App Version Added");
        return result;

    }
    @PutMapping("/update")
    public MessageResult<?>update(@RequestBody AppVersionVo appVersionVo){
        MessageResult<?> result =new MessageResult<>();

        AppVersion appVersion =appVersionService.findById(appVersionVo.getId());

        appVersion.setId(appVersionVo.getId());
        appVersion.setPlatform(appVersionVo.getPlatform());
        appVersion.setName(appVersionVo.getName());

        if(!appVersionVo.getLink().isEmpty() && appVersionVo.getLink()!=null){
            appVersion.setLink(appVersionVo.getLink());
        }

        appVersion.setDescription(appVersionVo.getDescription());
        appVersion.setVersion(appVersionVo.getVersion());
        appVersion.setCreateTime(new Date());

        AppVersion updateApp = appVersionService.update(appVersion);
        if(updateApp!=null){
            result.success("updated version success");
            result.setResult(updateApp);
            return result;
        }else {
            result.error500("Operation failed");
            return result;
        }
    }

    @GetMapping("/list")
    public MessageResult<?> list (){
         MessageResult<?>result =new MessageResult<>();
        List<AppVersion> appVersion =    appVersionService.find();

        if(appVersion.size()>0){
            result.success("Operation success");
            result.setResult(appVersion);
            return result;
        }else {
            result.error500("Operation failed");
            return result;
        }
    }

}
