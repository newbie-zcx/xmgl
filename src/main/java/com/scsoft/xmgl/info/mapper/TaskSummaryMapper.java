package com.scsoft.xmgl.info.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.scpt.annotation.MyBatisDao;
import com.scsoft.xmgl.info.entity.TaskSummary;

import java.util.Date;
import java.util.Map;

@MyBatisDao
public interface TaskSummaryMapper extends BaseMapper<TaskSummary>{
    boolean deleteByWorkSummary(Map<String,Object> map);
}
