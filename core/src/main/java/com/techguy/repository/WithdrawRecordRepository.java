package com.techguy.repository;

import com.techguy.entity.WithdrawRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawRecordRepository extends PagingAndSortingRepository<WithdrawRecord,Long> {

    Page<WithdrawRecord> findAllByStatus(Pageable pageable,Integer status);

    Page<WithdrawRecord> findAllByMemberId(Long memberId,Pageable pageable);

    @Query(value = "SELECT * FROM withdraw_record where :memberId= member_id and :status=status and date(:startDate)<=date(create_time) and date(create_time)<= date(:endDate)",nativeQuery = true)
    Page<WithdrawRecord> findBetweenStarAndEnd(@Param("memberId") Long memberId,@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("status") Integer status, Pageable pageable);

    @Query(value = "SELECT * FROM withdraw_record where :memberId= member_id  and date(:startDate)<=date(create_time) and date(create_time)<= date(:endDate)",nativeQuery = true)
    Page<WithdrawRecord> findBetweenStarAndEnd(@Param("memberId") Long memberId,@Param("startDate") String startDate,@Param("endDate") String endDate, Pageable pageable);

    @Query(value = "SELECT * FROM withdraw_record where :memberId= member_id and :status=status and date(:day)=date(create_time) ",nativeQuery = true)
    Page<WithdrawRecord> findByDay(@Param("memberId") Long memberId,@Param("day") String day,@Param("status") Integer status, Pageable pageable);

    Page<WithdrawRecord> findByMemberIdAndStatus(Long memberId,Integer status, Pageable page);
}
