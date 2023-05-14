package hire.service.user.service;

import com.hire.common.base.response.ResponseData;
import com.hire.common.mybatis.entity.PageDto;
import hire.service.user.controller.request.ReportCreateRequest;
import hire.service.user.entity.Report;

public interface ReportService {
    ResponseData<String> create(ReportCreateRequest reportCreateRequest);

    ResponseData<PageDto<Report>> list(Long id, Integer current, Integer size);

    ResponseData<String> delete(Long id);
}
