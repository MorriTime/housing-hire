package hire.service.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hire.common.base.entity.CommonUser;
import com.hire.common.base.exception.BizException;
import com.hire.common.base.response.ResponseData;
import com.hire.common.redis.utils.RedisUtil;
import com.hire.common.web.enums.RoleEnum;
import hire.service.base.controller.request.RegisterRequest;
import hire.service.base.convertet.UserConverter;
import hire.service.base.dao.UserDao;
import hire.service.base.dao.UserRoleDao;
import hire.service.base.entity.User;
import hire.service.base.entity.UserRole;
import hire.service.base.service.UserService;
import hire.service.base.utils.EmailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final UserRoleDao userRoleDao;

    private final RedisUtil redisUtil;

    private final EmailUtil emailUtil;

    @Override
    public CommonUser getInfo(String userId) {
        return userDao.selectUser(userId);
    }

    @Override
    public ResponseData<String> emailForCode(String email) {
        String code = emailUtil.sendMail(email);
        if (code == null) {
            throw new BizException("emailUtil返回出错");
        }
        // 缓存时间5min
        if (!redisUtil.set(email, code, 300)) {
            throw new BizException("redis异常");
        }

        return ResponseData.Success();
    }

    @Override
    @Transactional
    public ResponseData<String> userRegister(RegisterRequest user) {
        // 邮箱验证码校验
        String code = (String) redisUtil.get(user.getUsername());

        if (code == null || !code.equals(user.getCode())) {
            return ResponseData.Error("验证码错误");
        }

        // 类型转换
        User register = user.convert(UserConverter::convertUser);
        try {
            if (userDao.insert(register) < 1) {
                throw new BizException("SQL异常");
            } else {
                UserRole userRole = new UserRole();
                userRole.setUserId(register.getId());
                userRole.setRoleId(RoleEnum.USER.getId());
                userRoleDao.insert(userRole);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage(), e);
            throw new BizException("事务异常");
        }
        return ResponseData.Success();
    }

    @Override
    public ResponseData<User> getUserInfo(Long userId) {
        User user = userDao.selectById(userId);
        if (user == null) throw new BizException("userId异常");
        return ResponseData.Success(user);
    }

    @Override
    public ResponseData<String> updateUserInfo(User user) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", user.getId());
        if (userDao.update(user, updateWrapper) < 1) {
            throw new BizException("SQL异常");
        }

        return ResponseData.Success();
    }

    @Override
    public ResponseData<String> updatePassword(Map<String, String> map) {
        String userId = map.get("id");
        User user = userDao.selectById(userId);

        String newPwd = map.get("password");
        user.setPassword("{bcrypt}"+new BCryptPasswordEncoder().encode(newPwd));

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", userId);
        if (userDao.update(user, updateWrapper) < 1) {
            throw new BizException("SQL异常");
        }

        return ResponseData.Success();
    }

    @Override
    public ResponseData<User> overviewUser(Long id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.select("id", "username", "nick", "avatar", "phone", "sex");

        User user = userDao.selectOne(wrapper);

        if (user == null) {
            return ResponseData.Error("该用户不存在");
        }

        return ResponseData.Success(user);
    }
}
