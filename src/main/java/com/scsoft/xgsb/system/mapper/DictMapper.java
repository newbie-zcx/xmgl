package com.scsoft.xgsb.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.xgsb.system.entity.Dict;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author zhaopengfei
 * @since 2019-04-26
 */
public interface DictMapper extends BaseMapper<Dict> {
    //根据id查询

    List<Dict>getBygid(Integer gid);
   // Page<Dict> selectMyPage(Page<Dict> page, @Param("ew") QueryWrapper<Dict> wrapper);
//    Dict findById(Integer dictId);
}
