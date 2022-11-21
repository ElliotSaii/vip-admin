package com.techguy.service;

import com.techguy.entity.SubGuide;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubGuideService {
    void save(SubGuide subGuide);

    Page<SubGuide> findAllPage(Long id,Integer type, Pageable page);

    List<SubGuide> findByGuideId(Long id);

    SubGuide findById(Long id);

    void delete(Long id);

    SubGuide update(SubGuide subGuide);
}
