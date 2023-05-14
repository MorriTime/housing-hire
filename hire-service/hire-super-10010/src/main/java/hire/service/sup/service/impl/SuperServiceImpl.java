package hire.service.sup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hire.common.base.exception.BizException;
import com.hire.common.base.response.ResponseData;
import com.hire.common.mybatis.entity.PageDto;
import com.hire.common.web.enums.RoleEnum;
import hire.service.sup.controller.SuperRequest.CreateAdminRequest;
import hire.service.sup.convertet.UserConverter;
import hire.service.sup.dao.SuperDao;
import hire.service.sup.dao.UserRoleDao;
import hire.service.sup.entity.User;
import hire.service.sup.entity.UserRole;
import hire.service.sup.entity.response.UserResponse;
import hire.service.sup.service.SuperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class SuperServiceImpl implements SuperService {

    private final SuperDao superDao;

    private final UserRoleDao userRoleDao;

    @Override
    public ResponseData<PageDto<UserResponse>> getUsers(int role, Integer current, Integer size) {
        Page<UserResponse> page = new Page<>();
        page.setCurrent(current == null ? 0 : current)
                .setSize(size == null ? 5 : size);
        page.setTotal(superDao.selectRoleCount(role));

        List<Long> userIdList = superDao.selectRoleUserId(role, page.offset(), page.getSize());
        List<UserResponse> list = new ArrayList<>(userIdList.size());
        String roleName = role == 1 ? RoleEnum.USER.getName() : RoleEnum.ADMIN.getName();
        for (long userId : userIdList) {
            UserResponse userResponse = superDao.selectUser(userId);
            userResponse.setRole(roleName);
            list.add(userResponse);
        }
        page.setRecords(list);
        PageDto<UserResponse> userResponsePageDto = new PageDto<>(page);

        return ResponseData.Success(userResponsePageDto);
    }

    @Override
    @Transactional
    public ResponseData<String> insertAdmin(CreateAdminRequest user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, user.getUsername());
        List<User> userList = superDao.selectList(lambdaQueryWrapper);

        if (userList.size() != 0) {
            return ResponseData.Error("账号已存在");
        }

        User admin = user.convert(UserConverter::convertUser);

        try {
            if (superDao.insert(admin) < 1) {
                throw new BizException("SQL异常");
            } else {
                UserRole userRole = new UserRole();
                userRole.setUserId(admin.getId());
                userRole.setRoleId(RoleEnum.ADMIN.getId());
                userRoleDao.insert(userRole);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage(), e);
            throw new BizException("事务异常");
        }
        return ResponseData.Success();
    }

    @Override
    @Transactional
    public ResponseData<String> deleteUser(Long id) {
        try {
            if (superDao.deleteById(id) < 1) {
                throw new BizException("SQL异常");
            } else {
                if (superDao.delUserRole(id) < 1) {
                    throw new BizException("SQL异常");
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
