package com.scsoft.xmgl.info.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.scpt.annotation.MyBatisDao;
import com.scsoft.xmgl.info.entity.Milepost;

@MyBatisDao
public interface MilepostInfoMapper extends BaseMapper<Milepost> {
    boolean addMilepost(Milepost milepost);
    Milepost milepostIsExit(String proName,String milepostName);
}
