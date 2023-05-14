package hire.service.user.service;

import com.hire.common.base.response.ResponseData;
import com.hire.common.mybatis.entity.PageDto;
import hire.service.user.entity.Message;

public interface MessageService {
    ResponseData<PageDto<Message>> list(Long id, Integer current, Integer size);

    ResponseData<String> delete(Long id);

    ResponseData<String> update(Long id);
}
