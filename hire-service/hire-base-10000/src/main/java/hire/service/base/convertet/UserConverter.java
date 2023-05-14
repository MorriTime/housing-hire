package hire.service.base.convertet;

import hire.service.base.controller.request.RegisterRequest;
import hire.service.base.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserConverter {

    public static User convertUser(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password("{bcrypt}"+new BCryptPasswordEncoder().encode(request.getPassword()))
                .nick(request.getNick())
                .phone(request.getPhone())
                .sex(request.getSex())
                .build();

        return user;
    }
}
