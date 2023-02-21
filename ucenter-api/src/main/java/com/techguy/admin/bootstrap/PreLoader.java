package com.techguy.admin.bootstrap;

import com.techguy.entity.admin.Admin;
import com.techguy.repository.AdminRepository;
import com.techguy.role.Roles;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class PreLoader implements CommandLineRunner {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        loadContext();
    }

    private void loadContext() {
        if(adminRepository.count()==0){
            adminRepository.save(new Admin(500L,null,"viporgcenter@gmail.com",null,passwordEncoder.encode("theviporgcenter"),new Date(),Roles.ADMIN,passwordEncoder.encode("ima34rw3r3wrsdfsefesfd324324324*&^$%w#")
            ));

        }
    }
}
