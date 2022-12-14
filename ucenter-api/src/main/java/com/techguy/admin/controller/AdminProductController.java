package com.techguy.admin.controller;
import com.techguy.admin.vo.ProductVo;
import com.techguy.admin.vo.SubProductVo;
import com.techguy.constant.CommonConstant;
import com.techguy.entity.product.Product;
import com.techguy.entity.product.ProductRecord;
import com.techguy.entity.product.SubProduct;
import com.techguy.response.MessageResult;
import com.techguy.service.ProductRecordService;
import com.techguy.service.ProductService;
import com.techguy.service.SubProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;


@RestController
@RequestMapping(value = "/admin/api/product")
@CrossOrigin(origins = {"http://154.39.248.73:8818","http://localhost:3000"})
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminProductController {

    private final ProductService productService;
    private final SubProductService subProductService;
    private final ProductRecordService productRecordService;

    @Autowired
    public AdminProductController(ProductService productService, SubProductService subProductService, ProductRecordService productRecordService){
        this.productService = productService;
        this.subProductService = subProductService;
        this.productRecordService = productRecordService;
    }

    @PostMapping(value = "/add",consumes = {"*/*"})
    public MessageResult<Product> add(@RequestBody ProductVo productVo){
        MessageResult<Product> result = new MessageResult<>();
        Product product = new Product();
        product.setBuyAmount(productVo.getBuyAmount());
        product.setBuyStatus(0);
        product.setCreateTime(new Date());
        product.setFree(productVo.getFree());
        product.setName(productVo.getName());
        product.setTotalUnitPrice(new BigDecimal("0.00"));
//        product.setTotalUnitPrice(productVo.getTotalUnitPrice());
//        product.setUpdateTime(productVo.getUpdateTime());

        product.setStartTime(productVo.getStartTime());
        product.setEndTime(productVo.getEndTime());

        Product saveProduct = productService.add(product);
        if (saveProduct == null) {
            result.error500("Failed to add product");
            return result;
        }
        else {
            result.success("Add Product Successfully!");
            result.setResult(saveProduct);
            return result;
        }
    }
    @PostMapping(value = "/subProduct/add")
    public MessageResult<SubProduct> addSubProduct(@RequestBody SubProductVo subProductVo){
        MessageResult<SubProduct> result = new MessageResult<>();
        SubProduct subProduct = new SubProduct();

        subProduct.setProductId(subProductVo.getProductId());
        subProduct.setBuyStatus(0);
        subProduct.setName(subProductVo.getName());
        subProduct.setUnitPrice(subProductVo.getUnitPrice());
        subProduct.setImageUrl(subProductVo.getImageUrl());
        subProduct.setFromImgUrl(subProductVo.getFromImgUrl());
        subProduct.setDescription(subProductVo.getDescription());
        subProduct.setCreateTime(new Date());

        Product product = productService.findByProductId(subProductVo.getProductId());
        BigDecimal totalUnitPrice = product.getTotalUnitPrice();
         if(Objects.equals(totalUnitPrice,null)){
              totalUnitPrice= new BigDecimal("0.00");
         }
        totalUnitPrice = totalUnitPrice.add(subProductVo.getUnitPrice());

        product.setTotalUnitPrice(totalUnitPrice);
        SubProduct saveSubProduct = subProductService.add(subProduct);
        productService.update(product);

        if (saveSubProduct == null) {
            result.error500("Failed to add subProduct!");
            return result;
        }
        result.setSuccess(true);
        result.setCode(CommonConstant.OK_200);
        result.setMessage("Add successfully!");
        result.setResult(saveSubProduct);
        return result;

    }
    @GetMapping(value = "/pending-request")
    public MessageResult<?>  reviewJoin(){
        MessageResult<?> result = new MessageResult<>();
        List<ProductRecord> productRecordList = productRecordService.findAll();
        List<ProductRecord> records = new ArrayList<>();

        if(productRecordList.size()>0){
            result.error500("No request pending");
            return result;
        }

        for (ProductRecord productRecord: productRecordList){
            if (productRecord.getBuyStatus()==1) {
                records.add(productRecord);
            }
        }
        result.setSuccess(true);
        result.setCode(CommonConstant.OK_200);
        result.setMessage("review list");
        result.setResult(records);
        return result;
    }


    @GetMapping(value = "/main-product/list")
    public MessageResult<?> list (@RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize,@RequestParam(value = "name",required = false)String name){

        MessageResult<?> result = new MessageResult<>();
        Pageable page = PageRequest.of(pageNo - 1,pageSize, Sort.by("endTime").descending());

        Page<Product> productPage=null;
        if(name!=null && !name.isEmpty()){
            productPage = productService.searchByName(name, page);
        }else {
             productPage = productService.findAll(page);
        }

        List<Product> productList = productPage.getContent();
        long totalPages = productPage.getTotalPages();
        int size = productPage.getSize();
        long totalElements = productPage.getTotalElements();
        Map<String,Object> map = new HashMap<>();
        map.put("totalPages",totalPages);
        map.put("size",size);
        map.put("totalElement",totalElements);
        map.put("list",productList);
        if(productList.size()>0){



            result.setSuccess(true);
            result.setCode(CommonConstant.OK_200);
            result.setMessage("product list");
            result.setResult(map);

            return result;
        }else {
            result.error500("No product list");
            return result;
        }
    }

    /*
    Delete product with his related subs
     */
    @DeleteMapping("/delete")
    public MessageResult<?> deleteProduct(@RequestParam("productId")Long productId){
        MessageResult<?> result = new MessageResult<>();

        Product product = productService.findByProductId(productId);

        List<SubProduct> subProducts = subProductService.findByProductId(product.getId());

        if(subProducts.size()>0){
            productService.delete(product.getId());
            for(SubProduct subProduct: subProducts){
                if(Objects.equals(product.getId(), subProduct.getProductId())){
                    subProductService.delete(subProduct.getId());
                    result.success("Deleted success!");
                }
            }
            return result;

        }else {
            productService.delete(product.getId());
            result.success("Deleted success");
            return result;
        }
    }

    @PutMapping("/update")
    public MessageResult<?> update(@RequestBody ProductVo productVo){
        MessageResult<?> result =new MessageResult<>();
      Product product =  productService.findByProductId(productVo.getId());

      if(product!=null){
         if(productVo.getFree()!=null ){
             product.setFree(productVo.getFree());
         }if(productVo.getBuyAmount()!=null){
             product.setBuyAmount(productVo.getBuyAmount());
          }if(productVo.getName()!=null){
             product.setName(productVo.getName());
          }if(productVo.getStartTime()!=null){
             product.setStartTime(productVo.getStartTime());
          }if(productVo.getEndTime()!=null) {
              product.setEndTime(productVo.getEndTime());
          }
          Product update = productService.update(product);

          result.success("Update Success");
         return result;



          
      }else {
          result.error500("Operation failed");
          return result;
      }
    }

}
