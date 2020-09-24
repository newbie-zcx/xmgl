package com.scsoft.xmgl.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.xmgl.work.entity.SC;

public interface ISCService extends IService<SC> {
    int add(SC sc);
    SC getByContent(String content);
}
