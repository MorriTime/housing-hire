package hire.service.admin.controller;

import com.hire.common.base.response.ResponseData;
import com.hire.common.mybatis.entity.PageDto;
import hire.service.admin.entity.Report;
import hire.service.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * 获取举报列表
     * @param reportStatus 处理情况
     * @param current current
     * @param size size
     * @return ResponseData<PageDto<Report>>
     */
    @GetMapping("/reports")
    public ResponseData<PageDto<Report>> Reports(@RequestParam("reportStatus") int reportStatus,
                                                 @RequestParam(value = "current", required = false) Integer current,
                                                 @RequestParam(value = "size", required = false)Integer size) {
        return adminService.reports(reportStatus, current, size);
    }

    /**
     * 处理举报
     * @param report 举报体（id, handleStatus）
     * @return ResponseData<String>
     */
    @PutMapping("/deal")
    public ResponseData<String> DealReport(@RequestBody Report report) {
        return adminService.deal(report);
    }
}
