package com.scsoft.xmgl.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * spring session配置
 *
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 10:12
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)  //session过期时间  如果部署多机环境,需要打开注释
@Configuration
@ConditionalOnProperty(prefix = "bigcorn", name = "spring-session-open", havingValue = "true")
public class SpringSessionConfig {

}
