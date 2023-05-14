package hire.security.gateway.factory;

import com.alibaba.fastjson.JSON;
import com.hire.common.base.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.HttpStatusHolder;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.Map;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.setResponseStatus;

@Component
@Slf4j
public class MyRequestRateLimiterGateWayFilterFactory extends RequestRateLimiterGatewayFilterFactory {

    private final RateLimiter defaultRateLimiter;

    private final KeyResolver defaultKeyResolver;

    private boolean denyEmptyKey = true;

    @Override
    public String name() {
        return "MyRequestRateLimiter";
    }

    public MyRequestRateLimiterGateWayFilterFactory(RateLimiter defaultRateLimiter, KeyResolver defaultKeyResolver) {
        super(defaultRateLimiter, defaultKeyResolver);
        this.defaultRateLimiter = defaultRateLimiter;
        this.defaultKeyResolver = defaultKeyResolver;
    }

    @Override
    public GatewayFilter apply(Config config) {
        KeyResolver resolver = this.getOrDefault(config.getKeyResolver(), this.defaultKeyResolver);
        RateLimiter<Object> limiter = this.getOrDefault(config.getRateLimiter(), this.defaultRateLimiter);
        boolean denyEmpty = getOrDefault(config.getDenyEmptyKey(), this.denyEmptyKey);
        HttpStatusHolder emptyKeyStatus = HttpStatusHolder
                .parse((String) this.getOrDefault(config.getEmptyKeyStatus(), this.getEmptyKeyStatusCode()));

        return (exchange, chain) -> resolver.resolve(exchange).defaultIfEmpty("____EMPTY_KEY__")
                .flatMap(key -> {
                    if ("____EMPTY_KEY__".equals(key)) {
                        if (denyEmpty) {
                            setResponseStatus(exchange, emptyKeyStatus);
                            return exchange.getResponse().setComplete();
                        }
                        return chain.filter(exchange);
                    }
                    // 获取到 routeId
                    String routeId = config.getRouteId();
                    if (routeId == null) {
                        Route route = exchange
                                .getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
                        routeId = route.getId();
                    }
                    log.debug("MultiRequestRateLimiterGatewayFilterFactory::routeId={}", routeId);

                    return limiter.isAllowed(routeId, key).flatMap(response -> {

                        for (Map.Entry<String, String> header : response.getHeaders()
                                .entrySet()) {
                            exchange.getResponse().getHeaders().add(header.getKey(),
                                    header.getValue());
                        }

                        if (response.isAllowed()) {
                            return chain.filter(exchange);
                        } else {
                            // 自定义返回信息
                            ServerHttpResponse httpResponse = exchange.getResponse();
                            httpResponse.setStatusCode(HttpStatus.OK);
                            httpResponse.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                            String body= JSON.toJSONString(ResponseData.Error("操作过于频繁，稍后再试~"));
                            DataBuffer buffer =  httpResponse.bufferFactory().wrap(body.getBytes(Charset.forName("UTF-8")));
                            return httpResponse.writeWith(Mono.just(buffer));
                        }
                    });
                });
    }

    private <T> T getOrDefault(T configValue, T defaultValue) {
        return (configValue != null) ? configValue : defaultValue;
    }
}
