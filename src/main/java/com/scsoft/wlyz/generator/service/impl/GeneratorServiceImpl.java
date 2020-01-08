package com.scsoft.wlyz.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IOUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.scsoft.wlyz.generator.entity.TableInfo;
import com.scsoft.wlyz.generator.mapper.GeneratorMapper;
import com.scsoft.wlyz.generator.service.IGeneratorService;
import com.scsoft.wlyz.generator.utils.Query;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.scpt.utils.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * 代码生成服务实现类
 * @author zhaopengfei
 * @CreateDate 2019-04-26
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @Version: 1.0
 */
@Service
@Transactional
public class GeneratorServiceImpl implements IGeneratorService {
    @Autowired
    private GeneratorMapper genMapper;
    /**
     * 查询数据库表信息
     * @return 数据库表列表
     */
    @Override
    public PageResult<TableInfo> queryList(Query query) {
        QueryWrapper<TableInfo> warper = Wrappers.<TableInfo>query();
        if (query.containsKey("tableName")&& StringUtils.isNotBlank(query.get("tableName").toString())){
            warper.eq("table_name",query.get("tableName").toString());
        }
        if (query.containsKey("comments")&& StringUtils.isNotBlank(query.get("comments").toString())){
            warper.eq("table_comment",query.get("comments").toString());
        }
        List<TableInfo> pages = genMapper.queryList(new RowBounds(query.getOffset(),query.getLimit()), warper);
        int total=genMapper.queryList(warper).size();
        PageResult reslt=new PageResult();
        reslt.setCount(total);
        reslt.setData(pages);
        return reslt;
    }
    @Override
    public TableInfo queryTable(String tableName) {
        QueryWrapper<TableInfo> warper = Wrappers.<TableInfo>query();
        return genMapper.queryTable(warper.eq("table_name",tableName));
    }

    @Override
    public byte[] generatorCode(String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for(String tableName : tableNames){
            //查询表信息
//            Map<String, String> table = queryTable(tableName);
            //查询列信息
//            List<Map<String, String>> columns = queryColumns(tableName);
            //生成代码
//            GenUtils.generatorCode(table, columns, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 生成代码
     */
   /* public void generatorCode(TableInfo table, List<ColumnInfo> columns, ZipOutputStream zip)
    {
        // 表名转换成Java属性名
        String className = GenUtils.tableToJava(table.getTableName());
        table.setClassName(className);
        table.setClassname(StringUtils.uncapitalize(className));
        // 列信息
        table.setColumns(GenUtils.transColums(columns));
        // 设置主键
        table.setPrimaryKey(table.getColumnsLast());

        VelocityInitializer.initVelocity();

        String packageName = GenConfig.getPackageName();
        String moduleName = GenUtils.getModuleName(packageName);

        VelocityContext context = GenUtils.getVelocityContext(table);

        // 获取模板列表
        List<String> templates = GenUtils.getTemplates();
        for (String template : templates)
        {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            try
            {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(GenUtils.getFileName(template, table, moduleName)));
                IOUtils.write(sw.toString(), zip, Constants.UTF8);
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            }
            catch (IOException e)
            {
                log.error("渲染模板失败，表名：" + table.getTableName(), e);
            }
        }
    }*/

}
