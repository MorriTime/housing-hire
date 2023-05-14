package hire.security.oauth2.enums;

import com.hire.common.base.exception.BizExceptionCode;

public enum BizEnum implements BizExceptionCode {
    USERNAME_NULL(100, "账号或者密码不能为空"),
    PASSWORD_ERROR(101, "账号密码错误");


    private final int code;

    private final String msg;

    BizEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
