package hire.service.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hire.service.user.entity.House;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HouseDao extends BaseMapper<House> {
}
