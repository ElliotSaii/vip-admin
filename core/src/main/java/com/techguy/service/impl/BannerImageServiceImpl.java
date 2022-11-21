package com.techguy.service.impl;

import com.techguy.entity.BannerImage;
import com.techguy.repository.BannerImageRepository;
import com.techguy.service.BannerImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerImageServiceImpl implements BannerImageService {
    private final BannerImageRepository bannerImageRepository;

    @Autowired
    public BannerImageServiceImpl(BannerImageRepository bannerImageRepository) {
        this.bannerImageRepository = bannerImageRepository;
    }

    @Override
    public void save(BannerImage bannerImage) {
        bannerImageRepository.save(bannerImage);
    }

    @Override
    public List<BannerImage> findAll() {
        return bannerImageRepository.findAll();
    }

    @Override
    public BannerImage findById(Long id) {
        return bannerImageRepository.findById(id).orElseThrow(()->new IllegalArgumentException(
                String.format("Given banner id %s not found",id)
        ));
    }

    @Override
    public void delete(Long id) {
        bannerImageRepository.deleteById(id);
    }
}
