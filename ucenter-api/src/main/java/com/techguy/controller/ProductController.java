package com.techguy.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techguy.constant.CommonConstant;
import com.techguy.dto.ProductDTO;
import com.techguy.entity.Bank;
import com.techguy.entity.product.Product;
import com.techguy.entity.product.ProductRecord;
import com.techguy.entity.product.SubProduct;
import com.techguy.response.MessageResult;
import com.techguy.service.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@RequestMapping(value = "/api/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final MemberService memberService;
    private final ProductRecordService productRecordService;
    private final BankService bankService;
    private final RedisTemplate redisTemplate;
    private final SubProductService subProductService;



    @GetMapping("/list")
    public MessageResult<?> listRecord(@RequestParam("memberId") Long memberId, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        MessageResult<?> result = new MessageResult<>();
        Pageable page = PageRequest.of(pageNo, pageSize);

        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date =new Date();

        String currentTime = dateFormat.format(date);

        Page<Product> productPage = productService.findProductBetweenStartTimeAndEndTime(currentTime,page);

        List<Product> productList = productPage.getContent();
        BigDecimal totalUnitPrice = new BigDecimal("0.00");

      Integer type =1;


        List<ProductDTO> productDTOList = new ArrayList<>();

        for (Product product : productList) {
            ProductRecord productRecord = productRecordService.findByMemberIdAndProductId(memberId,product.getId(),type);
            ProductDTO productDTO = new ProductDTO();


            if(productRecord!=null){
                if (product.getId().equals(productRecord.getProductId())) {
                    productDTO.setProductRecord(productRecord);
                    productDTO.setId(product.getId());
                    productDTO.setBuyStatus(product.getBuyStatus());
                    productDTO.setName(product.getName());
                    productDTO.setFree(product.getFree());
                    productDTO.setBuyAmount(product.getBuyAmount());
                    productDTO.setMemberId(product.getMemberId());
                    productDTO.setTotalUnitPrice(product.getTotalUnitPrice());
                    productDTO.setStartTime(product.getStartTime());
                    productDTO.setEndTime(product.getEndTime());
                    productDTOList.add(productDTO);
                }
            }
            else {
                productDTO.setId(product.getId());
                productDTO.setBuyStatus(product.getBuyStatus());
                productDTO.setName(product.getName());
                productDTO.setFree(product.getFree());
                productDTO.setBuyAmount(product.getBuyAmount());
                productDTO.setMemberId(product.getMemberId());
                productDTO.setTotalUnitPrice(product.getTotalUnitPrice());
                productDTO.setStartTime(product.getStartTime());
                productDTO.setEndTime(product.getEndTime());
                productDTOList.add(productDTO);
            }
        }

            result.success("Product list");
            result.setResult(productDTOList);
            return result;
    }





//    @GetMapping("/list1")
//    public MessageResult<?> list(@RequestParam("memberId") Long memberId, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
//        MessageResult<?> result = new MessageResult<>();
//        Pageable page = PageRequest.of(pageNo, pageSize,Sort.by("createTime").ascending());
//        Map<String,Object> map = new HashMap<>();
//        Page<ProductRecord> productRecordPage = productRecordService.findProductByMemberId(memberId,page);
//
//        List<ProductRecord> productRecords = productRecordPage.getContent();
//        int numberOfElements = productRecordPage.getNumberOfElements();
////        int size = productRecordPage.getSize();
////        int number = productRecordPage.getNumber();
//
//
//        pageSize = (pageSize-numberOfElements);
//        Pageable pPage = PageRequest.of(pageNo,pageSize,Sort.by("createTime").descending());
//
//        Page<Product> productPages = productService.findAll(pPage);
//
//        List<Product> productList = productPages.getContent();
//
//        map.put("Record List",productRecords);
//        map.put("product List",productList);
//
//
//        result.setResult(map);
//
//        List<ProductDTO> productDTOList = new ArrayList<>();
//
//        return result;
//}


    @GetMapping("/bankInfo")
    public MessageResult<?> join(){
        MessageResult<?> result = new MessageResult<>();
        List<Bank> bankList = bankService.getBankList();
        // bank  version 2 =>available 3=>unavailable
      if(bankList.size()>0) {
          for (Bank bank : bankList) {
              if (bank.getBanKVersion() == 2) {
                  result.success("Bank with " + bank.getBanKVersion());
                  result.setResult(bank);
                  return result;
                }
           }
       }
      else {
          result.error500("No Bank available now");
          return result;
      }
     return result;
    }

    public static void main(String [] args) throws ParseException {

        Date currentTime =new Date();

        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(currentTime);
        Date finalD =DateFormat.getDateInstance().parse(format);
        System.out.println(finalD);
    }
}
