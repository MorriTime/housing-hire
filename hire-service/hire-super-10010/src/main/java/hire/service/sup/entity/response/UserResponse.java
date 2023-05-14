package hire.service.sup.entity.response;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;

    private String username;

    private String nick;

    private String phone;

    private String sex;

    private String create_time;

    private String update_time;

    private String role;

}
