package com.techguy.repository;

import com.techguy.entity.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppVersionRepository extends JpaRepository<AppVersion,Long> {
    AppVersion findByPlatform(Integer platform);
}
