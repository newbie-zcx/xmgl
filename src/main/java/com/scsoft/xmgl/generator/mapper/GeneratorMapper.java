package com.scsoft.xmgl.generator.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.scsoft.xmgl.generator.entity.TableInfo;
import com.scsoft.scpt.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * <p>
 * 代码生成器 Mapper 接口
 * </p>
 * @author zhaopengfei
 * @since 2019-04-26
 */
@MyBatisDao
@Mapper
public interface GeneratorMapper extends BaseMapper<TableInfo> {
    /**
     * 查询数据库表信息
     * @return 数据库表列表
     */
    List<TableInfo> queryList(RowBounds rowBounds, @Param(Constants.WRAPPER) Wrapper wrapper);
    List<TableInfo> queryList(@Param(Constants.WRAPPER) Wrapper wrapper);
    TableInfo queryTable(@Param(Constants.WRAPPER) Wrapper wrapper);

//    List<Map<String, String>> queryColumns(String tableName);
}
