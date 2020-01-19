package com.scsoft.wlyz.discuss.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.wlyz.discuss.entity.Issue;
import com.scsoft.wlyz.discuss.model.IssueModel;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chengshangshu
 * @since 2020-01-09
 */

public interface IIssueService extends IService<Issue> {
    /**
     *  分页查询结果集
     * @param issue
     */
    PageResult<Issue> listPage(Integer page, Integer limit, Issue issue);

    /**
      *  list查询
      */
    List<Issue> selectList(Issue issue);

    /**
      *  逻辑删除
      */
    Boolean logicDelete(Issue issue);

    boolean saveModel(IssueModel issue);

    PageResult<Issue> listPageByMap(Integer page, Integer limit, QueryWrapper queryWrapper);
}

