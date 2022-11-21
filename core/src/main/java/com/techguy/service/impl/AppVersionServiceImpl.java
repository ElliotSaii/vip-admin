package com.techguy.service.impl;

import com.techguy.entity.AppVersion;
import com.techguy.repository.AppVersionRepository;
import com.techguy.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppVersionServiceImpl implements AppVersionService {
    private final AppVersionRepository appVersionRepository;

    @Autowired
    public AppVersionServiceImpl(AppVersionRepository appVersionRepository) {
        this.appVersionRepository = appVersionRepository;
    }

    @Override
    public void save(AppVersion appVersion) {
        appVersionRepository.save(appVersion);
    }

    @Override
    public List<AppVersion> find() {
        return appVersionRepository.findAll();
    }

    @Override
    public AppVersion findByPlatform(Integer findByPlatform) {
        return appVersionRepository.findByPlatform(findByPlatform);
    }

    @Override
    public AppVersion update(AppVersion appVersion) {

        AppVersion updateApp = appVersionRepository.findById(appVersion.getId()).orElseThrow(()->new IllegalArgumentException(
                String.format("App version id %s not found",appVersion.getId())
        ));
        if(updateApp!=null){
            updateApp.setName(appVersion.getName());
            updateApp.setVersion(appVersion.getVersion());
            updateApp.setDescription(appVersion.getDescription());
            updateApp.setLink(appVersion.getLink());
            updateApp.setPlatform(appVersion.getPlatform());
            updateApp.setUpdateTime(appVersion.getUpdateTime());
            return  appVersionRepository.save(updateApp);
        }else {
            return null;
        }

    }

    @Override
    public AppVersion findById(Long id) {
        AppVersion appVersion;
        try{
             appVersion = appVersionRepository.findById(id).orElseThrow(()->
                    new IllegalArgumentException(String.format("Given app id not found %s",id)));
        }catch (NullPointerException exception){
            return null;
        }
        return   appVersion;

    }
}
