package com.hire.common.mybatis.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class PageDto<T> {

    private List<T> records;

    private long total;

    private long size;

    private long current;

    public PageDto(Page<T> page) {
        records = page.getRecords();
        total = page.getTotal();
        size = page.getSize();
        current = page.getCurrent();
    }
}
