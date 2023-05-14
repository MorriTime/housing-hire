package hire.service.base.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MessageDao {

    @Select("SELECT count(*) FROM message WHERE user_id = #{userId}")
    int count(String userId);
}
