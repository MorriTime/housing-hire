package hire.security.oauth2.service.Impl;


import com.hire.common.base.entity.AuthUser;
import com.hire.common.base.entity.CommonUser;
import hire.security.oauth2.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserDao userDao;

    /**
     * 实现UserDetailsService中的loadUserByUsername方法，用于加载用户数据
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CommonUser commonUser = userDao.selectOne(username);

        if (commonUser == null) {
            throw new UsernameNotFoundException("用户名未注册");
        }

        //用户权限列表
        Collection<? extends GrantedAuthority> authorities = userDao.queryRole(commonUser.getId())
                .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new AuthUser(
                commonUser.getId(),
                commonUser.getUsername(),
                commonUser.getPassword(),
                authorities);
    }
}


