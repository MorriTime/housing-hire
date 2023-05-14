package hire.service.user.service;

import com.hire.common.base.response.ResponseData;
import com.hire.common.mybatis.entity.PageDto;
import hire.service.user.controller.request.ForumCreateRequest;
import hire.service.user.controller.request.ForumUpdateRequest;
import hire.service.user.entity.response.ForumListResponse;

public interface ForumService {
    ResponseData<String> create(ForumCreateRequest forumCreateRequest);

    ResponseData<PageDto<ForumListResponse>> list(Long id, Integer current, Integer size);

    ResponseData<String> delete(Long id);

    ResponseData<String> update(ForumUpdateRequest forumUpdateRequest);
}
