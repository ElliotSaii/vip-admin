package com.techguy.service.impl;

import com.techguy.entity.LoginAttempt;
import com.techguy.repository.LoginAttemptRepository;
import com.techguy.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {
    @Autowired
    private LoginAttemptRepository loginAttemptRepository;
    @Override
    public void save(LoginAttempt attempt) {

        loginAttemptRepository.save(attempt);
    }

    @Override
    public LoginAttempt findByIpAddress(String ipAddress) {
        return loginAttemptRepository.findByIpAddress(ipAddress);
    }

    @Override
    public void update(LoginAttempt attempt) {
        Optional<LoginAttempt> optional = loginAttemptRepository.findById(attempt.getId());

        if(optional.isPresent()){
            LoginAttempt loginAttempt = optional.get();
                loginAttempt.setAttemptTime(attempt.getAttemptTime());
                loginAttempt.setIpAddress(attempt.getIpAddress());
                loginAttemptRepository.save(loginAttempt);

        }
    }
}
