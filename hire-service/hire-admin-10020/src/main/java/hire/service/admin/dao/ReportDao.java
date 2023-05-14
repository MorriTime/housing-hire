package hire.service.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hire.service.admin.entity.Report;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReportDao extends BaseMapper<Report> {


    @Delete("DELETE FROM house WHERE id = #{kindId}")
    void delSource(Long kindId);

    @Delete("DELETE FROM forum WHERE id = #{kindId}")
    void delForum(Long kindId);
}
