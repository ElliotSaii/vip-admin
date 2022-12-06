package com.techguy.admin.controller;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/api/guide")
@CrossOrigin(origins = {"http://154.39.248.73:8818","http://localhost:3000"})
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminGuideController {
    private final GuideService guideService;
    private final SubGuideService subGuideService;

    @Autowired
    public AdminGuideController(GuideService guideService, SubGuideService subGuideService) {
        this.guideService = guideService;
        this.subGuideService = subGuideService;
    }

    @PostMapping("/add")
    public MessageResult<?> add(@RequestParam("title")String title){
        MessageResult<?> result =new MessageResult<>();
        Guide guide =new Guide();
        guide.setTitle(title);
        guide.setCreateTime(new Date());

        guideService.save(guide);
        result.success("Guide Added");
        return  result;
    }

    @DeleteMapping("/delete")
    public MessageResult<?> delete(@RequestParam("id")Long id){
        MessageResult<?> result=new MessageResult<>();
      Guide  guide=  guideService.findById(id);
       if(guide!=null){
           guideService.delete(id);
           result.success("Deleted Guide");
           return  result;
       }else{
           result.error500("Operation failed");
           return  result;
       }

    }
    @PostMapping("/sub/add")
    public MessageResult<?> addSubGuide(@RequestParam("guideId")Long guidId,@RequestParam("title")String title,@RequestParam("description")String description,@RequestParam("videoUrl")String videoUrl){
        MessageResult<?>result =new MessageResult<>();
        Guide guide = guideService.findById(guidId);
        if(guide!=null){
            SubGuide subGuide =new SubGuide();
            subGuide.setGuideId(guidId);
            subGuide.setTitle(title);

            if(!videoUrl.isEmpty()){
                subGuide.setVideoUrl(videoUrl);
                subGuide.setType(2);
            }else {
                subGuide.setVideoUrl(videoUrl);
                subGuide.setType(1);
            }
            subGuide.setDescription(description);
            subGuide.setCreateTime(new Date());
            subGuideService.save(subGuide);
            result.success("Sub Guide Added");
            return  result;

        }else {
            result.error500("Operation failed");
            return  result;
        }

    }


    @GetMapping("/list")
    public MessageResult<?> list(@RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize){
        MessageResult<?> result =new MessageResult<>();
        pageNo = pageNo-1;
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
    public MessageResult<?> subList(@RequestParam("id")Long id){
        MessageResult<?> result=new MessageResult<>();
        Map<String,Object> map =new HashMap<>();

        List<SubGuide> subGuideList = subGuideService.findByGuideId(id);
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

    @DeleteMapping("/sub/delete")
    public MessageResult<?> deleteSub(@RequestParam("id")Long id){
        MessageResult<?> result =new MessageResult<>();

     SubGuide subGuide =   subGuideService.findById(id);
     if(subGuide!=null){
         subGuideService.delete(id);
         result.success("Deleted success");
         return result;
     }else {
         result.error500("Operation failed");
         return result;
     }
    }

    @PutMapping("/sub/edit")
    public MessageResult<?> subEdit(@RequestParam("id")Long id,@RequestParam("title")String title,@RequestParam("description")String description){
        MessageResult<?> result =new MessageResult<>();
        SubGuide subGuide = subGuideService.findById(id);

        if(subGuide!=null){
            subGuide.setTitle(title);
            subGuide.setDescription(description);
            subGuide.setId(id);

           SubGuide subGuide1= subGuideService.update(subGuide);
            result.success("Update Success");
            result.setResult(subGuide1);
            return result;

        }else {
            result.error500("Operation failed");
            return result;
        }
    }
}
