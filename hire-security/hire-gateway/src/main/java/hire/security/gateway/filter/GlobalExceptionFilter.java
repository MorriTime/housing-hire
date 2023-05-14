package hire.security.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hire.common.base.response.ResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关异常通用处理器
 */
@Slf4j
@Order(-1)
//@Component
@RequiredArgsConstructor
public class GlobalExceptionFilter implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
        ServerHttpResponse response = serverWebExchange.getResponse();

        if (response.isCommitted()) {
            return Mono.error(throwable);
        }

        // header set
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (throwable instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) throwable).getStatus());
        }

        return response
                .writeWith(Mono.fromSupplier(() -> {
                    DataBufferFactory bufferFactory = response.bufferFactory();
                    try {
                        log.warn("Error Spring Cloud Gateway : {} {}", serverWebExchange.getRequest().getPath(), throwable.getMessage());
                        return bufferFactory.wrap(objectMapper.writeValueAsBytes(ResponseData.Error(throwable.getMessage())));
                    } catch (JsonProcessingException e) {
                        log.warn("Error writing response", throwable);
                        return bufferFactory.wrap(new byte[0]);
                    }
                }));
    }
}
