package com.techguy.repository;

import com.techguy.entity.Guide;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuideRepository extends PagingAndSortingRepository<Guide,Long> {
}
