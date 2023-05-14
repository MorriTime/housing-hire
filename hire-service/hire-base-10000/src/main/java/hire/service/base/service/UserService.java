package hire.service.base.service;

import com.hire.common.base.entity.CommonUser;
import com.hire.common.base.response.ResponseData;
import hire.service.base.controller.request.RegisterRequest;
import hire.service.base.entity.User;

import java.util.Map;

public interface UserService {
    CommonUser getInfo(String userId);

    ResponseData<String> userRegister(RegisterRequest user);

    ResponseData<String> emailForCode(String email);

    ResponseData<User> getUserInfo(Long userId);

    ResponseData<String> updateUserInfo(User user);

    ResponseData<String> updatePassword(Map<String, String> map);

    ResponseData<User> overviewUser(Long id);
}
