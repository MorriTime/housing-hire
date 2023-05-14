package com.hire.common.base.response;


public enum BaseResponseEnum implements BaseResponse {

    SUCCESS(0,"SUCCESS"),
    ERROR(1001,"ERROR"),
    UNEXPECTED(-1, "UNEXPECTED"),
    SQL_ERROR(-2, "SQL_ERROR")
    ;

    private final int code;
    private final String msg;


    BaseResponseEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode(){
        return code;
    }
    public String getMsg(){
        return msg;
    }

}
