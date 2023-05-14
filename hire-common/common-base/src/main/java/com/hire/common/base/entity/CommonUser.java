package com.hire.common.base.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommonUser implements Serializable {

    private Long id;

    private String username;

    private String password;

    private String nick;

    private String avatar;

    private String phone;

    private Boolean sex;

    private String create_time;

    private String update_time;
}
