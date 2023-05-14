package hire.security.gateway.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 自定义认证
 */
@Component
@Slf4j
public class ReactiveRedisAuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private RedisTokenStore redisTokenStore;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .filter(a -> a instanceof BearerTokenAuthenticationToken)
                .cast(BearerTokenAuthenticationToken.class)
                .map(BearerTokenAuthenticationToken::getToken)
                .flatMap((accessToken ->{
                    log.info("accessToken is :{}",accessToken);
                    OAuth2AccessToken oAuth2AccessToken = this.redisTokenStore.readAccessToken(accessToken);
                    //根据access_token从数据库获取不到OAuth2AccessToken
                    if(oAuth2AccessToken == null){
                        return Mono.error(new InvalidTokenException("您未登录，请先登录"));
                    } else if(oAuth2AccessToken.isExpired()) {
                        return Mono.error(new InvalidTokenException("权限已过期，请重新请求"));
                    }

                    OAuth2Authentication oAuth2Authentication = redisTokenStore.readAuthentication(accessToken);
                    if(oAuth2Authentication == null){
                        return Mono.error(new InvalidTokenException("Authorization无效"));
                    }else {
                        return Mono.just(oAuth2Authentication);
                    }
                })).cast(Authentication.class);
    }

    @Bean
    public RedisTokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }
}
