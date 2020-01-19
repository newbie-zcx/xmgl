package com.scsoft.wlyz.discuss.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.wlyz.discuss.entity.Issue;
import com.scsoft.wlyz.discuss.entity.IssueFile;
import com.scsoft.wlyz.discuss.entity.IssueShiro;
import com.scsoft.wlyz.discuss.entity.ReplyFile;
import com.scsoft.wlyz.discuss.mapper.IssueFileMapper;
import com.scsoft.wlyz.discuss.mapper.IssueMapper;
import com.scsoft.wlyz.discuss.mapper.IssueShiroMapper;
import com.scsoft.wlyz.discuss.model.IssueModel;
import com.scsoft.wlyz.discuss.service.IIssueService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
    @Resource
    private IssueFileMapper issueFileMapper;
    @Resource
    private IssueShiroMapper issueShiroMapper;

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

        @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveModel(IssueModel model) {
            Issue issue = new Issue();
            issue.setTitle(model.getTitle());
            issue.setContent(model.getContent());
            issue.setIssueType(model.getIssueType());
            issue.setIssueShiroType(model.getIssueShiroType());

            Integer successNum = this.issueMapper.insert(issue);

        List<Integer> fileList = model.getFileList();
        if(!(null == fileList || fileList.size() == 0)){
            for(Integer file : fileList){
                IssueFile issueFile = new IssueFile();
                issueFile.setFileId(file);
                issueFile.setIssueId(issue.getId());
                issueFileMapper.insert(issueFile);
            }
        }

        List<String> userList = model.getUserList();
        if(!(null == userList || userList.size() == 0)){
            for(String userName : userList){
                IssueShiro issueShiro = new IssueShiro();
                issueShiro.setUserName(userName);
                issueShiro.setIssueId(issue.getId());
                issueShiroMapper.insert(issueShiro);
            }
        }
            if(successNum >= 1){
                return true;
            }else {
                return false;
            }
    }

    @Override
    public PageResult<Issue> listPageByMap(Integer page, Integer limit, QueryWrapper queryWrapper) {
        Page<Issue> issuePage = new Page<Issue>(page, limit);
        IPage iPage = issueMapper.selectPage(issuePage, queryWrapper);
        return new PageResult<Issue>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public PageResult<Issue> listHostPage(Integer page, Integer limit, Issue issue) {
        List<Integer> ids = this.issueMapper.hostIdList();
        if(null == ids){
            return new PageResult<Issue>();
        }
        Page<Issue> issuePage = new Page<Issue>(page, limit);
        QueryWrapper<Issue> ew = new QueryWrapper<Issue>();
        ew.in("ID", ids);
        IPage iPage = issueMapper.selectPage(issuePage,ew);
        return new PageResult<Issue>(iPage.getRecords(), iPage.getTotal());
    }
}
