package com.techguy.repository;

import com.techguy.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MemberRepository extends PagingAndSortingRepository<Member,Long> {
   Member findByEmail(String email);

  /* @Query(value = "SELECT new com.techguy.dto.MemberDTO(id,email,password)"
           + "FROM Member  "+
   "WHERE password=:password AND email=:email")*/

   @Query(value = "select * from Member where email=:email and password=:password",nativeQuery = true)
   Member findByPasswordAndEmail(@Param("password") String password, @Param("email") String email);

   Member findByToken(String token);


   Member findByInvCode(String invCode);


   Page<Member> findByUsernameIsContaining(String name,Pageable pageable);

   Page<Member> findAllByRealNameStatusEquals(Integer status,Pageable pageable);
}
