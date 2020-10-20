package com.scsoft.xmgl.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.scpt.annotation.MyBatisDao;
import com.scsoft.xmgl.work.entity.AcountContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao
public interface AcountContentMapper extends BaseMapper<AcountContent> {
    List<AcountContent> getByAcountId(int acountId);
    int addContent(@Param("acountId") int acountId, @Param("acIdList") List<Integer> acIdList);
}
