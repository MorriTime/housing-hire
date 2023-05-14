package com.hire.common.web.enums;

public enum RoleEnum {
    USER(1, "普通用户"),
    ADMIN(2, "管理员"),
    SUPER(3, "超级管理员");

    private final int id;

    private final String name;


    RoleEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
