package com.scsoft.xmgl.info.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.scpt.annotation.MyBatisDao;
import com.scsoft.xmgl.info.entity.Info;
import com.scsoft.xmgl.info.entity.WorkSummary;

import java.util.Date;
import java.util.List;

@MyBatisDao
public interface WorkSummaryMapper extends BaseMapper<WorkSummary> {
    boolean addComment(WorkSummary workSummary);
    List<WorkSummary> getByuserName(String userName);
    WorkSummary isExit(String userName,String proName, Date beginDate,Date endDate);
}
