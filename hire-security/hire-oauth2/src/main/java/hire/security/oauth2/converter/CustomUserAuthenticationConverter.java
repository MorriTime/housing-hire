package hire.security.oauth2.converter;

import com.hire.common.base.entity.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String name = authentication.getName();
        response.put(USERNAME, name);

        Object principal = authentication.getPrincipal();
        AuthUser authUser = null;
        if (principal instanceof AuthUser) {
            authUser = (AuthUser) principal;
        } else {
            UserDetails userDetails = userDetailsService.loadUserByUsername(name);
            authUser = (AuthUser) userDetails;
        }

        response.put(USERNAME, authUser.getUsername());
        response.put("id", authUser.getId());

        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        return response;
    }
}
