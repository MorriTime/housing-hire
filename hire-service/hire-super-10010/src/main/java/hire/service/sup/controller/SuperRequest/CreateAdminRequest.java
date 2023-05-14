package hire.service.sup.controller.SuperRequest;

import com.hire.common.web.utils.convert.Converter;
import hire.service.sup.entity.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.function.Function;

@Data
public class CreateAdminRequest implements Converter<CreateAdminRequest, User> {

    private Long id;

    @NotBlank(message = "账号不能为空")
    @NotNull(message = "账号不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    @NotNull(message = "密码不能为空")
    private String password;

    private String nick;

    @NotBlank(message = "电话不能为空")
    @NotNull(message = "电话不能为空")
    private String phone;

    @Override
    public User convert(Function<CreateAdminRequest, User> f) {
        return f.apply(this);
    }
}
