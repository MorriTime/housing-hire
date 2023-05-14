package hire.service.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hire.common.base.entity.CommonUser;
import hire.service.base.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao extends BaseMapper<User> {

    @Select("SELECT nick, avatar, sex " +
            "FROM user " +
            "WHERE id = #{userId}")
    CommonUser selectUser(String userId);
}
