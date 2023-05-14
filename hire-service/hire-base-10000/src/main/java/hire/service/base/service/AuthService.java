package hire.service.base.service;

import com.hire.common.web.config.FeignErrorDecoder;
import com.hire.common.web.constant.ServiceNameConstant;
import hire.service.base.entity.Oauth2TokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Component
@FeignClient(value = ServiceNameConstant.HIRE_AUTH_SERVICE, configuration = {FeignErrorDecoder.class})
public interface AuthService {

    @PostMapping("/oauth/token")
    Oauth2TokenDto auth(@RequestBody Map<String, String> parameters);

    @GetMapping("/oauth/logout")
    String logout(@RequestParam("accessToken") String accessToken);

}
