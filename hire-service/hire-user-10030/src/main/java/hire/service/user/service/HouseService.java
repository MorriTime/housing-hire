package hire.service.user.service;

import com.hire.common.base.response.ResponseData;
import com.hire.common.mybatis.entity.PageDto;
import hire.service.user.controller.request.HouseCreateRequest;
import hire.service.user.controller.request.HouseUpdateRequest;
import hire.service.user.entity.response.HouseListResponse;

public interface HouseService {
    ResponseData<String> create(HouseCreateRequest houseCreateRequest);

    ResponseData<PageDto<HouseListResponse>> list(Long id, Integer current, Integer size);

    ResponseData<String> delete(Long id);

    ResponseData<String> update(HouseUpdateRequest houseUpdateRequest);
}
