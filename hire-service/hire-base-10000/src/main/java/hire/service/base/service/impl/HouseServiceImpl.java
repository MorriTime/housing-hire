package hire.service.base.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hire.common.base.exception.BizException;
import com.hire.common.base.response.ResponseData;
import com.hire.common.mybatis.entity.PageDto;
import com.hire.common.web.utils.TypeUtil;
import hire.service.base.controller.request.HouseSelectRequest;
import hire.service.base.dao.HouseDao;
import hire.service.base.dao.UserDao;
import hire.service.base.entity.House;
import hire.service.base.entity.User;
import hire.service.base.entity.response.HouseListResponse;
import hire.service.base.entity.response.HouseResponse;
import hire.service.base.service.HouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HouseServiceImpl implements HouseService {

    private final HouseDao houseDao;

    private final UserDao userDao;

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public ResponseData<PageDto<HouseListResponse>> houses(HouseSelectRequest houseSelectRequest) {

        int current = houseSelectRequest.getCurrent() == null ? 0 : houseSelectRequest.getCurrent();
        int size = houseSelectRequest.getSize() == null ? 5 : houseSelectRequest.getSize();

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                // 条件构造
                .withQuery(builderHouseQuery(houseSelectRequest))
                // 分页条件
                .withPageable(PageRequest.of(current, size))
                .build();

        SearchHits<House> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery, House.class);

        List<HouseListResponse> houses = new ArrayList<>();
        if (searchHits.getTotalHits() > 0) {
            houses = searchHits.stream()
                    .map(SearchHit::getContent)
                    .map(house -> HouseListResponse.builder()
                            .id(house.getId())
                            .title(house.getTitle())
                            .introduce(house.getIntroduce())
                            .price(house.getPrice())
                            .city(house.getCity())
                            .address(house.getAddress())
                            .image(TypeUtil.str2List(house.getImage()))
                            .cover(TypeUtil.str2List(house.getImage()).get(0))
                            .tags(TypeUtil.str2List(house.getTags()))
                            .space(house.getSpace())
                            .floor(house.getFloor())
                            .term(house.getTerm())
                            .type(house.getType())
                            .createTime(house.getCreateTime())
                            .build()).collect(Collectors.toList());
        }

        PageDto<HouseListResponse> responsePageDto = new PageDto<>(houses, searchHits.getTotalHits(), current, size);

        return ResponseData.Success(responsePageDto);
    }

    private BoolQueryBuilder builderHouseQuery(HouseSelectRequest houseSelectRequest) {
        // 构建布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 关键字查询
        if (StrUtil.isNotBlank(houseSelectRequest.getContent())) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("introduce", houseSelectRequest.getContent()))
                    .should(QueryBuilders.matchQuery("title", houseSelectRequest.getContent()))
                    .should(QueryBuilders.matchQuery("tags", houseSelectRequest.getContent()));
        }
        // 城市
        if (StrUtil.isNotBlank(houseSelectRequest.getCity())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("city", houseSelectRequest.getCity()));
        }

        // 地址
        if (StrUtil.isNotBlank(houseSelectRequest.getAddress())) {
            boolQueryBuilder.filter(QueryBuilders.matchQuery("address", houseSelectRequest.getAddress()));
        }

        // 价格范围
        Double priceBegin = houseSelectRequest.getPriceBegin() == null ? 0.0 : houseSelectRequest.getPriceBegin().doubleValue();
        Double priceEnd = houseSelectRequest.getPriceEnd() == null ? Integer.MAX_VALUE : houseSelectRequest.getPriceEnd().doubleValue();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(priceBegin).lte(priceEnd));

        // 面积范围
        Double spaceBegin = houseSelectRequest.getSpaceBegin() == null ? 0.0 : houseSelectRequest.getSpaceBegin().doubleValue();
        Double spaceEnd = houseSelectRequest.getSpaceEnd() == null ? Integer.MAX_VALUE : houseSelectRequest.getSpaceEnd().doubleValue();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("space").gte(spaceBegin).lte(spaceEnd));

        // 楼层范围
        Integer floorBegin = houseSelectRequest.getFloorBegin() == null ? 0 : houseSelectRequest.getFloorBegin();
        Integer floorEnd = houseSelectRequest.getFloorEnd() == null ? 20 : houseSelectRequest.getFloorEnd();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("floor").gte(floorBegin).lte(floorEnd));

        // 最小租期范围
        Integer termBegin = houseSelectRequest.getTermBegin() == null ? 0 : houseSelectRequest.getTermBegin();
        Integer termEnd = houseSelectRequest.getTermEnd() == null ? 12 : houseSelectRequest.getTermEnd();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("term").gte(termBegin). lte(termEnd));

        boolQueryBuilder.filter(QueryBuilders.matchQuery("state", 0));

        return boolQueryBuilder;
    }

    @Override
    public ResponseData<HouseResponse> house(Long id) {
        QueryWrapper<House> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        House house = houseDao.selectOne(wrapper);
        User user = userDao.selectById(house.getUserId());

        if (house == null || user == null) throw new BizException("查询异常");

        HouseResponse houseResponse = new HouseResponse().apply(house, user);

        return ResponseData.Success(houseResponse);
    }
}
