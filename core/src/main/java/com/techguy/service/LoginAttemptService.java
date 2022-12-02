package com.techguy.service;

import com.techguy.entity.LoginAttempt;

public interface LoginAttemptService {
    void save(LoginAttempt attempt);

    LoginAttempt findByIpAddress(String ipAddress);

    void update(LoginAttempt attempt);
}
