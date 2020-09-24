package com.scsoft.xmgl.info.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.scpt.annotation.MyBatisDao;
import com.scsoft.xmgl.info.entity.Info;

import java.util.List;
import java.util.Map;


@MyBatisDao
public interface UnfinishedProjectMapper extends BaseMapper<Info> {
      Info selectByProName(String proName);
}
