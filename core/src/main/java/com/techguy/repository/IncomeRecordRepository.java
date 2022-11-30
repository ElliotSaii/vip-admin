package com.techguy.repository;

import com.techguy.entity.IncomeRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRecordRepository extends PagingAndSortingRepository<IncomeRecord,Long> {

    Page<IncomeRecord> findByMemberId(Long memberId, Pageable pageable);

    Page<IncomeRecord> findByMemberIdAndNameContaining(Long memberId,String name,Pageable pageable);

    @Query(value = "SELECT * FROM income_record  WHERE :memberId=member_id AND date(:startDate)<=date(create_time) AND date(create_time)<= date(:endDate)",nativeQuery = true)
    Page<IncomeRecord> findStartAndEndDate(@Param("memberId")Long memberId,@Param("startDate")String startDate,@Param("endDate")String endDate,Pageable pageable);

    @Query(value = "SELECT * FROM income_record  WHERE :memberId=member_id AND name like %:name% AND date(:startDate)<=date(create_time) AND date(create_time)<= date(:endDate)",nativeQuery = true)
    Page<IncomeRecord> findStartAndEndDateAndName(@Param("memberId")Long memberId,@Param("name")String name,@Param("startDate")String startDate,@Param("endDate")String endDate,Pageable pageable);

    @Query(value = "SELECT * FROM income_record  WHERE :memberId=member_id AND date(:date)=date(create_time)",nativeQuery = true)
    Page<IncomeRecord> findByMemberIdAndCreateTime(@Param("memberId") Long memberId,@Param("date") String date,Pageable pageable);
}
