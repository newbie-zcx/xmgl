package com.scsoft.wlyz.generator.modle;

/**
 * 代码生成的查询参数
 *
 */
public class GenConfig {


    /**
     * 项目地址
     */
    private String projectPath;

    /**
     * 作者
     */
    private String author;

    /**
     * 项目的包
     */
    private String projectPackage;

    /**
     * 核心模块的包
     */
    private String corePackage;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 忽略的表前缀
     */
    private String ignoreTabelPrefix;

    /**
     * 业务名称
     */
    private String bizName;

    /**
     * 模块名
     */
    private String moduleName;
    
    /**
     * 父级菜单名称
     */
    private String parentMenuName;



    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCorePackage() {
        return corePackage;
    }

    public void setCorePackage(String corePackage) {
        this.corePackage = corePackage;
    }

    public String getProjectPackage() {

        return projectPackage;
    }

    public void setProjectPackage(String projectPackage) {
        this.projectPackage = projectPackage;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIgnoreTabelPrefix() {
        return ignoreTabelPrefix;
    }

    public void setIgnoreTabelPrefix(String ignoreTabelPrefix) {
        this.ignoreTabelPrefix = ignoreTabelPrefix;
    }

    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getParentMenuName() {
        return parentMenuName;
    }

    public void setParentMenuName(String parentMenuName) {
        this.parentMenuName = parentMenuName;
    }


}
