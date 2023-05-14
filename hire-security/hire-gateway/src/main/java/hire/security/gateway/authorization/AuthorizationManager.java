package hire.security.gateway.authorization;

import cn.hutool.core.text.AntPathMatcher;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hire.common.base.entity.Permission;
import com.hire.common.base.exception.BizException;
import com.nimbusds.jose.JWSObject;
import hire.security.gateway.dao.PermissionDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.Collection;

/**
 * 鉴权管理器，用于判断是否有资源的访问权限
 */
@Component
@Slf4j
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        // 从Redis中获取当前路径可访问角色列表
        // 当前路径
        String url = authorizationContext.getExchange().getRequest().getURI().getPath();

        Permission sqlAuthorities = permissionDao.selectOne(new QueryWrapper<Permission>().eq("url", url));

        String token = authorizationContext.getExchange().getRequest().getHeaders().getFirst("Authorization");
        log.debug("当前请求头Authorization中的值:{}",token);
        if (token.isEmpty()) {
            log.warn("当前请求头Authorization中的值不存在");
            return Mono.just(new AuthorizationDecision(false));
        }

        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        String accessToken = token.replace("Bearer ", "");
        OAuth2Authentication oAuth2Authentication = redisTokenStore.readAuthentication(accessToken);
        Collection<GrantedAuthority> authorities = oAuth2Authentication.getAuthorities();
        log.debug("当前token所拥有的权限:{}",authorities.toString());

        //认证通过且角色匹配 的用户访问当前路径
        for (GrantedAuthority authority : authorities) {
            String authorityStr= authority.getAuthority();
            if (antPathMatcher.match(authorityStr, sqlAuthorities.getAuthority())) {

                try {
                    JWSObject jwsObject = JWSObject.parse(accessToken);
                    String userId = JSONObject.parseObject(jwsObject.getPayload().toString()).get("id").toString() ;
                    //设置请求头参数userId
                    ServerHttpRequest request = authorizationContext.getExchange().getRequest().mutate()
                            .headers(httpHeaders -> httpHeaders.add("userId" , userId)).build();
                    authorizationContext.getExchange().mutate().request(request).build();
                    return Mono.just(new AuthorizationDecision(true));
                } catch (ParseException e) {
                    log.error(e.getMessage());
                    throw new BizException("解析错误");
                }
            }
        }
        return Mono.just(new AuthorizationDecision(false));
    }
}
