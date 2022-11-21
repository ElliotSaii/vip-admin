package com.techguy.service;

import com.techguy.entity.Guide;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GuideService {
    void save(Guide guide);

    Page<Guide> findAll(Pageable page);

    Guide findById(Long id);

    void delete(Long id);
}
