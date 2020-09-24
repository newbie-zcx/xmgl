package com.scsoft.xmgl.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.scpt.annotation.MyBatisDao;
import com.scsoft.xmgl.work.entity.Weekly;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@MyBatisDao
public interface WeeklyMapper extends BaseMapper<Weekly> {
    Weekly getByUserName(String userName);
    List<Weekly> getByProId(@Param("weeklyIdList") List<Integer> weeklyIdList,Date startDate,Date endDate);
    List<Weekly> getHour(@Param("weeklyIdList") List<Integer> weeklyIdList,Date dateStart,Date dateEnd);
}
