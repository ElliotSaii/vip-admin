package com.techguy.service.impl;

import com.techguy.entity.Guide;
import com.techguy.repository.GuideRepository;
import com.techguy.service.GuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GuideServiceImpl implements GuideService {

    private final GuideRepository guideRepository;

    @Autowired
    public GuideServiceImpl(GuideRepository guideRepository) {
        this.guideRepository = guideRepository;
    }

    @Override
    public void save(Guide guide) {
        guideRepository.save(guide);
    }

    @Override
    public Page<Guide> findAll(Pageable page) {
        return guideRepository.findAll(page);
    }

    @Override
    public Guide findById(Long id) {
        return guideRepository.findById(id).orElseThrow(()-> new IllegalArgumentException(
                String.format("Given guide id %s not found",id)
        ));
    }

    @Override
    public void delete(Long id) {
        guideRepository.deleteById(id);
    }
}
