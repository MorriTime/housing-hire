package hire.service.base.entity.response;

import com.hire.common.web.utils.TypeUtil;
import com.hire.common.web.utils.convert.ConvertFunction;
import hire.service.base.entity.Forum;
import hire.service.base.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForumResponse implements ConvertFunction<Forum, User, ForumResponse> {

    private Long id;

    private String title;

    private String content;

    private int kind;

    private List<String> img;

    private String createTime;

    private Long usrId;

    private String username;

    private String nick;

    private String phone;

    private Boolean sex;

    @Override
    public ForumResponse apply(Forum forum, User user) {
        return ForumResponse.builder()
                .id(forum.getId())
                .title(forum.getTitle())
                .content(forum.getContent())
                .img(TypeUtil.str2List(forum.getImg()))
                .createTime(forum.getCreateTime())
                .usrId(user.getId())
                .username(user.getUsername())
                .nick(user.getNick())
                .phone(user.getPhone())
                .sex(user.getSex())
                .build();
    }
}
