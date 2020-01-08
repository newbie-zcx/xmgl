package com.scsoft.wlyz.discuss.service;

import com.scsoft.scpt.common.PageResult;
import com.scsoft.wlyz.discuss.entity.Reply;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 回复表 服务类
 * </p>
 *
 * @author wangchao
 * @since 2020-01-08
 */

public interface IReplyService extends IService<Reply> {
    /**
     *  分页查询结果集
     * @param reply
     * @return
     */
    PageResult<Reply> listPage(Integer page, Integer limit, Reply reply);

    /**
      *  list查询
      */
    List<Reply> selectList(Reply reply);

}

