package hire.service.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hire.common.base.exception.BizException;
import com.hire.common.base.response.ResponseData;
import com.hire.common.mybatis.entity.PageDto;
import hire.service.user.controller.request.ReportCreateRequest;
import hire.service.user.dao.ReportDao;
import hire.service.user.entity.Report;
import hire.service.user.service.ReportService;
import hire.service.user.utils.IndexConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportDao reportDao;

    @Override
    public ResponseData<String> create(ReportCreateRequest reportCreateRequest) {
        Report report = reportCreateRequest.convert(IndexConverter::convertReport);
        if (reportDao.insert(report) < 1) throw new BizException("SQL异常");

        return ResponseData.Success();
    }

    @Override
    public ResponseData<PageDto<Report>> list(Long id, Integer current, Integer size) {
        Page<Report> page = new Page<>();
        page.setCurrent(current == null ? 0 : current)
                .setSize(size == null ? 5 : size);

        QueryWrapper<Report> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", id);
        wrapper.orderByDesc("create_time");

        page.setTotal(reportDao.selectCount(wrapper));

        Page<Report> record = reportDao.selectPage(page, wrapper);

        PageDto<Report> reportPageDto = new PageDto<>(record);

        return ResponseData.Success(reportPageDto);
    }

    @Override
    public ResponseData<String> delete(Long id) {
        LambdaQueryWrapper<Report> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Report::getId, id);
        Report report = reportDao.selectOne(wrapper);

        if (report == null) {
            ResponseData.Error("id异常");
        }
        reportDao.delete(wrapper);
        return ResponseData.Success();
    }
}
