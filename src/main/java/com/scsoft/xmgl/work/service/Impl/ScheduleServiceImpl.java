package com.scsoft.xmgl.work.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.xmgl.work.service.IScheduleService;
import com.scsoft.xmgl.work.entity.ProUser;
import com.scsoft.xmgl.work.mapper.ProUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ScheduleServiceImpl extends ServiceImpl<ProUserMapper, ProUser> implements IScheduleService {
    @Resource
    private ProUserMapper proUserMapper;

    @Override
    public boolean addMember(int proId, List<Integer> userIdList) {
        QueryWrapper<ProUser> wrapper = new QueryWrapper<ProUser>();
        wrapper.eq("pro_id",proId);
        if (proUserMapper.IsExit(proId)>0){
            proUserMapper.delete(wrapper);
        }
        return proUserMapper.addMember(proId,userIdList)>0;
    }
}
