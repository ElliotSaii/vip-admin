package com.techguy.security;

import com.techguy.role.Roles;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class SecurityUserDetails extends User {
    private Long id;
    private Roles roles;

    public SecurityUserDetails(Long id, String email, String password, Roles roles){
        super(email,password, AuthorityUtils.createAuthorityList(roles.getRoleName()));
        this.id=id;
        this.roles=roles;
    }

    public Long getId() {
        return id;
    }

    public Roles getRoles() {
        return roles;
    }
}
