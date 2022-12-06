package com.techguy.service.impl;

import com.techguy.code.OnlyCodeUtils;
import com.techguy.entity.Member;
import com.techguy.entity.admin.Admin;
import com.techguy.repository.AdminRepository;
import com.techguy.role.Roles;
import com.techguy.security.SecurityUserDetails;
import com.techguy.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;



    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }

    @Override
    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    @Override
    public Admin register( String encodePW, String email,String sercetKey) {

            Admin saveAdmin=new Admin();
            saveAdmin.setEmail(email);
            saveAdmin.setPassword(encodePW);
            saveAdmin.setCreateTime(new Date());
            saveAdmin.setSecrectKey(sercetKey);
            saveAdmin.setRoles(Roles.ADMIN);
            return adminRepository.save(saveAdmin);
    }

    @Override
    public Admin update(Admin admin) {
        Admin admin1 = adminRepository.findById(admin.getId()).orElseThrow(() -> new IllegalArgumentException(
                String.format("Admin with id : %s not found", admin.getId())
        ));
        if (admin1!=null){
            admin1.setToken(admin.getToken());
            adminRepository.save(admin1);
        }
        return admin1;

    }



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(email);
        return new SecurityUserDetails(admin.getId(),admin.getEmail(),admin.getPassword(),admin.getRoles());
    }
}
