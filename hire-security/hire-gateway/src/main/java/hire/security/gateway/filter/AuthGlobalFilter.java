package hire.security.gateway.filter;

import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;

/**
 * 将登录用户的JWT转化成用户信息的全局过滤器
 * Ordered：Ordered接口用来指定拦截器生效顺序（数字越小优先级越高）
 */
@Slf4j
@Order(-1)
public class AuthGlobalFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (token.isEmpty()) {
            return chain.filter(exchange);
        }

        String realToken = token.replace("bearer ", "");
        log.info("AuthGlobalFilter.filter() token:{}",realToken);
        try {
            JWSObject jwsObject = JWSObject.parse(realToken);
            String userId = jwsObject.getPayload().toString();
            log.info("AuthGlobalFilter.filter() userId:{}", userId);
            ServerHttpRequest request = exchange.getRequest().mutate().header("userId", userId).build();
            exchange = exchange.mutate().request(request).build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return chain.filter(exchange);
    }
}
