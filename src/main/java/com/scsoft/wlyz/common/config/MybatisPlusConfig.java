package com.scsoft.wlyz.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.scsoft.wlyz.common.handler.MyMetaObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MybatisPlus配置
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 10:12
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.scsoft.wlyz.*.mapper")
public class MybatisPlusConfig {
    /**
     * 注入sql注入器
     */
    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;

    }


    /**
     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
     * 性能分析拦截器，不建议生产使用
     */
    @Bean
    @Profile({"dev","test"})// 设置 dev test 环境开启
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        /*<!-- SQL 执行性能分析，开发环境使用，线上不推荐。 maxTime 指的是 sql 最大执行时长 -->*/
        //performanceInterceptor.setMaxTime(1000);
        /*<!--SQL是否格式化 默认false-->*/
        //performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }

    /**
    /**
     * 自动填充
     *
     * @return
     */
    @Bean
    public MetaObjectHandler metaObjectHandler(){
        return new MyMetaObjectHandler();
    }


}