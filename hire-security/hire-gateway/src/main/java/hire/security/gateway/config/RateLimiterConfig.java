package hire.security.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class RateLimiterConfig {

    @Primary
    @Bean(value = "remoteAddrKeyResolver")
    public KeyResolver remoteAddrKeyResolver() {
        return exchange -> {
            String hostAddress = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            log.info("remoteAddrKeyResolver 限流ip {}", hostAddress);
            return Mono.just(hostAddress);
        };
    }

    @Bean(value = "pathKeyResolver")
    public KeyResolver pathKeyResolver() {
        return exchange -> {
            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
            log.info("pathKeyResolver 限流ip {}", route.getId());
            return Mono.just(exchange.getRequest().getPath().toString());
        };
    }
}
