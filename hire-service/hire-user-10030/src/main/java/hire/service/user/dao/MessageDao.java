package hire.service.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hire.service.user.entity.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageDao extends BaseMapper<Message> {
}
