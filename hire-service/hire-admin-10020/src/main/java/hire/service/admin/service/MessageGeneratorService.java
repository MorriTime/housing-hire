package hire.service.admin.service;

import com.hire.common.web.enums.ReportEnum;
import hire.service.admin.dao.MessageDao;
import hire.service.admin.entity.Message;
import hire.service.admin.entity.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageGeneratorService {

    private final MessageDao messageDao;

    public Boolean generateMessage2Author(Report report) {
        String reason;
        switch (report.getReportType()) {
            case 1 : {
                reason = ReportEnum.WARMING_PRICE.getName();
                break;
            }
            case 2 : {
                reason = ReportEnum.WARMING_CONTENT.getName();
                break;
            }
            case 3 : {
                reason = ReportEnum.WARMING_COMMENT.getName();
                break;
            }
            default: {
                reason = ReportEnum.WARMING_OTHER.getName();
            }
        }
        String content = "您好，您的内容：" + report.getReportTitle() + ",由于" + reason + "原因被举报。经过管理员初步审核，存在违规行为，内容已被清楚。\n" +
                "详细举报信息：" + report.getContent() + "。";

        Message message = Message.builder()
                .content(content)
                .userId(report.getUserId())
                .status(0)
                .build();

        return messageDao.insert(message) >= 1;
    }

    public Boolean generateMessage2Report(Report report) {
        String content = "感谢您的举报，我们已经对举报的内容进行处理。";
        Message message = Message.builder()
                .content(content)
                .userId(report.getReportedId())
                .status(0)
                .build();

        return messageDao.insert(message) >= 1;
    }
}
