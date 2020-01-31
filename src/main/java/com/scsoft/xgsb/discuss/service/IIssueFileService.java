package com.scsoft.xgsb.discuss.service;

import com.scsoft.xgsb.discuss.entity.IssueFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.scpt.common.PageResult;

import java.util.List;

/**
 * <p>
 * 议题附件关系表 服务类
 * </p>
 *
 * @author wangchao
 * @since 2020-01-18
 */

public interface IIssueFileService extends IService<IssueFile> {
    /**
     *  分页查询结果集
     * @param issueFile
     */
    PageResult<IssueFile> listPage(int pageNum, int pageSize,IssueFile issueFile);

    /**
      *  list查询
      */
    List<IssueFile> selectList(IssueFile issueFile);

    /**
      *  逻辑删除
      */
    Boolean logicDelete(IssueFile issueFile);
}

