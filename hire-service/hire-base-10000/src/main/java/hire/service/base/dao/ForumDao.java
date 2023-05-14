package hire.service.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hire.service.base.entity.Forum;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ForumDao extends BaseMapper<Forum> {

}
