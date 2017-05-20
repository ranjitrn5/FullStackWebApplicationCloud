package com.springfullstackcloudapp.backend.persistence.domains.backend;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by ranji on 5/20/2017.
 */
public class Authority implements GrantedAuthority {

    private final String authority;

    public Authority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return null;
    }
}
