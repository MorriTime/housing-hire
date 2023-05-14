package hire.service.sup.convertet;

import hire.service.sup.controller.SuperRequest.CreateAdminRequest;
import hire.service.sup.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserConverter {

    public static User convertUser(CreateAdminRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password("{bcrypt}"+new BCryptPasswordEncoder().encode(request.getPassword()))
                .nick("admin" + request.getUsername())
                .phone(request.getPhone())
                .build();

        return user;
    }
}
