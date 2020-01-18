package com.scsoft.wlyz.discuss.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scsoft.scpt.common.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.scsoft.wlyz.discuss.entity.IssueShiro;
import com.scsoft.wlyz.discuss.mapper.IssueShiroMapper;
import com.scsoft.wlyz.discuss.service.IIssueShiroService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 议题权限关系表 服务实现类
 * </p>
 *
 * @author wangchao
 * @since 2020-01-18
 */
@Service
public class IssueShiroServiceImpl extends ServiceImpl<IssueShiroMapper, IssueShiro> implements IIssueShiroService {
     @Resource
    private IssueShiroMapper issueShiroMapper;
    /**
     * 通过分页查询
     * @return PageResult
     */
    @Override
    public PageResult<IssueShiro> listPage(int pageNum, int pageSize, IssueShiro issueShiro) {
       Page<IssueShiro> page = new Page<IssueShiro>(pageNum, pageSize);
         QueryWrapper<IssueShiro> ew = new QueryWrapper<IssueShiro>();
          getEntityWrapper(ew,issueShiro);
         IPage iPage = issueShiroMapper.selectPage(page,ew);
          return new PageResult<IssueShiro>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public List<IssueShiro> selectList(IssueShiro issueShiro) {
        QueryWrapper<IssueShiro> ew = new QueryWrapper<IssueShiro>();
        getEntityWrapper(ew,issueShiro);
        return issueShiroMapper.selectList(ew);

     }
    /**
     *  公共查询条件
     * @param entityWrapper
     * @return
     */
    public QueryWrapper<IssueShiro> getEntityWrapper(QueryWrapper<IssueShiro> entityWrapper,IssueShiro issueShiro){
        return entityWrapper;
    }


    /**
         *  逻辑删除
         */
        @Override
        public Boolean logicDelete(IssueShiro issueShiro){
            issueShiro.setIsDel(1);
            return this.updateById(issueShiro);
        }
}
