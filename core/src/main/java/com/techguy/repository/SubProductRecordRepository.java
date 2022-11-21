package com.techguy.repository;

import com.techguy.entity.product.SubProductRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubProductRecordRepository  extends JpaRepository<SubProductRecord,Long> {

    List<SubProductRecord> findByProductIdAndMemberId(Long productId,Long memberId);
}
