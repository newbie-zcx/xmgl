package com.scsoft.xmgl.info.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.info.entity.Milepost;

public interface IMilepostInfoService extends IService<Milepost> {
    Milepost queryById(int id);
    PageResult list(int pageNum, int pageSize,String proId,String proName,String milepostStartDate,String milepostEndDate,String milepostStatus);
    boolean add(Milepost milepost);
    boolean update(Milepost milepost);
    boolean delete(int id);
    boolean addMilepost(Milepost milepost);
    void queryall();
}
