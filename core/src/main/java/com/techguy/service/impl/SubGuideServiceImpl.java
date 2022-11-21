package com.techguy.service.impl;

import com.techguy.entity.SubGuide;
import com.techguy.repository.SubGuideRepository;
import com.techguy.service.SubGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubGuideServiceImpl implements SubGuideService {
    private final SubGuideRepository subGuideRepository;

    @Autowired
    public SubGuideServiceImpl(SubGuideRepository subGuideRepository) {
        this.subGuideRepository = subGuideRepository;
    }

    @Override
    public void save(SubGuide subGuide) {
        subGuideRepository.save(subGuide);
    }

    @Override
    public Page<SubGuide> findAllPage(Long id,Integer type, Pageable page) {
        return subGuideRepository.findByGuideIdAndType(id,type,page);
    }

    @Override
    public List<SubGuide> findByGuideId(Long id) {
        return subGuideRepository.findByGuideId(id);
    }

    @Override
    public SubGuide findById(Long id) {
      return  subGuideRepository.findById(id).orElseThrow(()->new IllegalArgumentException(
              String.format("Given sub id %s not found",id)
      ));
    }

    @Override
    public void delete(Long id) {
        subGuideRepository.deleteById(id);
    }

    @Override
    public SubGuide update(SubGuide sGuide) {
        SubGuide subGuide = subGuideRepository.findById(sGuide.getId()).orElseThrow(() -> new IllegalArgumentException(
                String.format("Given id % not found", sGuide.getId())
        ));

        subGuide.setId(sGuide.getId());
        subGuide.setTitle(sGuide.getTitle());
        subGuide.setDescription(sGuide.getDescription());
       return subGuideRepository.save(subGuide);
    }
}
