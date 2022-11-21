package com.techguy.admin.controller;

import com.techguy.constant.CommonConstant;
import com.techguy.entity.Member;
import com.techguy.entity.product.ProductRecord;
import com.techguy.response.MessageResult;
import com.techguy.service.MemberService;
import com.techguy.service.ProductRecordService;
import com.techguy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/api/product-record")
@CrossOrigin(origins = {"http://154.39.248.73:8818","http://localhost:8080"})
public class AdminProductRecordController {
    private final ProductRecordService productRecordService;
    private final ProductService productService;
    private final MemberService memberService;


    public AdminProductRecordController(ProductRecordService productRecordService, ProductService productService, MemberService memberService) {
        this.productRecordService = productRecordService;
        this.productService = productService;
        this.memberService = memberService;
    }

    @GetMapping("/list")
    public MessageResult<?> list(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize,@RequestParam(value = "status",required = false)Integer status,
                                 @RequestParam(value = "name",required = false)String name) {
        pageNo = (pageNo - 1);
        MessageResult<?> result = new MessageResult<>();
        Map<String, Object> map = new HashMap<>();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createTime").descending());

        Page<ProductRecord> productRecordPage;
        if(status>0){
          productRecordPage = productRecordService.findAllPage(pageable,status);
        }else if(name!=null){
            productRecordPage = productRecordService.filterByName(name,pageable);
        }
        else {
         productRecordPage = productRecordService.findAllPage(pageable);
        }

        long totalElements = productRecordPage.getTotalElements();
        List<ProductRecord> productRecordList = productRecordPage.getContent();

        if (productRecordList.size() > 0) {
            result.setSuccess(true);
            result.setCode(CommonConstant.OK_200);
            result.setMessage("Order List");
            map.put("totalElements", totalElements);
            map.put("list", productRecordList);
            result.setResult(map);
            return result;
        } else {
            result.error500("No order list");
            return result;
        }
    }





}
