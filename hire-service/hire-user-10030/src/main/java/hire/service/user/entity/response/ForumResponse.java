package hire.service.user.entity.response;

import com.hire.common.web.utils.convert.ConvertFunction;
import com.hire.common.web.utils.convert.MultiConverter;
import hire.service.user.entity.Forum;
import hire.service.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForumResponse implements MultiConverter<Forum, User, ForumResponse> {

    private Forum forum;

    private User user;

    public ForumResponse(Forum forum, User user) {
        this.forum = forum;
        this.user = user;
    }

    private Long id;

    private String title;

    private String content;

    private int kind;

    private List<String> img;

    private String createTime;

    private Long userId;

    private String username;

    private String nick;

    private String phone;

    private Boolean sex;


    @Override
    public ForumResponse convert(ConvertFunction<Forum, User, ForumResponse> f) {
        return f.apply(this.forum, this.user);
    }
}
