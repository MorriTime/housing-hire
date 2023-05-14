package hire.service.sup.service;

import com.hire.common.base.response.ResponseData;
import com.hire.common.mybatis.entity.PageDto;
import hire.service.sup.controller.SuperRequest.CreateAdminRequest;
import hire.service.sup.entity.User;
import hire.service.sup.entity.response.UserResponse;

public interface SuperService {

    ResponseData<PageDto<UserResponse>> getUsers(int role, Integer current, Integer size);

    ResponseData<String> insertAdmin(CreateAdminRequest user);

    ResponseData<String> deleteUser(Long id);


}
