package com.scsoft.xmgl.work.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.xmgl.work.entity.AC;
import com.scsoft.xmgl.work.mapper.ACMapper;
import com.scsoft.xmgl.work.service.IACService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ACServiceImpl extends ServiceImpl<ACMapper, AC> implements IACService {
    @Resource
    private ACMapper acMapper;


    @Override
    public int add(AC ac) {
        acMapper.insert(ac);
        return ac.getId();
    }
}
