package hire.service.base.controller;

import com.hire.common.base.response.ResponseData;
import com.hire.common.mybatis.entity.PageDto;
import hire.service.base.controller.request.HouseSelectRequest;
import hire.service.base.entity.User;
import hire.service.base.entity.response.ForumListResponse;
import hire.service.base.entity.response.ForumResponse;
import hire.service.base.entity.response.HouseListResponse;
import hire.service.base.entity.response.HouseResponse;
import hire.service.base.service.ForumService;
import hire.service.base.service.HouseService;
import hire.service.base.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/base")
@RequiredArgsConstructor
public class IndexController {

    private final ForumService forumService;

    private final HouseService houseService;

    private final UserService userService;

    /**
     * 查看用户信息
     * @param id 用户id
     * @return ResponseData<User>
     */
    @GetMapping("/user")
    public ResponseData<User> UserDetail(@RequestParam("id") Long id) {
        return userService.overviewUser(id);
    }

    /**
     * 查找房屋
     * @param houseSelectRequest 房源筛选条件
     * @return ResponseData<PageDto<HouseListResponse>>
     */
    @PostMapping("/sources")
    public ResponseData<PageDto<HouseListResponse>> SelectHouses(@Validated @RequestBody HouseSelectRequest houseSelectRequest) {
        return houseService.houses(houseSelectRequest);
    }

    /**
     * 浏览房屋信息
     * @param id 房屋id
     * @return ResponseData<HouseResponse>
     */
    @GetMapping("/source")
    public ResponseData<HouseResponse> SelectHouse(@RequestParam("id") Long id) {
        return houseService.house(id);
    }

    /**
     * 获取论坛列表
     * @param current current
     * @param size size
     * @return ResponseData<PageDto<ForumListResponse>>
     */
    @GetMapping("/forums")
    public ResponseData<PageDto<ForumListResponse>> SelectForums(@RequestParam("current") Integer current,
                                                                @RequestParam("size") Integer size) {
        return forumService.forms(current, size);
    }

    /**
     * 获取论坛详情
     * @param id 论坛id
     * @return ResponseData<ForumResponse>
     */
    @GetMapping("/forum")
    public ResponseData<ForumResponse> SelectForum(@RequestParam("id") Long id) {
        return forumService.forum(id);
    }

}
