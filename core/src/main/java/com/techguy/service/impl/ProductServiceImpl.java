package com.techguy.service.impl;

import com.techguy.entity.product.Product;
import com.techguy.repository.ProductRepository;
import com.techguy.service.MemberService;
import com.techguy.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final MemberService memberService;
    @Override
    public List<Product> getProductList() {
    List<Product> list = new ArrayList<>();
//        Pageable page = PageRequest.of(pageNo,pageSize,Sort.by("createTime").descending());
        Iterable<Product> products = productRepository.findAll();
        for (Product product : products) {
            list.add(product);
        }
       return  list;
    }

    @Override
    public Product findByMemberId(Long memberId) {
        return productRepository.findById(memberId).orElseThrow(()->new IllegalArgumentException(
                String.format("Member with id %s not found",memberId)
        ));
    }

    @Override
    public Product add(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product findByProductId(Long productId) {
        return productRepository.findById(productId).orElseThrow(()-> new IllegalArgumentException(
                String.format("Product with id %s not exit",productId)
        ));
    }

    @Override
    public Product update(Product product) {
        Product prod = productRepository.findById(product.getId()).orElseThrow(()->new IllegalArgumentException("update failed"));

        if (prod !=null){
//            prod.setBuyStatus(product.getBuyStatus());
//            prod.setMemberId(product.getMemberId());
            prod.setFree(product.getFree());
            prod.setName(product.getName());
            prod.setBuyAmount(product.getBuyAmount());
            prod.setStartTime(product.getStartTime());
            prod.setEndTime(product.getEndTime());
            prod.setTotalUnitPrice(product.getTotalUnitPrice());
            prod = productRepository.save(prod);
        }
        return prod;
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

//    @Override
//    public Page<Product> findAll(Pageable pageable, Long memberId) {
//        return productRepository.findAll(pageable,memberId);
//    }

    @Override
    public Page<Product> findProductBetweenStartTimeAndEndTime(String startTime,Pageable pageable) {

        return productRepository.findProductBetweenStartTimeAndEndTime(startTime,pageable);
    }

    @Override
    public List<Product> findAll() {
     return (List<Product>) productRepository.findAll();
    }



    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<Product> searchByName(String name, Pageable page) {
        return productRepository.findByNameIsContaining(name,page);
    }
}
