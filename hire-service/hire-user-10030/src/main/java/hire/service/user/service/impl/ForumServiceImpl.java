package hire.service.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hire.common.base.exception.BizException;
import com.hire.common.base.response.ResponseData;
import com.hire.common.mybatis.entity.PageDto;
import com.hire.common.web.utils.TypeUtil;
import hire.service.user.controller.request.ForumCreateRequest;
import hire.service.user.controller.request.ForumUpdateRequest;
import hire.service.user.dao.ForumDao;
import hire.service.user.entity.Forum;
import hire.service.user.entity.response.ForumListResponse;
import hire.service.user.service.ForumService;
import hire.service.user.utils.IndexConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ForumServiceImpl implements ForumService {

    private final ForumDao forumDao;

    @Override
    public ResponseData<String> create(ForumCreateRequest forumCreateRequest) {
        Forum forum = forumCreateRequest.convert(IndexConverter::convertForum);
        if (forumDao.insert(forum) < 1) throw new BizException("SQL异常");

        return ResponseData.Success();
    }

    @Override
    public ResponseData<PageDto<ForumListResponse>> list(Long id, Integer current, Integer size) {
        Page<Forum> page = new Page<>();
        page.setCurrent(current == null ? 0 : current)
                .setSize(size == null ? 5 : size);

        QueryWrapper<Forum> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", id);
        wrapper.orderByDesc("create_time");

        page.setTotal(forumDao.selectCount(wrapper));

        List<Forum> forums = forumDao.selectPage(page, wrapper).getRecords();
        List<ForumListResponse> forumListResponses = forums.stream().map(forum -> ForumListResponse.builder()
                .id(forum.getId())
                .title(forum.getTitle())
                .content(forum.getContent())
                .kind(forum.getKind())
                .img(TypeUtil.str2List(forum.getImg()))
                .createTime(forum.getCreateTime())
                .build()).collect(Collectors.toList());

        PageDto<ForumListResponse> forumListResponsePageDto = new PageDto<>(forumListResponses, page.getTotal(), page.getSize(), page.getCurrent());

        return ResponseData.Success(forumListResponsePageDto);
    }

    @Override
    public ResponseData<String> delete(Long id) {
        LambdaQueryWrapper<Forum> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Forum::getId, id);
        Forum forum = forumDao.selectOne(wrapper);

        if (forum == null) {
            ResponseData.Error("id异常");
        }
        forumDao.delete(wrapper);
        return ResponseData.Success();
    }

    @Override
    public ResponseData<String> update(ForumUpdateRequest forumUpdateRequest) {
        Forum forum = forumUpdateRequest.convert(IndexConverter::convertUpForum);
        UpdateWrapper<Forum> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", forum.getId());

        if (forumDao.update(forum, wrapper) < 1) {
            throw new BizException("SQL异常");
        }

        return ResponseData.Success();
    }
}
