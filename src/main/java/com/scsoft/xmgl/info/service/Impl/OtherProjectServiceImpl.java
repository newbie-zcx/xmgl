package com.scsoft.xmgl.info.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.scpt.exception.BusinessException;
import com.scsoft.xmgl.info.entity.Info;
import com.scsoft.xmgl.info.mapper.OtherProjectMapper;
import com.scsoft.xmgl.info.mapper.UnfinishedProjectMapper;
import com.scsoft.xmgl.info.service.IOtherProjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OtherProjectServiceImpl extends ServiceImpl<OtherProjectMapper, Info> implements IOtherProjectService {
    @Resource
    private OtherProjectMapper otherProjectMapper;
    @Resource
    private UnfinishedProjectMapper unfinishedProjectMapper;

    @Override
    public PageResult<Info>  list(int pageNum, int pageSize,String proName,String proManager,String proDept) {
        QueryWrapper<Info> wrapper = new QueryWrapper<Info>();
        if (StringUtils.isNotBlank(proName)) {
            wrapper.like("pro_name", proName);
        }
        if (StringUtils.isNotBlank(proManager)) {
            wrapper.like("pro_manager", proManager);
        }
        if (StringUtils.isNotBlank(proDept)) {
            wrapper.like("pro_dept", proDept);
        }
        wrapper.eq("pro_type", "内部研发");
        Page<Info> infoPage = new Page<Info>(pageNum, pageSize);
        List<Info>  infoList = otherProjectMapper.selectPage(infoPage,wrapper).getRecords();
        return new PageResult<>(infoList, infoPage.getTotal());
    }

    @Override
    public boolean add(Info info) {
        if (unfinishedProjectMapper.selectByProName(info.getProName())==null){
            return unfinishedProjectMapper.insert(info) > 0;
        }else {
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        boolean rs = otherProjectMapper.deleteById(id) > 0;
        return rs;
    }

    @Override
    public boolean update(Info info) {
        boolean rs = otherProjectMapper.updateById(info) > 0;
        return rs;
    }

    @Override
    public List<Info> listInfo() {
        return otherProjectMapper.selectList(new QueryWrapper<Info>());
    }

}

