package com.scsoft.wlyz.discuss.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scsoft.wlyz.discuss.entity.Reply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 回复表 Mapper 接口
 * </p>
 *
 * @author wangchao
 * @since 2020-01-08
 */
public interface ReplyMapper extends BaseMapper<Reply> {

    IPage<Reply> selectListByIssue(Page<Reply> page, Map<String, Object> paramMap);
}
