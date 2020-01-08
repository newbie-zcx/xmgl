package com.scsoft.wlyz.generator.engine.base;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成的抽象配置
 */
public abstract class AbstractGeneratorConfig {

    GlobalConfig globalConfig = new GlobalConfig();

    DataSourceConfig dataSourceConfig = new DataSourceConfig();

    StrategyConfig strategyConfig = new StrategyConfig();

    PackageConfig packageConfig = new PackageConfig();

    TemplateConfig templateConfig=new TemplateConfig();

    TableInfo tableInfo = null;

    /**
     * 代码生成器配置
     */

    protected abstract void config();

    public void init() {
        config();
    }
    public AbstractGeneratorConfig() {
    }

    public void doMpGeneration() {
        init();
        AutoGenerator autoGenerator = new AutoGenerator();
        // 全局配置
        autoGenerator.setGlobalConfig(globalConfig);
        // 数据源配置
        autoGenerator.setDataSource(dataSourceConfig);
        // 包配置
        autoGenerator.setPackageInfo(packageConfig);
        // 策略配置
        autoGenerator.setStrategy(strategyConfig);
        // 注入自定义配置，可以在 beetl 中使用 cfg.abc 【可无】  ${cfg.abc}
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return  System.getProperty("user.dir") + "/src/main/resources/mapper/" + packageConfig.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        autoGenerator.setCfg(cfg);

        focList.add(new FileOutConfig("/templates/page.html.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                System.out.println("========生成page_html成功==========文件位置="+ System.getProperty("user.dir")+"/src/main/resources/static/view/module/" + packageConfig.getModuleName() + tableInfo.getEntityPath()+".html");

                // 自定义输入文件名称
                return  System.getProperty("user.dir")+"/src/main/resources/static/view/module/" + packageConfig.getModuleName() + tableInfo.getEntityPath()+".html";
            }
        });
        cfg.setFileOutConfigList(focList);
        autoGenerator.setCfg(cfg);
        // 自定义  xxAdd.html 生成
        focList.add(new FileOutConfig("/templates/page_add.html.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {

                System.out.println("========生成add_html成功==========文件位置="+ System.getProperty("user.dir")+"/src/main/resources/static/view/module/" + packageConfig.getModuleName() + tableInfo.getEntityPath()+"_add.html");
                // 自定义输入文件名称
                return  System.getProperty("user.dir")+"/src/main/resources/static/view/module/" + packageConfig.getModuleName() + tableInfo.getEntityPath()+"_add.html";
            }
        });
        cfg.setFileOutConfigList(focList);
        autoGenerator.setCfg(cfg);
        // 自定义  xxView.html 生成
        focList.add(new FileOutConfig("/templates/page_view.html.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                System.out.println("========生成view_html成功==========文件位置="+ System.getProperty("user.dir")+"/src/main/resources/static/view/module/" + packageConfig.getModuleName() + tableInfo.getEntityPath()+"_view.html");

                return  System.getProperty("user.dir")+"/src/main/resources/static/view/module/" + packageConfig.getModuleName() + tableInfo.getEntityPath()+"_view.html";
            }
        });
        cfg.setFileOutConfigList(focList);
        autoGenerator.setCfg(cfg);
        //  自定义 xxUpdate.html生成
        focList.add(new FileOutConfig("/templates/page_edit.html.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                System.out.println("========生成edit_html成功==========文件位置="+ System.getProperty("user.dir")+"/src/main/resources/static/view/module/" + packageConfig.getModuleName() + tableInfo.getEntityPath()+"_edit.html");
                return  System.getProperty("user.dir")+"/src/main/resources/static/view/module/" + packageConfig.getModuleName() + tableInfo.getEntityPath()+"_edit.html";
            }
        });


        cfg.setFileOutConfigList(focList);
        autoGenerator.setCfg(cfg);

        autoGenerator.setTemplate(templateConfig);

        autoGenerator.setTemplateEngine(new VelocityTemplateEngine());
        autoGenerator.execute();
        //获取table信息,用于代码生成
        List<TableInfo> tableInfoList = autoGenerator.getConfig().getTableInfoList();
        if (tableInfoList != null && tableInfoList.size() > 0) {
            this.tableInfo = tableInfoList.get(0);
        }

    }
}
