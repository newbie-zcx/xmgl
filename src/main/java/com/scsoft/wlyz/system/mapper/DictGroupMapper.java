package com.scsoft.xgsb.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.xgsb.system.entity.DictGroup;

/**
 * <p>
 * 字典分组 Mapper 接口
 * </p>
 *
 * @author zhaopengfei
 * @since 2019-04-26
 */
public interface DictGroupMapper extends BaseMapper<DictGroup> {
    //通过id查找
    DictGroup  findById(Integer dictGroupId);




}

