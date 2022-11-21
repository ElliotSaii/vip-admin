package com.techguy.service;

import com.techguy.entity.BannerImage;

import java.util.List;

public interface BannerImageService {
    void save(BannerImage bannerImage);

    List<BannerImage> findAll();

    BannerImage findById(Long id);

    void delete(Long id);
}
