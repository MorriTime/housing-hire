package hire.service.user.controller.request;

import com.hire.common.web.utils.convert.Converter;
import hire.service.user.entity.Forum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.Function;

@Data
public class ForumUpdateRequest implements Converter<ForumUpdateRequest, Forum> {

    @NotNull(message = "主键id不能为空")
    private Long id;
    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    @NotNull(message = "标题不能为空")
    private String title;
    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    @NotNull(message = "内容不能为空")
    private String content;
    /**
     * 求租/引流 1:求租 0:招租
     */
    @NotNull(message = "类型不能为空")
    private int kind;
    /**
     * 图片数组
     */
    @NotNull(message = "内容不能为空")
    private List<String> img;

    @Override
    public Forum convert(Function<ForumUpdateRequest, Forum> f) {
        return f.apply(this);
    }
}
