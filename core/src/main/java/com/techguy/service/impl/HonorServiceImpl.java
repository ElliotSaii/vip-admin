package com.techguy.service.impl;

import com.techguy.entity.Honor;
import com.techguy.repository.HonorRepository;
import com.techguy.service.HonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HonorServiceImpl implements HonorService {
    private final HonorRepository honorRepository;

    @Autowired
    public HonorServiceImpl(HonorRepository honorRepository) {
        this.honorRepository = honorRepository;
    }

    @Override
    public void save(Honor honor) {
        honorRepository.save(honor);
    }

    @Override
    public Page<Honor> findAll(Pageable page) {
        return honorRepository.findAll(page);
    }

    @Override
    public List<Honor> findAll() {
        return (List<Honor>) honorRepository.findAll();
    }
}
