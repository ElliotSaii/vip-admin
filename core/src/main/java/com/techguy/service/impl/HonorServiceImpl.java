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

    @Override
    public Honor findById(Long id) {
        return honorRepository.findById(id).orElseThrow(()->new IllegalArgumentException(
                String.format("Id %s not found",id)
        ));
    }

    @Override
    public void update(Honor honor) {
      Honor honor1=  honorRepository.findById(honor.getId()).orElseThrow(()->new IllegalArgumentException(
                String.format("Id %s not found",honor.getId())
        ));

      honor1.setImageUrl(honor.getImageUrl());
      honor1.setDescription(honor.getDescription());
      honor1.setEmail(honor.getEmail());

      honorRepository.save(honor1);
    }
}
