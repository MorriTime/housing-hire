package hire.service.base.service;

import com.hire.common.base.response.ResponseData;
import com.hire.common.mybatis.entity.PageDto;
import hire.service.base.controller.request.HouseSelectRequest;
import hire.service.base.entity.response.HouseListResponse;
import hire.service.base.entity.response.HouseResponse;

public interface HouseService {
    ResponseData<PageDto<HouseListResponse>> houses(HouseSelectRequest houseSelectRequest);

    ResponseData<HouseResponse> house(Long id);
}
