package hire.service.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hire.service.base.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleDao extends BaseMapper<UserRole> {
}
