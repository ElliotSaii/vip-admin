package com.techguy.record;

import com.techguy.constant.CommonConstant;
import com.techguy.entity.Member;
import com.techguy.entity.product.Product;
import com.techguy.entity.product.ProductRecord;
import com.techguy.response.MessageResult;
import com.techguy.service.MemberService;
import com.techguy.service.ProductRecordService;
import com.techguy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/product/record")
public class ProductRecordController {
    private final ProductRecordService productRecordService;
    private final MemberService memberService;
    private final ProductService productService;

    @Autowired
    public ProductRecordController(ProductRecordService productRecordService,MemberService memberService,ProductService productService){
        this.productRecordService = productRecordService;
        this.memberService = memberService;
        this.productService = productService;
    }

    @PostMapping("/join")
    public MessageResult<?> join(@RequestParam("memberId")Long memberId, @RequestParam("productId")Long productId){
        MessageResult<?> result = new MessageResult<>();

        Member member = memberService.findByMemberId(memberId);
        Product product = productService.findByProductId(productId);
        ProductRecord productRecord = new ProductRecord();


        if (member !=null && product !=null){
            //0 not request : 1 : pending 2: approve by admin
            productRecord.setBuyStatus(1);
            productRecord.setMemberId(member.getId());
            productRecord.setCreateTime(new Date());
            productRecord.setProductId(product.getId());

            member.setBuyStatus(1);
            memberService.update(member);

             //save to record
            ProductRecord productRecord1 = productRecordService.save(productRecord);
            result.setSuccess(true);
            result.setMessage("Successful joined");
            result.setResult(productRecord1);
        }
        return result;
    }
  /*  @GetMapping("/list")
    public MessageResult<Product> list(@RequestParam("memberId")Long memberId){
        MessageResult<Product> result = new MessageResult<>();


        List<ProductRecord> recordList = productRecordService.findByMemberId(memberId);

        ProductRecord productRecord = new ProductRecord();

        for (ProductRecord record : recordList) {
            productRecord = record;
        }
        Long productId = productRecord.getProductId();
        Long recordMemberId = productRecord.getMemberId();

        List<Product> productList = productService.getProductList();

        //update product for each user
        if(productList.size()>0){
          productList.forEach(product -> {
             if( product.getMemberId() == recordMemberId && product.getId() == productId){
                 product.setBuyStatus(2);
                 product.setMemberId(recordMemberId);
                 productService.update(product);

                 result.setSuccess(true);
                 result.setCode(CommonConstant.OK_200);
                 result.setMessage("Products List");
                 result.setResult(product);
             }
          });
        }
        return  result;
    }
*/
}
