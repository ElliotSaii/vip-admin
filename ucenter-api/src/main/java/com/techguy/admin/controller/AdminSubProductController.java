package com.techguy.admin.controller;

import com.techguy.admin.vo.SubProductVo;
import com.techguy.entity.product.Product;
import com.techguy.entity.product.SubProduct;
import com.techguy.response.MessageResult;
import com.techguy.service.ProductService;
import com.techguy.service.SubProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/api/sub-product")
@CrossOrigin(origins = {"http://154.39.248.73:8818","http://localhost:8080"})
public class AdminSubProductController {

    private final SubProductService subProductService;
    private final ProductService productService;

    @Autowired
    public AdminSubProductController(SubProductService subProductService,ProductService productService){
        this.subProductService = subProductService;
        this.productService = productService;
    }

    @GetMapping("/list")
    public MessageResult<?> list(@RequestParam("productId")Long productId,@RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize){
        MessageResult<?> result = new MessageResult<>();
        pageNo =pageNo-1;
        Pageable page = PageRequest.of(pageNo,pageSize,Sort.by("createTime").descending());
        Page<SubProduct> subProductPage = subProductService.findSubProductByProductId(productId,page);

        List<SubProduct> subProductList = subProductPage.getContent();
        long totalElements = subProductPage.getTotalElements();

        Map<String,Object>map =new HashMap<>();
        map.put("totalElements",totalElements);
        if(subProductList.size()>0){
            result.success("Subs List");
            map.put("list",subProductList);
            result.setResult(map);
            return result;
        }
        else {
            result.error500("No Subs");
            return result;
        }
    }

    @DeleteMapping("/delete")
    public MessageResult<?> delete(@RequestParam("subId")Long subId){
        MessageResult<?> result =new MessageResult<>();
        SubProduct subProduct = subProductService.findBySubProductId(subId);

        if(subProduct!=null){
            Long productId = subProduct.getProductId();
            Product product = productService.findByProductId(productId);
            if(product!=null){
                BigDecimal totalUnitPrice = product.getTotalUnitPrice().subtract(subProduct.getUnitPrice());
                product.setTotalUnitPrice(totalUnitPrice);
                productService.update(product);

                subProductService.delete(subId);
                result.success("Deleted Success");
                return result;
            } else {
                result.error500("Operation failed");
                return result;
            }
        }else {
            result.error500("Operation failed");
            return result;
        }
    }
    @PutMapping(value = "/update")
    public MessageResult<SubProduct> editSub(@RequestBody SubProductVo subProductVo){
        MessageResult<SubProduct> result =new MessageResult<>();
        SubProduct subProduct = subProductService.findBySubProductId(subProductVo.getId());

        if(subProduct!=null){

            Product product = productService.findByProductId(subProduct.getProductId());

            BigDecimal totalUnitPrice = product.getTotalUnitPrice().subtract(subProduct.getUnitPrice());

            BigDecimal remainTotalUnitPrice = totalUnitPrice.add(subProductVo.getUnitPrice());
            product.setTotalUnitPrice(remainTotalUnitPrice);
            productService.update(product);

            subProduct.setUnitPrice(subProductVo.getUnitPrice());
            subProduct.setDescription(subProductVo.getDescription());
            subProduct.setName(subProductVo.getName());
            subProduct.setImageUrl(subProductVo.getImageUrl());
            subProduct.setFromImgUrl(subProductVo.getFromImgUrl());
            subProduct.setFree(subProductVo.getFree());

            subProductService.update(subProduct);
            result.success("Update success");
            return result;
        }else {
            result.error500("Operation failed");
            return result;
        }
    }
}
