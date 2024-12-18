package org.taxi.entity.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    CLIENT,
    DRIVER,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
