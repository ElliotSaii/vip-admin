package com.techguy.repository;

import com.techguy.entity.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt,Long> {

    LoginAttempt findByIpAddress(String ipAddress);
}
