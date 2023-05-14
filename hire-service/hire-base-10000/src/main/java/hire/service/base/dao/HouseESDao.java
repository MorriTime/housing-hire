package hire.service.base.dao;

import hire.service.base.entity.House;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

@Mapper
public interface HouseESDao extends ElasticsearchRepository<House, Long> {
}
