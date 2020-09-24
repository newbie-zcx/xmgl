package com.scsoft.xmgl.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.xmgl.work.entity.AC;

public interface IACService extends IService<AC> {
    int add(AC ac);
}
