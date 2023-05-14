package hire.service.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hire.common.base.exception.BizException;
import com.hire.common.base.response.ResponseData;
import com.hire.common.mybatis.entity.PageDto;
import hire.service.user.dao.MessageDao;
import hire.service.user.entity.Message;
import hire.service.user.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageDao messageDao;

    @Override
    public ResponseData<PageDto<Message>> list(Long id, Integer current, Integer size) {
        Page<Message> page = new Page<>();
        page.setCurrent(current == null ? 0 : current)
                .setSize(size == null ? 5 : size);

        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", id);
        wrapper.orderByAsc("create_time");

        page.setTotal(messageDao.selectCount(wrapper));

        Page<Message> record = messageDao.selectPage(page, wrapper);

        PageDto<Message> messagePageDto = new PageDto<>(record);

        return ResponseData.Success(messagePageDto);
    }

    @Override
    public ResponseData<String> delete(Long id) {
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getId, id);
        Message message = messageDao.selectOne(wrapper);

        if (message == null) {
            return ResponseData.Error("id异常");
        }
        messageDao.delete(wrapper);
        return ResponseData.Success();
    }

    @Override
    public ResponseData<String> update(Long id) {
        UpdateWrapper<Message> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id);

        Message message = messageDao.selectOne(wrapper);
        if (message == null) {
            throw new BizException("id异常");
        } else {
            message.setStatus(1);
            if (messageDao.update(message, wrapper) < 1) {
                throw new BizException("SQL异常");
            }
        }

        return ResponseData.Success();
    }
}
