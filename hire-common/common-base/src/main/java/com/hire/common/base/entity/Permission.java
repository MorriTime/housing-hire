package com.hire.common.base.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;

@Data
@Accessors(chain = true)
public class Permission implements GrantedAuthority {

    private String url;

    private String name;

    private String description;

    private Long pid;

    @Override
    public String getAuthority() {
        return name;
    }
}
