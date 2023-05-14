package com.hire.common.base.exception;

public class BizException extends RuntimeException{

    /**
     * 错误码
     */
    protected int code;
    /**
     * 错误信息
     */
    protected String msg;

    public BizException() {
        super();
    }

    public BizException(BizExceptionCode err) {
        super(err.getMsg());
        this.code = err.getCode();
        this.msg = err.getMsg();
    }

    public BizException(BizExceptionCode err, Throwable cause) {
        super(err.getMsg(), cause);
        this.code = err.getCode();
        this.msg = err.getMsg();
    }

    public BizException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public BizException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BizException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BizException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
