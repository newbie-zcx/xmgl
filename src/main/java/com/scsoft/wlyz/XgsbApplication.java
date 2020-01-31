package com.scsoft.xgsb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @Description: 启动主方法
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @CreateDate: 2019/3/13 14:15
 * ---------------------------------
 */
@SpringBootApplication
@CrossOrigin
public class XgsbApplication extends SpringBootServletInitializer {
    private final static Logger logger = LoggerFactory.getLogger(XgsbApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(XgsbApplication.class, args);
        logger.info("============服务启动完成!=============");
    }
   /**
    * 为了打包springboot项目部署tomcat
    */
    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}
