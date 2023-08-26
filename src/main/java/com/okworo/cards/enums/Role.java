package com.okworo.cards.enums;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Morris.Okworo on 26/08/2023
 */
@Getter
public enum Role implements GrantedAuthority {
    MEMBER, ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
