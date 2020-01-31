package com.scsoft.xgsb.discuss.service;

import com.scsoft.xgsb.discuss.entity.IssueShiro;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.scpt.common.PageResult;

import java.util.List;

/**
 * <p>
 * 议题权限关系表 服务类
 * </p>
 *
 * @author wangchao
 * @since 2020-01-18
 */

public interface IIssueShiroService extends IService<IssueShiro> {
    /**
     *  分页查询结果集
     * @param issueShiro
     */
    PageResult<IssueShiro> listPage(int pageNum, int pageSize,IssueShiro issueShiro);

    /**
      *  list查询
      */
    List<IssueShiro> selectList(IssueShiro issueShiro);

    /**
      *  逻辑删除
      */
    Boolean logicDelete(IssueShiro issueShiro);
}

