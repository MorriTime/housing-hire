package hire.service.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hire.service.base.entity.House;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HouseDao extends BaseMapper<House> {

}
