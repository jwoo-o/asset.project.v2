package com.gen.vacation.global.Enum;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-08-24
 * Time: 오후 3:52
 */
public enum TokenEnum implements GrantedAuthority {

    USER("ROLE_USER", "사용자"),
    ADMIN("ROLE_ADMIN", "관리자");

    private String authority;
    private String description;

    private TokenEnum(String authority, String description) {
        this.authority = authority;
        this.description = description;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public String getDescription() {
        return description;
    }
}
