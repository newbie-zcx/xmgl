package com.scsoft.xmgl.info.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.xmgl.info.entity.OutRegister;

import java.util.Map;

public interface OutRegisterMapper extends BaseMapper<OutRegister> {
    boolean updateState(Map<String,Object> map);
}
