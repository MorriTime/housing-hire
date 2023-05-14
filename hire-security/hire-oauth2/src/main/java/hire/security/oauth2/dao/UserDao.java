package hire.security.oauth2.dao;

import com.hire.common.base.entity.CommonUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserDao {
    @Select("SELECT " +
            "    p.url " +
            "FROM " +
            "    user_role ur " +
            "LEFT JOIN role_permission rp ON ur.role_id = rp.role_id " +
            "LEFT JOIN permission p ON rp.permission_id = p.id " +
            "WHERE " +
            "    ur.user_id = #{id}")
    List<String> queryAuthor(Long id);

    @Select("SELECT r.name " +
            "    FROM user_role u " +
            "LEFT JOIN role r ON r.id = u.role_id " +
            "    WHERE u.user_id = #{id}")
    List<String> queryRole(Long id);

    @Select("SELECT * from user where username = #{username}")
    CommonUser selectOne(String username);
}
