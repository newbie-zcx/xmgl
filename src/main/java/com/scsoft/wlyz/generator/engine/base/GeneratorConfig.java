package com.scsoft.xgsb.generator.engine.base;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.scsoft.xgsb.generator.modle.DataBaseConfig;
import com.scsoft.xgsb.generator.modle.DbTypeDriver;
import com.scsoft.xgsb.generator.modle.GenConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.io.File;

/**
 * 默认的代码生成的配置
 *
 */
public class GeneratorConfig extends AbstractGeneratorConfig {
    private GenConfig genConfig;
    @Autowired
    DataSourceProperties dataSourceProperties;
    public GeneratorConfig(GenConfig genConfig) {
        this.genConfig = genConfig;
    }

    @Override
    protected void config() {
        dataSourceConfig();
        globalConfig();
        strategyConfig();
        packageConfig();
        templateConfig();
    }
    /**
     * 全局配置
     */
    protected void globalConfig() {
        String projectPath =genConfig.getProjectPath();
        globalConfig.setOutputDir(projectPath + File.separator + "src" + File.separator + "main" + File.separator + "java");
        globalConfig.setFileOverride(true);
        globalConfig.setEnableCache(false);
        globalConfig.setSwagger2(false);
        globalConfig.setBaseResultMap(true);
        globalConfig.setBaseColumnList(true);
        globalConfig.setOpen(false);
        globalConfig.setAuthor(genConfig.getAuthor());
//        globalConfig.setServiceName("I%sService");
//        contextConfig.setProPackage(genConfig.getProjectPackage());
//        contextConfig.setCoreBasePackage(genConfig.getCorePackage());
    }
    /**
     * 数据库配置
     */
    protected void dataSourceConfig() {
        String dbType= DbTypeDriver.getDbByDriver(DataBaseConfig.driverClassName);
        dataSourceConfig.setDbType(DbType.getDbType(dbType));
        dataSourceConfig.setDriverName(DataBaseConfig.driverClassName);
        dataSourceConfig.setUsername(DataBaseConfig.username);
        dataSourceConfig.setPassword(DataBaseConfig.password);
        dataSourceConfig.setUrl(DataBaseConfig.url);
    }
    /**
     * 生成策略
     */
    protected void strategyConfig() {
        if (genConfig.getIgnoreTabelPrefix() != null) {
            strategyConfig.setTablePrefix(new String[]{genConfig.getIgnoreTabelPrefix()});
        }
        String tableNames=genConfig.getTableName().substring(1,genConfig.getTableName().length());
        System.out.println("======生成表为==========="+tableNames);
        strategyConfig.setInclude(tableNames.split(","));
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);

        strategyConfig.setSuperEntityClass("com.scsoft.scpt.base.enitity.BaseEntity");
        strategyConfig.setSuperControllerClass("com.scsoft.scpt.base.controller.BaseController");
        strategyConfig.setSuperEntityColumns(new String[]{"create_id","create_name","create_date","update_id","update_name","update_date","remark","is_del"});
        strategyConfig.setEntityLombokModel(true);
        strategyConfig.setEntityTableFieldAnnotationEnable(true);
        strategyConfig.setControllerMappingHyphenStyle(true);
        strategyConfig.setEntityTableFieldAnnotationEnable(true);
    }
    /**
     * 包名配置
     */
    protected void packageConfig() {
        packageConfig.setParent(genConfig.getProjectPackage());
        packageConfig.setModuleName(genConfig.getModuleName());
        packageConfig.setController("controller");
        packageConfig.setEntity("entity");
        packageConfig.setService("service");
        packageConfig.setServiceImpl("service.impl");
        packageConfig.setMapper("mapper");
    }


    protected void templateConfig() {
        templateConfig.setService("/templates/service.java");
        templateConfig.setController("/templates/controller.java");
        templateConfig.setServiceImpl("/templates/serviceImpl.java");
        templateConfig.setXml(null);
    }



}
