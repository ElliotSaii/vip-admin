package com.techguy.repository;

import com.techguy.entity.SubGuide;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubGuideRepository extends PagingAndSortingRepository<SubGuide,Long> {

    Page<SubGuide> findByGuideIdAndType(Long guideId,Integer type, Pageable pageable);

    List<SubGuide> findByGuideId(Long guideId);
}
