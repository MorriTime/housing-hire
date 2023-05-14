package hire.service.sup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hire.service.sup.entity.User;
import hire.service.sup.entity.response.UserResponse;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SuperDao extends BaseMapper<User> {

    @Select("SELECT " +
            "    count(*) " +
            "FROM " +
            "    user u " +
            "LEFT JOIN user_role ur on u.id = ur.user_id " +
            "WHERE " +
            "    role_id = #{role}")
    long selectRoleCount(int role);

    @Select("SELECT " +
            "    user_id " +
            "FROM " +
            "    user_role " +
            "WHERE role_id = #{role} " +
            "LIMIT #{offset}, #{size}")
    List<Long> selectRoleUserId(@Param("role") int role, @Param("offset") long offset, @Param("size") long size);

    @Select("SELECT " +
            "    id, username, nick, phone, sex, create_time, update_time " +
            "FROM " +
            "    user " +
            "WHERE " +
            "    id = #{userId}")
    UserResponse selectUser(long userId);

    @Delete("DELETE FROM user_role WHERE user_id = #{id}")
    int delUserRole(Long id);
}
