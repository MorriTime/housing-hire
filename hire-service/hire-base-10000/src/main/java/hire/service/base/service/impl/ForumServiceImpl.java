package hire.service.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hire.common.base.exception.BizException;
import com.hire.common.base.response.ResponseData;
import com.hire.common.mybatis.entity.PageDto;
import com.hire.common.web.utils.TypeUtil;
import hire.service.base.dao.ForumDao;
import hire.service.base.dao.UserDao;
import hire.service.base.entity.Forum;
import hire.service.base.entity.User;
import hire.service.base.entity.response.ForumListResponse;
import hire.service.base.entity.response.ForumResponse;
import hire.service.base.service.ForumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForumServiceImpl implements ForumService {

    private final ForumDao forumDao;

    private final UserDao userDao;

    @Override
    public ResponseData<PageDto<ForumListResponse>> forms(Integer current, Integer size) {
        Page<Forum> page = new Page<>();
        page.setCurrent(current == null ? 0 : current)
                .setSize(current == null ? 5 : size);

        QueryWrapper<Forum> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 0);
        wrapper.orderByDesc("create_time");

        page.setTotal(forumDao.selectCount(wrapper));

        List<Forum> list = forumDao.selectPage(page, wrapper).getRecords();
        List<ForumListResponse> listResponses = list.stream()
                .map(forum -> {
                    ForumListResponse forumListResponse = new ForumListResponse();
                    forumListResponse.setId(forum.getId());
                    forumListResponse.setTitle(forum.getTitle());
                    forumListResponse.setContent(forum.getContent());
                    forumListResponse.setKind(forum.getKind());
                    forumListResponse.setImg(TypeUtil.str2List(forum.getImg()));
                    forumListResponse.setCreateTime(forum.getCreateTime());
                    return forumListResponse;
                })
                .collect(Collectors.toList());

        PageDto<ForumListResponse> forumListResponsePageDto = new PageDto<>(listResponses, page.getTotal(), page.getSize(), page.getCurrent());

        return ResponseData.Success(forumListResponsePageDto);
    }

    @Override
    public ResponseData<ForumResponse> forum(Long id) {
        Forum forum = forumDao.selectById(id);
        User user = userDao.selectById(id);

        if (forum == null || user == null) {
            log.error("forum查询错误");
            throw new BizException("id异常");
        }

        ForumResponse forumResponse = new ForumResponse().apply(forum, user);

        return ResponseData.Success(forumResponse);
    }
}
