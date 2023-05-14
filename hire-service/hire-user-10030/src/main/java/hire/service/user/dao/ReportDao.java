package hire.service.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hire.service.user.entity.Report;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReportDao extends BaseMapper<Report> {
}
