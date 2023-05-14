package hire.security.oauth2.controller;

import hire.security.oauth2.entity.Oauth2TokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

/**
 * 自定义Oauth2接口
 */
@RestController
@RequestMapping("/oauth")
public class AuthController {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @PostMapping("/token")
    public Oauth2TokenDto postAccessToken(Principal principal, @RequestBody Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();

        return Oauth2TokenDto.builder()
                .token(oAuth2AccessToken.getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .tokenHead("Bearer ")
                .userId(oAuth2AccessToken.getAdditionalInformation().get("id").toString())
                .role(oAuth2AccessToken.getAdditionalInformation().get("role").toString())
                .build();
    }

    @GetMapping("/logout")
    public String logout(@RequestParam("accessToken") String accessToken) {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessToken);
        if (oAuth2AccessToken != null) {
            OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();
            //从tokenStore中移除token
            tokenStore.removeAccessToken(oAuth2AccessToken);
            tokenStore.removeRefreshToken(oAuth2RefreshToken);
            tokenStore.removeAccessTokenUsingRefreshToken(oAuth2RefreshToken);
            return "登出成功";
        } else {
            return "token已失效，请勿重复登出";
        }
    }
}
