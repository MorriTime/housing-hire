package hire.service.base.controller;

import com.hire.common.base.entity.CommonUser;
import com.hire.common.base.response.ResponseData;
import hire.service.base.controller.request.LoginRequest;
import hire.service.base.controller.request.RegisterRequest;
import hire.service.base.dao.MessageDao;
import hire.service.base.entity.Oauth2TokenDto;
import hire.service.base.entity.User;
import hire.service.base.service.AuthService;
import hire.service.base.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/base")
@RequiredArgsConstructor
public class LoginAndRegisterController {

    private final AuthService authService;

    private final UserService userService;

    private final MessageDao messageDao;

    /**
     * 登录
     * @param user 登录实体
     * @return
     */
    @PostMapping("/login")
    public ResponseData<Object> Login(@Validated @RequestBody LoginRequest user) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "password");
        parameters.put("username", user.getUsername());
        parameters.put("password", user.getPassword());

        Oauth2TokenDto oauth2TokenDto = authService.auth(parameters);
        CommonUser info = userService.getInfo(oauth2TokenDto.getUserId());
        if (info != null) {
            oauth2TokenDto.setAvatar(info.getAvatar());
            oauth2TokenDto.setNick(info.getNick());
            oauth2TokenDto.setSex(info.getSex());
        }
        oauth2TokenDto.setMessage(messageDao.count(oauth2TokenDto.getUserId()));

        return ResponseData.Success(oauth2TokenDto);
    }

    @GetMapping("/send")
    public ResponseData<String> SendEmail(@Email @RequestParam("email") String email) {
        return userService.emailForCode(email);
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public ResponseData<String> Register(@Validated @RequestBody RegisterRequest user) {
        return userService.userRegister(user);
    }

    /**
     * 登出
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/logout")
    public ResponseData<String> Logout(HttpServletRequest httpServletRequest) {
            String accessToken = httpServletRequest.getHeader("Authorization").replace("Bearer ", "");
        return ResponseData.Success(authService.logout(accessToken));
    }

    /**
     * 查看用户信息
     * @param userId
     * @return
     */
    @GetMapping("/userInfo")
    private ResponseData<User> getInfo(@RequestParam("userId") Long userId) {
        return userService.getUserInfo(userId);
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @PutMapping("/userInfo")
    private ResponseData<String> updateInfo(@RequestBody User user) {
        return userService.updateUserInfo(user);
    }

    /**
     * 修改密码
     * @param map
     * @return
     */
    @PutMapping("/changePwd")
    private ResponseData<String> updatePassword(@RequestBody Map<String, String> map) {
        return userService.updatePassword(map);
    }
}
