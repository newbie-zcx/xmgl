package com.scsoft.wlyz.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.wlyz.system.entity.DictGroup;
import com.scsoft.wlyz.system.mapper.DictGroupMapper;
import com.scsoft.wlyz.system.service.IDictGroupService;
import com.scsoft.scpt.common.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 * 字典分组 服务实现类
 * </p>
 *
 * @author zhaopengfei
 * @CreateDate 2019-04-26
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @Version: 1.0
 */
@Service
public class DictGroupServiceImpl extends ServiceImpl<DictGroupMapper, DictGroup> implements IDictGroupService {
     @Resource
     private DictGroupMapper dictGroupMapper;


     @Override
     public DictGroup findById(Integer dictGroupId) {
          return dictGroupMapper.findById(dictGroupId);
     }

    /**
     * 分页查找
     * @param page
     * @param condition
     * @return PageResult
     */
     @Override
     public PageResult findAll(Page page, String condition) {
         QueryWrapper<DictGroup> ew = new QueryWrapper<DictGroup>();
         if (StringUtils.isNotBlank(condition)) {
             ew.like("code", condition);
         }
         ew.select("code","name","remark","id","create_date");
         List<DictGroup>  records = dictGroupMapper.selectPage(page, ew).getRecords();
         return new PageResult<DictGroup>(records, page.getTotal());
     }

    @Override
    public boolean logicDelete(DictGroup dictGroup) {
        Integer isDel = dictGroup.getIsDel();

        if (isDel==1){
            return true;
        }
        return false;
    }
}
