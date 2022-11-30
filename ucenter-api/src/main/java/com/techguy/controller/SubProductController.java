package com.techguy.controller;

import com.techguy.config.LocaleMessageSourceService;
import com.techguy.constant.CommonConstant;
import com.techguy.dto.SubProductDTO;
import com.techguy.entity.Member;
import com.techguy.entity.product.Product;
import com.techguy.entity.product.ProductRecord;
import com.techguy.entity.product.SubProduct;
import com.techguy.entity.product.SubProductRecord;
import com.techguy.response.MessageResult;
import com.techguy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/sub-product")
public class SubProductController {
    private final ProductService productService;
    private final SubProductService subProductService;
    private final MemberService memberService;
    private final SubProductRecordService subProductRecordService;
    private final ProductRecordService productRecordService;
    private final LocaleMessageSourceService messageSourceService;

    @Autowired
    public SubProductController(ProductService productService, SubProductService subProductService, MemberService memberService, SubProductRecordService subProductRecordService, ProductRecordService productRecordService, LocaleMessageSourceService messageSourceService){
        this.productService=productService;
        this.subProductService=subProductService;
        this.memberService =memberService;
        this.subProductRecordService = subProductRecordService;
        this.productRecordService = productRecordService;
        this.messageSourceService = messageSourceService;
    }
//todo delete
    /**
     *
     * @param productId
     * @param memberId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public MessageResult<?> list(@RequestParam("productId")Long productId,@RequestParam(value = "memberId",required = false)Long memberId,@RequestParam("pageNo")Integer pageNo,
                                 @RequestParam("pageSize")Integer pageSize){

        Pageable page = PageRequest.of(pageNo,pageSize);
        MessageResult<?> result = new MessageResult<>();
        Product product = productService.findByProductId(productId);
         memberService.findByMemberId(memberId);
        Page<SubProduct> subProductPage = subProductService.findSubProductByProductId(product.getId(), page);
        List<SubProduct> subProductList = subProductPage.getContent();
        List<SubProduct> tempSubProductList = new ArrayList<>();
        List<ProductRecord>  ProductRecords=productRecordService.findByProductIdAndMemberId(productId,memberId);
        if(!(subProductList.size() >0)){
            result.error500("No sub product list");
            return result;
        }
        if (ProductRecords.size()>0){
            for (ProductRecord productRecord : ProductRecords){
                Long subProductId = productRecord.getSubProductId();
                Integer buyStatus = productRecord.getBuyStatus();

                if(subProductId!=null && productRecord.getProductType()==2) {
                    SubProduct subProduct = subProductService.findBySubProductId(subProductId);
                    if (subProduct == null) {
                        result.error500("Operation failed");
                        return result;
                    }
                    subProduct.setId(subProductId);
                    subProduct.setBuyStatus(buyStatus);
                    subProductService.update(subProduct);
                }
            }
            Page<SubProduct> subProductPages = subProductService.findSubProductByProductId(product.getId(), page);
            tempSubProductList = subProductPages.getContent();
            result.setSuccess(true);
            result.setCode(CommonConstant.OK_200);
            result.setMessage("Sub product list");
            result.setResult(tempSubProductList);
            return result;
        }
            else {
                SubProduct temp = new SubProduct();
                for (SubProduct sb : subProductList){
                    temp.setBuyStatus(0);
                    temp.setId(sb.getId());
                    subProductService.update(temp);
                }
            Page<SubProduct> subProductPage1 = subProductService.findSubProductByProductId(product.getId(), page);
               tempSubProductList = subProductPage1.getContent();
                result.setSuccess(true);
                result.setMessage("SubProduct List");
                result.setCode(CommonConstant.OK_200);
                result.setResult(tempSubProductList);
                return result;
            }

    }

    @GetMapping("/listSub")
    public MessageResult<?> listSub(@RequestParam("productId")Long productId,@RequestParam(value = "memberId",required = false)Long memberId,@RequestParam("pageNo")Integer pageNo,
                                 @RequestParam("pageSize")Integer pageSize) {
        MessageResult<?> result =new MessageResult<>();
        Pageable page =PageRequest.of(pageNo,pageSize, Sort.by("createTime").descending());

        Integer type =2;
        Page<SubProduct> subProductPage = subProductService.findSub(productId,page);
        List<SubProduct> subProductList = subProductPage.getContent();

        List<SubProductDTO> subProductDTOList =new ArrayList<>();

        if(subProductList.size()>0) {
        for(SubProduct subProduct: subProductList) {
            ProductRecord subInRecord = productRecordService.findSubInRecord(subProduct.getId(), productId, memberId, type);

            SubProductDTO subProductDTO = new SubProductDTO();


            if (subInRecord!=null && subInRecord.getSubProductId().equals(subProduct.getId())) {
                subProductDTO.setProductRecord(subInRecord);
                subProductDTO.setId(subProduct.getId());
                subProductDTO.setBuyStatus(subProduct.getBuyStatus());
                subProductDTO.setName(subProduct.getName());
                subProductDTO.setFree(subProduct.getFree());
                subProductDTO.setUnitPrice(subProduct.getUnitPrice());
                subProductDTO.setMemberId(subProduct.getMemberId());

                subProductDTO.setImageUrl(subProduct.getImageUrl());
                subProductDTO.setFromImgUrl(subProduct.getFromImgUrl());
                subProductDTO.setCreateTime(subProduct.getCreateTime());
                subProductDTO.setMemberId(subInRecord.getMemberId());
                subProductDTO.setDescription(subProduct.getDescription());
                subProductDTO.setProductId(subProduct.getProductId());
                subProductDTOList.add(subProductDTO);
            } else {
                subProductDTO.setId(subProduct.getId());
                subProductDTO.setBuyStatus(subProduct.getBuyStatus());
                subProductDTO.setName(subProduct.getName());
                subProductDTO.setFree(subProduct.getFree());
                subProductDTO.setUnitPrice(subProduct.getUnitPrice());


                subProductDTO.setImageUrl(subProduct.getImageUrl());
                subProductDTO.setFromImgUrl(subProduct.getFromImgUrl());
                subProductDTO.setCreateTime(subProduct.getCreateTime());
                subProductDTO.setDescription(subProduct.getDescription());
                subProductDTO.setProductId(subProduct.getProductId());
                subProductDTOList.add(subProductDTO);
            }
        }
            result.success("Sub product List");
            result.setResult(subProductDTOList);
            return result;
        }else {
            result.error500(messageSourceService.getMessage("NO_PRODUCT"));
            return result;
        }
    }

    @PostMapping("/submit")
    public MessageResult<?> submitSubProduct(@RequestParam("memberId")Long memberId, @RequestParam("subProductId")Long subProductId,
                                             @RequestParam("productId")Long productId, @RequestParam("imageUrl")String imageUrl, @RequestParam("adminImageUrl")String adminImageUrl,
                                             @RequestParam("fromImageUrl")String fromImageUrl){
        MessageResult<?> result =new MessageResult<>();
        Member member = memberService.findByMemberId(memberId);
        Product product = productService.findByProductId(productId);
        SubProduct subProduct = subProductService.findBySubProductId(subProductId);
        ProductRecord productRecord = productRecordService.findByMemberIdAndSubProductId(memberId,subProductId);

        ProductRecord record = new ProductRecord();
        SubProductRecord subRecord  = new SubProductRecord();

        if(!Objects.equals(subProduct.getProductId(), productId)){
            result.error500(messageSourceService.getMessage("OPERATION_FAIL"));
            return result;
        }
        if (member!=null && product!=null && subProduct!=null &&productRecord==null){
           record.setProductId(productId);
           record.setSubProductId(subProductId);
           record.setBuyStatus(1);
           record.setName(subProduct.getName());
           record.setMemberId(memberId);
           record.setImageUrl(imageUrl);
           record.setProductType(2);
           record.setCreateTime(new Date());
           record.setUnitPrice(subProduct.getUnitPrice());
           record.setAdminImgUrl(adminImageUrl);
           record.setFromImgUrl(fromImageUrl);

            ProductRecord saveProductRecord = productRecordService.save(record);

            subRecord.setProductId(productId);
            subRecord.setSubProductId(subProductId);
            subRecord.setBuyStatus(1);
            subRecord.setName(subProduct.getName());
            subRecord.setMemberId(memberId);
            subRecord.setImageUrl(imageUrl);
            subRecord.setCreateTime(new Date());
            subRecord.setUnitPrice(subProduct.getUnitPrice());
            SubProductRecord saveSubProductRecord = subProductRecordService.save(subRecord);
//           ProductRecord productRecord1 = new ProductRecord();

            if (saveProductRecord == null && saveSubProductRecord==null) {
                result.error500(messageSourceService.getMessage("OPERATION_FAIL"));
                return result;
            }



            result.success(messageSourceService.getMessage("OPERATION_SUCCESS"));
            result.setResult(saveProductRecord);
            return result;
        }
        if(productRecord!=null && productRecord.getBuyStatus()==3) {
            record.setId(productRecord.getId());
            record.setProductId(productId);
            record.setSubProductId(subProductId);
            record.setBuyStatus(1);
            record.setName(subProduct.getName());
            record.setMemberId(memberId);
            record.setImageUrl(imageUrl);
            record.setProductType(2);
            record.setCreateTime(new Date());
            record.setUnitPrice(subProduct.getUnitPrice());
            record.setAdminImgUrl(adminImageUrl);
            record.setFromImgUrl(fromImageUrl);

            ProductRecord saveProductRecord = productRecordService.update(record);

            result.success(messageSourceService.getMessage("OPERATION_SUCCESS"));
            result.setResult(saveProductRecord);
            return result;
        }
        return result;
    }
}
