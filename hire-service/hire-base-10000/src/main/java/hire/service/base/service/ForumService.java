package hire.service.base.service;

import com.hire.common.base.response.ResponseData;
import com.hire.common.mybatis.entity.PageDto;
import hire.service.base.entity.response.ForumListResponse;
import hire.service.base.entity.response.ForumResponse;

import java.util.List;

public interface ForumService {
    ResponseData<PageDto<ForumListResponse>> forms(Integer current, Integer size);

    ResponseData<ForumResponse> forum(Long id);
}
