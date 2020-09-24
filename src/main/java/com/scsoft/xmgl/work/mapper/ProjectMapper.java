package com.scsoft.xmgl.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.scpt.annotation.MyBatisDao;
import com.scsoft.xmgl.work.entity.Project;

@MyBatisDao
public interface ProjectMapper extends BaseMapper<Project> {
    Project getByProName(String proName);
}
