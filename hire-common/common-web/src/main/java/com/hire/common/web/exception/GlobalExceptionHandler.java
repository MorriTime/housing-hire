package com.hire.common.web.exception;

import com.hire.common.base.exception.BizException;
import com.hire.common.base.response.BaseResponseEnum;
import com.hire.common.base.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(IllegalArgumentException.class)
    public <T> ResponseData<T> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("非法参数异常，异常原因：{}", e.getMessage(), e);
        return ResponseData.Error(e.getMessage());
    }

    @ExceptionHandler(value = ServletRequestBindingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData<?> handleServletRequestBindingException(Exception e) {
        log.error("绑定异常，异常原因：{}", e.getMessage(), e);
        return ResponseData.Error("请求参数为空");
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseData<?> handleBusinessException(MethodArgumentNotValidException e) {
        return ResponseData.Error(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<?> methodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.error("请求异常，异常原因：{}", e.getMessage(), e);
        return ResponseData.Error("请求方法错误");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseData<?> exceptionHandler(Exception e) {
        if (e instanceof BizException ) {
            log.error("biz exec error, e = ", e);
            return ResponseData.Create((BizException) e);
        }
        log.error("biz exec error, e = ", e);
        return ResponseData.Create(BaseResponseEnum.UNEXPECTED);
    }

}
