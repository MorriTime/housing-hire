package hire.service.base.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumListResponse {

    private Long id;

    private String title;

    private String content;

    private int kind;

    private List<String> img;

    private String createTime;
}
