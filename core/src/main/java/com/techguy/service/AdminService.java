package com.techguy.service;

import com.techguy.entity.Member;
import com.techguy.entity.admin.Admin;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AdminService extends UserDetailsService {
    Admin findByEmail(String email);

    Admin register( String encodePW, String email,String secrectKey);

    Admin update(Admin admin);

   
}
