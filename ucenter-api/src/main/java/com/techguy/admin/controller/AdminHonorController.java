package com.techguy.admin.controller;

import com.techguy.entity.Honor;
import com.techguy.response.MessageResult;
import com.techguy.service.HonorService;
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
@RequestMapping("/admin/api/honor")
@CrossOrigin(origins = {"http://154.39.248.73:8818","http://localhost:8080"})
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminHonorController {
    private final HonorService honorService;

    @Autowired
    public AdminHonorController(HonorService honorService) {
        this.honorService = honorService;
    }

    @PostMapping("/add")
    public MessageResult<?> add(@RequestParam("email")String email,@RequestParam("description")String description,@RequestParam(value = "imageLink",required = false)String imageLink,
                                @RequestParam(value = "imageUrl",required = false)String imageUrl){
        MessageResult<?> result =new MessageResult<>();
        Honor honor =new Honor();

        honor.setEmail(email);
        honor.setDescription(description);

        if(imageLink!=null && !imageLink.equals("undefined")){
            honor.setImageUrl(imageLink);
        }else if (imageUrl!=null && !imageUrl.equals("undefined")){
            honor.setImageUrl(imageUrl);
        }
        honor.setCreateTime(new Date());

        honorService.save(honor);
        result.success("Honor Added!");
        return result;

    }

    @PutMapping("/edit")
    public MessageResult<?> edit (@RequestParam("id")Long id,@RequestParam("email")String email,@RequestParam("description")String description,
                                @RequestParam(value = "imageUrl",required = false)String imageUrl) {

      MessageResult<?> result=new MessageResult<>();
      Honor honor=honorService.findById(id);
      if(honor!=null){
          honor.setEmail(email);
          honor.setDescription(description);
          honor.setImageUrl(imageUrl);

          honorService.update(honor);
          result.success("Operation success");
          return result;
      }else {
          result.error500("OPERATION_FAIL");
          return result;
      }
    }


    @GetMapping("/list")
    public MessageResult<?> list (@RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize){
        MessageResult<?> result =new MessageResult<>();

        pageNo = pageNo-1;
        Pageable page = PageRequest.of(pageNo,pageSize, Sort.by("createTime").descending());
        Map<String,Object> map =new HashMap<>();
      Page<Honor> honorPage = honorService.findAll(page);

        long totalElements = honorPage.getTotalElements();
        map.put("totalElements",totalElements);
        List<Honor> honorList = honorPage.getContent();

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
