package com.techguy.role;

import lombok.Getter;


@Getter
public enum Roles {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String roleName;


    Roles(String roleName) {
        this.roleName = roleName;
    }
}
