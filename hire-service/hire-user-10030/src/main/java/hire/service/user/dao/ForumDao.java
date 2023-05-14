package hire.service.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hire.service.user.entity.Forum;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ForumDao extends BaseMapper<Forum> {
}
