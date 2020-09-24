package com.scsoft.xmgl.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.scpt.annotation.MyBatisDao;
import com.scsoft.xmgl.work.entity.ProWeekly;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao
public interface ProWeeklyMapper extends BaseMapper<ProWeekly> {
    ProWeekly getByWeeklyId(int weeklyId);
    List<ProWeekly> getByProId(int proId);
    int add(@Param("proId") int proId, @Param("weeklyId") int weeklyId);
    int update(@Param("proId") int proId, @Param("weeklyId") int weeklyId);
    int deleteByWeeklyId(@Param("weeklyId") int weeklyId);
}
