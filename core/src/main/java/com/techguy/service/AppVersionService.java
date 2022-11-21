package com.techguy.service;

import com.techguy.entity.AppVersion;

import java.util.List;

public interface AppVersionService {
    void save(AppVersion appVersion);

    List<AppVersion> find();

    AppVersion findByPlatform(Integer platform);

    AppVersion update(AppVersion appVersion);

    AppVersion findById(Long id);
}
