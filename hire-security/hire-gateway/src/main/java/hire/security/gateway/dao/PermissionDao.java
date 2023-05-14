package hire.security.gateway.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hire.common.base.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionDao extends BaseMapper<Permission> {
}
