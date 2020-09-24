package com.scsoft.xmgl.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.scpt.annotation.MyBatisDao;
import com.scsoft.xmgl.work.entity.ProUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao
public interface ProUserMapper extends BaseMapper<ProUser> {
    int IsExit(int proId);
    List<ProUser> selectByProIds(@Param("proIds") List<Integer> proIds);
    int addMember(@Param("proId") int proId, @Param("userIdList") List<Integer> userIdList);
    List<ProUser> selectByProId(int proId);
    boolean deleteByProId(int proId);
    List<ProUser> selectByUserId(int userId);
}
