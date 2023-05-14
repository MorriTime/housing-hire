package hire.service.sup.controller;

import com.hire.common.base.response.ResponseData;
import com.hire.common.mybatis.entity.PageDto;
import hire.service.sup.controller.SuperRequest.CreateAdminRequest;
import hire.service.sup.entity.response.UserResponse;
import hire.service.sup.service.SuperService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/super")
@RequiredArgsConstructor
public class SuperController {

    private final SuperService superService;

    @GetMapping("/users")
    public ResponseData<PageDto<UserResponse>> adminList(@RequestParam("role") int role,
                                                         @RequestParam(value = "current", required = false) Integer current,
                                                         @RequestParam(value = "size", required = false) Integer size) {
        return superService.getUsers(role, current, size);
    }

    @PostMapping("/create")
    public ResponseData<String> createAdmin(@Validated @RequestBody CreateAdminRequest user) {
        return superService.insertAdmin(user);
    }

    @DeleteMapping("/del")
    public ResponseData<String> delUser(@RequestParam("id") Long id) {
        return superService.deleteUser(id);
    }

}
