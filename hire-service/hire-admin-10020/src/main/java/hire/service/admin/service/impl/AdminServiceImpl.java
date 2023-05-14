package hire.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hire.common.base.exception.BizException;
import com.hire.common.base.response.ResponseData;
import com.hire.common.mybatis.entity.PageDto;
import hire.service.admin.dao.ReportDao;
import hire.service.admin.entity.Report;
import hire.service.admin.service.AdminService;
import hire.service.admin.service.MessageGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ReportDao reportDao;

    private final MessageGeneratorService messageGeneratorService;

    @Override
    public ResponseData<PageDto<Report>> reports(int reportStatus, Integer current, Integer size) {
        Page<Report> page = new Page<>();
        page.setCurrent(current == null ? 0 : current)
                .setSize(current == null ? 5 : size);

        QueryWrapper<Report> wrapper = new QueryWrapper<>();
        wrapper.eq("report_status", reportStatus);
        wrapper.orderByDesc("create_time");

        page.setTotal(reportDao.selectCount(wrapper));
        Page<Report> reportPage = reportDao.selectPage(page, wrapper);

        PageDto<Report> reportPageDto = new PageDto<>(reportPage);

        return ResponseData.Success(reportPageDto);

    }

    @Override
    @Transactional
    public ResponseData<String> deal(Report report) {
        Report newReport = reportDao.selectById(report.getId());
        // 更新
        newReport.setReportStatus(1);
        newReport.setHandleStatus(report.getHandleStatus());
        UpdateWrapper<Report> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", newReport.getId());

        if (!messageGeneratorService.generateMessage2Report(newReport)) {
            throw new BizException("SQL异常");
        }

        // 先修改report表，然后删除相应内容
        try {
            if (reportDao.update(newReport, wrapper) < 1) {
                throw new BizException("SQL异常");
            } else {
                if (newReport.getHandleStatus() == 1) {
                    switch (newReport.getKind()) {
                        case "source": {
                            reportDao.delSource(newReport.getKindId());
                            break;
                        }
                        case "forum": {
                            reportDao.delForum(newReport.getKindId());
                            break;
                        }
                    }
                    // 添加通知
                    if (!messageGeneratorService.generateMessage2Author(newReport)) {
                        throw new BizException("SQL异常");
                    }
                }
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage(), e);
            throw new BizException("事务异常");
        }

        return ResponseData.Success();
    }
}
