package com.scsoft.xmgl.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.scpt.annotation.MyBatisDao;
import com.scsoft.xmgl.work.entity.WeeklyContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao
public interface WeeklyContentMapper extends BaseMapper<WeeklyContent> {
    List<WeeklyContent> getByWeeklyId(int weeklyId);
    int addContent(@Param("weeklyId") int weeklyId,@Param("scId") int scId);
    int deleteByWeeklyId(int weeklyId);
}
