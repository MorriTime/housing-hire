package com.hire.common.web.enums;

public enum ReportEnum {
    WARMING_PRICE(1, "价格"),
    WARMING_CONTENT(2, "内容"),

    WARMING_COMMENT(3, "评论"),
    WARMING_OTHER(4, "其他");


    private final int id;

    private final String name;


    ReportEnum(int id, String name) {
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
