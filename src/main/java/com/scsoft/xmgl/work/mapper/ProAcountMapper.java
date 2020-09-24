package com.scsoft.xmgl.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.scpt.annotation.MyBatisDao;
import com.scsoft.xmgl.work.entity.ProAcount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao
public interface ProAcountMapper extends BaseMapper<ProAcount> {
    ProAcount getByAcountId(int acountId);
    int add(@Param("proId") int proId, @Param("acountId") int acountId);
}
