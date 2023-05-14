package com.hire.common.web.config;

import com.alibaba.fastjson.JSON;
import com.hire.common.base.exception.BizException;
import com.hire.common.base.response.BaseResponseEnum;
import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Configuration
public class FeignErrorDecoder extends ErrorDecoder.Default {

    @Override
    public Exception decode(String methodKey, Response response) {
        Exception exception = super.decode(methodKey, response);

        if (exception instanceof RetryableException) {
            return exception;
        }

        try {
            if (exception instanceof FeignException && ((FeignException) exception).responseBody().isPresent()) {
                ByteBuffer responseBody = ((FeignException) exception).responseBody().get();
                String bodyText = StandardCharsets.UTF_8.newDecoder().decode(responseBody.asReadOnlyBuffer()).toString();
                // 将异常信息，转换为ExceptionInfo对象
                BizException bizException = JSON.parseObject(bodyText, BizException.class);
                // 如果excepiton中code不为空，则使用该code，否则使用默认的错误code
                Integer code = Optional.ofNullable(bizException.getCode()).orElse(BaseResponseEnum.ERROR.getCode());
                // 如果excepiton中message不为空，则使用该message，否则使用默认的错误message
                String message = Optional.ofNullable(bizException.getMsg()).orElse(BaseResponseEnum.ERROR.getMsg());
                return new BizException(code, message);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return exception;
    }

}
