package hire.service.base.controller.request;

import com.hire.common.web.utils.convert.Converter;
import hire.service.base.entity.User;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.function.Function;

@Data
public class RegisterRequest implements Converter<RegisterRequest, User> {

    @Email(message = "填写正确的邮箱格式")
    @NotBlank(message = "账号不能为空")
    @NotNull(message = "账号不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    @NotNull(message = "密码不能为空")
    private String password;

    @NotBlank(message = "称呼不能为空")
    private String nick;

    @NotBlank(message = "手机号码不能为空")
    @NotNull(message = "手机号不能为空")
    @Length(min = 11, max = 11, message = "手机号只能为11位")
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    private String phone;

    @NotNull(message = "性别不能为空")
    private Boolean sex;

    @NotBlank(message = "验证码不能为空")
    @NotNull(message = "验证码不能为空")
    private String code;

    @Override
    public User convert(Function<RegisterRequest, User> f) {
        return f.apply(this);
    }
}
