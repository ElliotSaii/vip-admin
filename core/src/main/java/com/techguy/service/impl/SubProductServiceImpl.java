package com.techguy.service.impl;
import com.techguy.entity.product.SubProduct;
import com.techguy.repository.SubProductRepository;
import com.techguy.service.SubProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubProductServiceImpl implements SubProductService {
    private final SubProductRepository subProductRepository;

    public SubProductServiceImpl(SubProductRepository subProductRepository) {
        this.subProductRepository = subProductRepository;
    }

    @Override
    public SubProduct add(SubProduct subProduct) {
        return subProductRepository.save(subProduct);
    }

    @Override
    public Page<SubProduct> findSubProductByProductId(Long id, Pageable page) {
        return subProductRepository.findByProductId(id,page);
    }

    @Override
    public List<SubProduct> findSubProductByProductId(Long productId) {
        return subProductRepository.findByProductId(productId);
    }

    @Override
    public SubProduct findBySubProductId(Long subProductId) {
        return subProductRepository.findById(subProductId).orElseThrow(()->new IllegalArgumentException(
                   String.format("Subproduct with id : %s not found",subProductId)
           ));
    }

    @Override
    public SubProduct update(SubProduct subProduct) {
        SubProduct subProduct1 = subProductRepository.findById(subProduct.getId()).orElseThrow(() -> new IllegalArgumentException(
                String.format("SubProduct not found with id :%s", subProduct.getProductId())
        ));
        if(subProduct1!=null){
            subProduct1.setFree(subProduct.getFree());
            subProduct1.setImageUrl(subProduct.getImageUrl());
            subProduct1.setUnitPrice(subProduct.getUnitPrice());
            subProduct1.setName(subProduct.getName());
            subProduct1.setDescription(subProduct.getDescription());
            subProduct1.setFromImgUrl(subProduct.getFromImgUrl());
            subProductRepository.save(subProduct1);
        }
        return subProduct1;
    }

    @Override
    public List<SubProduct> findByProductId(Long id) {
        return subProductRepository.findByProductId(id);
    }


    @Override
    public void delete(Long id) {
        subProductRepository.deleteById(id);
    }

    @Override
    public Page<SubProduct> findSub(Long productId, Pageable page) {
        return subProductRepository.findAllByProductId(productId,page);
    }
}
