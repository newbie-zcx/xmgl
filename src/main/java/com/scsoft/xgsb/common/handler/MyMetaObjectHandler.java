package com.scsoft.xgsb.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.scsoft.xgsb.common.redis.RedisUtil;
import com.scsoft.xgsb.system.entity.User;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Date;


/**
 * @Description: 注入公共字段自动填充,任选注入方式即可
 * @Author: zhaopengfei
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @CreateDate: 2019/3/15 15:29
 * @Version: 1.0
 */
public class MyMetaObjectHandler implements MetaObjectHandler {
    protected final static Logger logger = LoggerFactory.getLogger(MyMetaObjectHandler.class);
    @Resource
    private RedisUtil redisUtil;
    @Override
    public void insertFill(MetaObject metaObject) {
        logger.info("新增的时候做操作");
        // 更多查看源码测试用例
        System.out.println("*************************");
        System.out.println("insert fill");
        System.out.println("*************************");
        //获取当前登录用户
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (null!=user){
            setFieldValByName("createId", user.getId(), metaObject);
            setFieldValByName("createName", user.getRealName(), metaObject);
            setFieldValByName("createDate", new Date(), metaObject);
            setFieldValByName("isDel", 0, metaObject);
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        logger.info("更新的时候干点不可描述的事情");
        //更新填充
        System.out.println("*************************");
        System.out.println("update fill");
        System.out.println("*************************");
        //mybatis-plus版本2.0.9+
        //获取当前登录用户
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (null!=user){
        setFieldValByName("updateId", user.getId(), metaObject);
        setFieldValByName("updateName", user.getRealName(), metaObject);
        setFieldValByName("updateDate", new Date(), metaObject);
        }
    }
}
