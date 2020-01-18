package com.scsoft.wlyz.discuss.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scsoft.scpt.common.PageResult;
import org.springframework.stereotype.Service;
import com.scsoft.wlyz.discuss.entity.IssueFile;
import com.scsoft.wlyz.discuss.mapper.IssueFileMapper;
import com.scsoft.wlyz.discuss.service.IIssueFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 议题附件关系表 服务实现类
 * </p>
 *
 * @author wangchao
 * @since 2020-01-18
 */
@Service
public class IssueFileServiceImpl extends ServiceImpl<IssueFileMapper, IssueFile> implements IIssueFileService {
     @Resource
    private IssueFileMapper issueFileMapper;
    /**
     * 通过分页查询
     * @return PageResult
     */
    @Override
    public PageResult<IssueFile> listPage(int pageNum, int limit, IssueFile issueFile) {
       Page<IssueFile> page = new Page<IssueFile>(pageNum, limit);
         QueryWrapper<IssueFile> ew = new QueryWrapper<IssueFile>();
          getEntityWrapper(ew,issueFile);
         IPage iPage = issueFileMapper.selectPage(page,ew);
          return new PageResult<IssueFile>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public List<IssueFile> selectList(IssueFile issueFile) {
        QueryWrapper<IssueFile> ew = new QueryWrapper<IssueFile>();
        getEntityWrapper(ew,issueFile);
        return issueFileMapper.selectList(ew);

     }
    /**
     *  公共查询条件
     * @param entityWrapper
     * @return
     */
    public QueryWrapper<IssueFile> getEntityWrapper(QueryWrapper<IssueFile> entityWrapper,IssueFile issueFile){
        return entityWrapper;
    }


    /**
         *  逻辑删除
         */
        @Override
        public Boolean logicDelete(IssueFile issueFile){
            issueFile.setIsDel(1);
            return this.updateById(issueFile);
        }
}
