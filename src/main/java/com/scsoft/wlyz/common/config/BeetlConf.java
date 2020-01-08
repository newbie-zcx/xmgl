package com.scsoft.wlyz.common.config;

import com.scsoft.wlyz.common.shiro.ShiroExt;
import org.beetl.core.GroupTemplate;
import org.beetl.core.TagFactory;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: BeetlConf
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 10:12
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@Configuration
public class BeetlConf {
    @Value("${spring.mvc.view.prefix}")
    String templatesPath;//模板根目录 ，比如 "templates"
    /**
     * beetl的配置
     */
    @Bean(initMethod = "init",name = "beetlConfig")
    public BeetlGroupUtilConfiguration getBeetlGroupUtilConfiguration(@Qualifier("tagFactorys") Map<String, TagFactory> tagFactorys) {
        BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();
        //获取Spring Boot 的ClassLoader
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if(loader==null){
            loader = BeetlConf.class.getClassLoader();
        }
//        beetlGroupUtilConfiguration.setConfigProperties(extProperties);//额外的配置，可以覆盖默认配置，一般不需要
        ClasspathResourceLoader cploder = new ClasspathResourceLoader(loader,
                templatesPath);
        beetlGroupUtilConfiguration.setResourceLoader(cploder);
        beetlGroupUtilConfiguration.init();
        //如果使用了优化编译器，涉及到字节码操作，需要添加ClassLoader
//        beetlGroupUtilConfiguration.getGroupTemplate().setClassLoader(loader);
        beetlGroupUtilConfiguration.setTagFactorys(tagFactorys);
        //设置Springboot的shiro标签
        GroupTemplate groupTemplate = beetlGroupUtilConfiguration.getGroupTemplate();
//        groupTemplate.registerFunctionPackage("shiro", new ShiroExt());
        Map<String, Object> functionPackages = new HashMap<String, Object>();
        functionPackages.put("shiro", new ShiroExt());
        beetlGroupUtilConfiguration.setFunctionPackages(functionPackages);

//        groupTemplate.registerTag("dict", DictTag.class);
        groupTemplate.setClassLoader(loader);
        return beetlGroupUtilConfiguration;

    }




    @Bean(name = "beetlViewResolver")
    public BeetlSpringViewResolver getBeetlSpringViewResolver(@Qualifier("beetlConfig") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
        BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
//        beetlSpringViewResolver.setPrefix("WEB-INF/views/");
        beetlSpringViewResolver.setSuffix(".html");
        beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
        beetlSpringViewResolver.setOrder(0);
        beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
        return beetlSpringViewResolver;
    }

}
