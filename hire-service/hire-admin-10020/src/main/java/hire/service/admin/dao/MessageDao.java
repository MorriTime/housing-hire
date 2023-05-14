package hire.service.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hire.service.admin.entity.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageDao extends BaseMapper<Message> {
}
