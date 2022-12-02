package com.techguy.service;

import com.techguy.entity.Honor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HonorService {
    void save(Honor honor);

    Page<Honor> findAll(Pageable page);

    List<Honor> findAll();

    Honor findById(Long id);

    void update(Honor honor);
}
