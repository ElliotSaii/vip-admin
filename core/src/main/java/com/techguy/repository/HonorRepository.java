package com.techguy.repository;

import com.techguy.entity.Honor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HonorRepository extends PagingAndSortingRepository<Honor,Long> {
}
