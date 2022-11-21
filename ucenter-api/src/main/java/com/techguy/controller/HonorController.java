package com.techguy.controller;

import com.techguy.entity.Honor;
import com.techguy.response.MessageResult;
import com.techguy.service.HonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/honor")
public class HonorController {
    private final HonorService honorService;

    @Autowired
    public HonorController(HonorService honorService) {
        this.honorService = honorService;
    }

    @GetMapping("/list")
    public MessageResult<?> list (){
        MessageResult<?> result =new MessageResult<>();

        Map<String,Object> map =new HashMap<>();

        List<Honor> honorList =  honorService.findAll();

        if(honorList.size()>0){
            map.put("list",honorList);
            result.success("Honor List");
            result.setResult(map);
            return result;
        }else {
            result.error500("No honors");
            return  result;
        }
    }
}
