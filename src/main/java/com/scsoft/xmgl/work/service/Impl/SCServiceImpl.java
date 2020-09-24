package com.scsoft.xmgl.work.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.xmgl.work.entity.SC;
import com.scsoft.xmgl.work.mapper.SCMapper;
import com.scsoft.xmgl.work.service.ISCService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SCServiceImpl extends ServiceImpl<SCMapper, SC> implements ISCService {

    @Resource
    private SCMapper scMapper;

    @Override
    public int add(SC sc) {
        scMapper.insert(sc);
        return sc.getId();
    }


    //content不具唯一性，获取id失败
    @Override
    public SC getByContent(String content) {
        QueryWrapper<SC> wrapper = new QueryWrapper<SC>();
        if (StringUtils.isNotBlank(content)){
            wrapper.eq("content",content);
        }
        return scMapper.selectOne(wrapper);
    }
}
