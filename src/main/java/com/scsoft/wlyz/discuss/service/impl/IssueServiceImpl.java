package com.scsoft.wlyz.discuss.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.wlyz.discuss.entity.Reply;
import org.springframework.stereotype.Service;
import com.scsoft.wlyz.discuss.entity.Issue;
import com.scsoft.wlyz.discuss.mapper.IssueMapper;
import com.scsoft.wlyz.discuss.service.IIssueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chengshangshu
 * @since 2020-01-09
 */
@Service
public class IssueServiceImpl extends ServiceImpl<IssueMapper, Issue> implements IIssueService {
    @Resource
    private IssueMapper issueMapper;
    /**
     * 通过分页查询
     * @param page
     * @return PageResult
     */
    @Override
    public PageResult<Issue> listPage(Integer page, Integer limit, Issue issue) {
       Page<Issue> issuePage = new Page<Issue>(page, limit);
         QueryWrapper<Issue> ew = new QueryWrapper<Issue>();

         IPage iPage = issueMapper.selectPage(issuePage,ew);
          return new PageResult<Issue>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public List<Issue> selectList(Issue issue) {
        QueryWrapper<Issue> ew = new QueryWrapper<Issue>();

        return issueMapper.selectList(ew);

     }






    /**
         *  逻辑删除
         */
        @Override
        public Boolean logicDelete(Issue issue){
            issue.setIsDel(1);
            return this.updateById(issue);
        }
}
