package com.scsoft.xgsb.yqsb.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scsoft.xgsb.yqsb.entity.Detail;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 疫情上报详情 Mapper 接口
 * </p>
 *
 * @author 赵鹏飞
 * @since 2020-01-31
 */
public interface DetailMapper extends BaseMapper<Detail> {
    IPage<Detail> todayNosb(Page<Detail> page, @Param(Constants.WRAPPER) Wrapper<Detail> wrapper);
}
