package hire.service.user.controller;

import com.hire.common.base.response.ResponseData;
import com.hire.common.mybatis.entity.PageDto;
import hire.service.user.controller.request.*;
import hire.service.user.entity.Message;
import hire.service.user.entity.Report;
import hire.service.user.entity.response.ForumListResponse;
import hire.service.user.entity.response.HouseListResponse;
import hire.service.user.service.ForumService;
import hire.service.user.service.HouseService;
import hire.service.user.service.MessageService;
import hire.service.user.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class IndexController {

    private final ForumService forumService;

    private final HouseService houseService;

    private final ReportService reportService;

    private final MessageService messageService;

    /**
     * 发布房屋出租信息
     * @param houseCreateRequest 房屋创建实体
     * @return ResponseData<String>
     */
    @PostMapping("/resource")
    public ResponseData<String> CreateHouse(@Validated @RequestBody HouseCreateRequest houseCreateRequest) {
        return houseService.create(houseCreateRequest);
    }

    /**
     * 查看发布的房屋信息列表
     * @param id 用户id
     * @param current 分页
     * @param size 分页
     * @return ResponseData<PageDto<HouseListResponse>>
     */
    @GetMapping("/resource")
    public ResponseData<PageDto<HouseListResponse>> GetHouses(@RequestHeader(value = "userId") Long id,
                                                              @RequestParam(value = "current", required = false) Integer current,
                                                              @RequestParam(value = "size", required = false) Integer size) {
        return houseService.list(id, current, size);
    }

    /**
     * 删除房屋出租信息
     * @param id 房屋id
     * @return ResponseData<String>
     */
    @DeleteMapping("/resource")
    public ResponseData<String> DeleteHouse(@RequestParam("id") Long id) {
        return houseService.delete(id);
    }

    /**
     * 修改房屋出租信息
     * @param houseUpdateRequest 房屋实体
     * @return ResponseData<String>
     */
    @PutMapping("/resource")
    public ResponseData<String> UpdateHouse(@Validated @RequestBody HouseUpdateRequest houseUpdateRequest) {
        return houseService.update(houseUpdateRequest);
    }

    /**
     * 发表论坛信息
     * @param forumCreateRequest 论坛实体
     * @return ResponseData<String>
     */
    @PostMapping("/forum")
    public ResponseData<String> CreateForum(@Validated @RequestBody ForumCreateRequest forumCreateRequest) {
        return forumService.create(forumCreateRequest);
    }

    /**
     * 查看发布的论坛列表
     * @param id 用户id
     * @param current 分页
     * @param size 分页
     * @return ResponseData<PageDto<ForumListResponse>>
     */
    @GetMapping("/forum")
    public ResponseData<PageDto<ForumListResponse>> GetForums(@RequestHeader(value = "userId") Long id,
                                                              @RequestParam(value = "current", required = false) Integer current,
                                                              @RequestParam(value = "size", required = false) Integer size) {
        return forumService.list(id, current, size);
    }

    /**
     * 删除论坛信息
     * @param id 论坛id
     * @return ResponseData<String>
     */
    @DeleteMapping("/forum")
    public ResponseData<String> DeleteForum(@RequestParam("id") Long id) {
        return forumService.delete(id);
    }

    /**
     * 修改论坛信息
     * @param forumUpdateRequest 论坛实体
     * @return ResponseData<String>
     */
    @PutMapping("/forum")
    public ResponseData<String> UpdateForum(@Validated @RequestBody ForumUpdateRequest forumUpdateRequest) {
        return forumService.update(forumUpdateRequest);
    }

    /**
     * 举报论坛/房源
     * @param reportCreateRequest 举报实体
     * @return ResponseData<String>
     */
    @PostMapping("/report")
    public ResponseData<String> CreateReport(@Validated @RequestBody ReportCreateRequest reportCreateRequest) {
        return reportService.create(reportCreateRequest);
    }

    /**
     * 获取举报列表
     * @param id 用户id
     * @param current current
     * @param size size
     * @return ResponseData<PageDto<Report>>
     */
    @GetMapping("/report")
    public ResponseData<PageDto<Report>> GetReports(@RequestHeader(value = "userId") Long id,
                                                    @RequestParam(value = "current", required = false) Integer current,
                                                    @RequestParam(value = "size",required = false) Integer size) {
        return reportService.list(id, current, size);
    }

    /**
     * 删除举报
     * @param id 举报id
     * @return ResponseData<String>
     */
    @DeleteMapping("/report")
    public ResponseData<String> DeleteReport(@RequestParam("id") Long id) {
        return reportService.delete(id);
    }

    /**
     * 获取消息列表
     * @param id 用户id
     * @return ResponseData<PageDto<Message>>
     */
    @GetMapping("/message")
    public ResponseData<PageDto<Message>> GetMessage(@RequestHeader(value = "userId") Long id,
                                                     @RequestParam(value = "current", required = false) Integer current,
                                                     @RequestParam(value = "size",required = false) Integer size) {
        return messageService.list(id, current, size);
    }

    /**
     * 删除消息
     * @param id 消息id
     * @return ResponseData<String>
     */
    @DeleteMapping("/message")
    public ResponseData<String> DeleteMessage(@RequestParam("id") Long id) {
        return messageService.delete(id);
    }

    /**
     * 消息已读
     * @param id 消息id
     * @return ResponseData<String>
     */
    @PutMapping("/message")
    public ResponseData<String> UpdateMessage(@RequestParam("id") Long id) {
        return messageService.update(id);
    }
}
