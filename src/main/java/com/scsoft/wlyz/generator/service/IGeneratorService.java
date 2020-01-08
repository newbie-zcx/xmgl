package com.scsoft.wlyz.generator.service;

import com.scsoft.wlyz.generator.entity.TableInfo;
import com.scsoft.wlyz.generator.utils.Query;
import com.scsoft.scpt.common.PageResult;

/**
 * <p>
 * 代码生成接口
 * </p>
 * @author zhaopengfei
 * @CreateDate 2019-04-26
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @Version: 1.0
 */
public interface IGeneratorService{
     /**
      * 查询数据库表信息
      * @return 数据库表列表
      */
     public PageResult<TableInfo> queryList(Query query);
     /**
      * 根据表名称查询信息
      *
      * @param tableName 表名称
      * @return 表信息
      */
     public TableInfo queryTable(String tableName);

     /**
      * 批量生成代码
      * @param tableNames 表数组
      * @return 数据
      */
     public byte[] generatorCode(String[] tableNames);
}
