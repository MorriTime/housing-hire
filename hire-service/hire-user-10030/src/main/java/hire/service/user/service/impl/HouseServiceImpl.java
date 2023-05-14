package hire.service.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hire.common.base.exception.BizException;
import com.hire.common.base.response.ResponseData;
import com.hire.common.mybatis.entity.PageDto;
import com.hire.common.web.utils.TypeUtil;
import hire.service.user.controller.request.HouseCreateRequest;
import hire.service.user.controller.request.HouseUpdateRequest;
import hire.service.user.dao.HouseDao;
import hire.service.user.entity.House;
import hire.service.user.entity.response.HouseListResponse;
import hire.service.user.service.HouseService;
import hire.service.user.utils.IndexConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseDao houseDao;

    @Override
    public ResponseData<String> create(HouseCreateRequest houseCreateRequest) {
        House house = houseCreateRequest.convert(IndexConverter::convertHouse);
        if (houseDao.insert(house) < 1) throw new BizException("SQL异常");

        return ResponseData.Success();
    }

    @Override
    public ResponseData<PageDto<HouseListResponse>> list(Long id, Integer current, Integer size) {
        Page<House> page = new Page<>();
        page.setCurrent(current == null ? 0 : current)
                .setSize(size == null ? 5 : size);

        QueryWrapper<House> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", id);
        wrapper.orderByDesc("create_time");

        page.setTotal(houseDao.selectCount(wrapper));

        List<House> houses = houseDao.selectPage(page, wrapper).getRecords();
        List<HouseListResponse> houseListResponses = houses.stream().map(house -> HouseListResponse.builder()
                .id(house.getId())
                .title(house.getTitle())
                .introduce(house.getIntroduce())
                .price(house.getPrice())
                .city(house.getCity())
                .address(house.getAddress())
                .image(TypeUtil.str2List(house.getImage()))
                .cover(TypeUtil.str2List(house.getImage()).get(0))
                .tags(TypeUtil.str2List(house.getTags()))
                .type(house.getType())
                .createTime(house.getCreateTime())
                .build()).collect(Collectors.toList());

        PageDto<HouseListResponse> houseListResponsePageDto = new PageDto<>(houseListResponses, page.getTotal(), page.getSize(), page.getCurrent());

        return ResponseData.Success(houseListResponsePageDto);
    }

    @Override
    public ResponseData<String> delete(Long id) {
        LambdaQueryWrapper<House> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(House::getId, id);
        House house = houseDao.selectOne(wrapper);

        if (house == null) {
            ResponseData.Error("id异常");
        }
        houseDao.delete(wrapper);
        return ResponseData.Success();
    }

    @Override
    public ResponseData<String> update(HouseUpdateRequest houseUpdateRequest) {
        House house = houseUpdateRequest.convert(IndexConverter::convertUpHouse);
        UpdateWrapper<House> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", house.getId());

        if (houseDao.update(house, wrapper) < 1) {
            throw new BizException("SQL异常");
        }

        return ResponseData.Success();
    }
}
