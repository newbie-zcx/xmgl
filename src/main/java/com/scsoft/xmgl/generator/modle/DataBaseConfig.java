package com.scsoft.xmgl.generator.modle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 数据库相关设置
 * spring:
 *   datasource
 */
@Component
public class DataBaseConfig {
    /**
     * driver-class
     */
    public static String driverClassName;

    /**
     * 数据库url
     */
    public static String url;

    /**
     * 数据库用户名
     */
    public static String username;

    /**
     * 数据库密码
     */
    public static String password;

    public static String getDriverClassName() {
        return driverClassName;
    }
    @Value("${spring.datasource.driver-class-name}")
    public void setDriver(String driver) {
        DataBaseConfig.driverClassName = driver;
    }

    public static String getUrl() {
        return url;
    }
    @Value("${spring.datasource.url}")
    public void setUrl(String url) {
        DataBaseConfig.url = url;
    }

    public static String getUsername() {
        return username;
    }
    @Value("${spring.datasource.username}")
    public void setUsername(String username) {
        DataBaseConfig.username = username;
    }

    public static String getPassword() {
        return password;
    }
    @Value("${spring.datasource.password}")
    public  void setPassword(String password) {
        DataBaseConfig.password = password;
    }
}