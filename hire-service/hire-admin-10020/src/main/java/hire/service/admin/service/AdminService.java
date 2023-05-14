package hire.service.admin.service;

import com.hire.common.base.response.ResponseData;
import com.hire.common.mybatis.entity.PageDto;
import hire.service.admin.entity.Report;

public interface AdminService {
    ResponseData<PageDto<Report>> reports(int reportStatus, Integer current, Integer size);

    ResponseData<String> deal(Report report);
}
