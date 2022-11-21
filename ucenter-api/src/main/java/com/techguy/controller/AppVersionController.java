package com.techguy.controller;

import com.techguy.entity.AppVersion;
import com.techguy.response.MessageResult;
import com.techguy.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/app")
public class AppVersionController {
    private final AppVersionService appVersionService;


    @GetMapping("/index")
    public MessageResult<?> home(){
        return MessageResult.error("index");
    }

    @Autowired
    public AppVersionController(AppVersionService appVersionService) {
        this.appVersionService = appVersionService;
    }

    @GetMapping("/version")
    public MessageResult<?> version (@RequestParam("platform")Integer platform){
        MessageResult<?>result =new MessageResult<>();
        AppVersion appVersion = appVersionService.findByPlatform(platform);

        if(appVersion!=null){
         result.success("Application Info");
         result.setResult(appVersion);
         return  result;
        }else {
            result.error500("Operation failed");
            return result;
        }


    }
}
