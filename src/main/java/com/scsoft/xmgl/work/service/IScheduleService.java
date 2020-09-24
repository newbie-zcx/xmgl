package com.scsoft.xmgl.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.xmgl.work.entity.ProUser;

import java.util.List;

public interface IScheduleService extends IService<ProUser> {
    boolean addMember(int proId, List<Integer> userIdList);
}
