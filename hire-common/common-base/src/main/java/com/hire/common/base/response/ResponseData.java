package com.hire.common.base.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hire.common.base.exception.BizException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData<T> {

    private T data;
    private final int code;
    private String msg;

    private ResponseData(int code) {
        this.code = code;
    }

    private ResponseData(int code, T data) {
        this.code = code;
        this.data = data;
    }

    private ResponseData(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private ResponseData(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @JsonIgnore
    //使之不在json序列化结果当中
    public boolean isSuccess() {
        return this.code == BaseResponseEnum.SUCCESS.getCode();
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public static <T> ResponseData<T> Create(BaseResponse e) {
        return new ResponseData<>(e.getCode(), e.getMsg());
    }

    public static <T> ResponseData<T> Create(BizException e) {
        return new ResponseData<>(e.getCode(), e.getMsg());
    }

    public static <T> ResponseData<T> Success() {
        return new ResponseData<>(BaseResponseEnum.SUCCESS.getCode(), BaseResponseEnum.SUCCESS.getMsg());
    }

    public static <T> ResponseData<T> Success(String msg) {
        return new ResponseData<>(BaseResponseEnum.SUCCESS.getCode(), msg);
    }

    public static <T> ResponseData<T> Success(T data) {
        return new ResponseData<>(BaseResponseEnum.SUCCESS.getCode(), data);
    }

    public static <T> ResponseData<T> Success(String msg, T data) {
        return new ResponseData<>(BaseResponseEnum.SUCCESS.getCode(), msg, data);
    }


    public static <T> ResponseData<T> Error() {
        return new ResponseData<>(BaseResponseEnum.ERROR.getCode(), BaseResponseEnum.ERROR.getMsg());
    }


    public static <T> ResponseData<T> Error(String msg) {
        return new ResponseData<>(BaseResponseEnum.ERROR.getCode(), msg);
    }

    public static <T> ResponseData<T> Error(int code, String msg) {
        return new ResponseData<>(code, msg);
    }
}
