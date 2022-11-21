package com.techguy.controller;

import com.techguy.entity.Guide;
import com.techguy.entity.SubGuide;
import com.techguy.response.MessageResult;
import com.techguy.service.GuideService;
import com.techguy.service.SubGuideService;
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
@RequestMapping("/api/guide")
public class GuideController {

    private final GuideService guideService;
    private final SubGuideService subGuideService;

   @Autowired
    public GuideController(GuideService guideService, SubGuideService subGuideService) {
        this.guideService = guideService;
       this.subGuideService = subGuideService;
   }

    @GetMapping("/list")
    public MessageResult<?> list(@RequestParam("pageNo")Integer pageNo, @RequestParam("pageSize")Integer pageSize){
        MessageResult<?> result =new MessageResult<>();
        Pageable page = PageRequest.of(pageNo,pageSize, Sort.by("createTime").descending());
        Map<String,Object> map =new HashMap<>();
        Page<Guide> guidePage = guideService.findAll(page);

        long totalElements = guidePage.getTotalElements();
        List<Guide> guideList = guidePage.getContent();
        map.put("totalElements",totalElements);

        if(guideList.size()>0){
            map.put("list",guideList);
            result.setResult(map);
            return result;
        }else {
            result.error500("No guide list");
            return result;
        }
    }

    @GetMapping("/sub/list")
    public MessageResult<?> subList(@RequestParam("id")Long id,@RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize,@RequestParam("type")Integer type){

        Pageable page =PageRequest.of(pageNo,pageSize,Sort.by("createTime").descending());

        MessageResult<?> result=new MessageResult<>();
        Map<String,Object> map =new HashMap<>();

     Page<SubGuide>subGuidePage = subGuideService.findAllPage(id,type, page);
        List<SubGuide> subGuideList = subGuidePage.getContent();
        long totalElements = subGuidePage.getTotalElements();
        map.put("totalElements",totalElements);
        if(subGuideList.size()>0){
            map.put("list",subGuideList);
            result.success("Sub Guide List");
            result.setResult(map);
            return result;
        }else {
            result.error500("No sub guide");
            return result;
        }
    }
}
