package com.techguy.controller;

import com.techguy.config.LocaleMessageSourceService;
import com.techguy.entity.AppVersion;
import com.techguy.response.MessageResult;
import com.techguy.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app")
public class AppVersionController {
    private final AppVersionService appVersionService;
    private final LocaleMessageSourceService messageSourceService;


    @Autowired
    public AppVersionController(AppVersionService appVersionService, LocaleMessageSourceService messageSourceService) {
        this.appVersionService = appVersionService;
        this.messageSourceService = messageSourceService;
    }

    @GetMapping("/version")
    public MessageResult<?> version (@RequestParam("platform")Integer platform){
        MessageResult<?>result =new MessageResult<>();
        AppVersion appVersion = appVersionService.findByPlatform(platform);

        if(appVersion!=null){
         result.success(messageSourceService.getMessage("OPERATION_SUCCESS"));
         result.setResult(appVersion);
         return  result;
        }else {
            result.error500(messageSourceService.getMessage("OPERATION_FAIL"));
            return result;
        }


    }
}
