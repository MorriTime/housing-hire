package hire.service.user.controller.request;

import com.hire.common.web.utils.convert.Converter;
import hire.service.user.entity.Report;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.function.Function;

@Data
public class ReportCreateRequest implements Converter<ReportCreateRequest, Report> {

    @NotNull(message = "举报类型不能为空")
    private int reportType;

    @NotBlank(message = "举报文章标题不能为空")
    @NotNull(message = "举报文章标题不能为空")
    private String reportTitle;

    @NotBlank(message = "举报内容不能为空")
    @NotNull(message = "举报内容不看为空")
    private String content;

    @NotBlank(message = "举报人不能为空")
    @NotNull(message = "举报人不能为空")
    private String reportUser;

    @NotNull(message = "举报者Id不能为空")
    private Long userId;

    @NotNull(message = "被举报作者Id不能为空")
    private Long reportedId;

    @NotBlank(message = "举报内容网址不能为空")
    @NotNull(message = "举报内容网址不能为空")
    private String url;

    @Override
    public Report convert(Function<ReportCreateRequest, Report> f) {
        return f.apply(this);
    }
}
