package com.scsoft.xgsb.common.aspect;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.scsoft.xgsb.system.entity.Role;
import com.scsoft.xgsb.system.entity.User;
import com.scsoft.scpt.annotation.DataScope;
import com.scsoft.scpt.base.enitity.BaseEntity;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据过滤处理
 * @Description: 日志切人点
 * @Author: zhaopengfei
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @CreateDate: 2019/3/15 15:29
 * @Version: 1.0
 */
@Aspect
@Component
public class DataScopeAspect
{
    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";

    // 配置织入点
    @Pointcut("@annotation(com.scsoft.scpt.annotation.DataScope)")
    public void dataScopePointCut()
    {
    }

    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point) throws Throwable
    {
        handleDataScope(point);
    }

    protected void handleDataScope(final JoinPoint joinPoint)
    {
        // 获得注解
        DataScope controllerDataScope = getAnnotationLog(joinPoint);
        if (controllerDataScope == null)
        {
            return;
        }
        // 获取当前的用户
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (currentUser != null)
        {
            // 如果是超级管理员，则不过滤数据
            if (currentUser.getUserName().equals("admin"))
            {
                dataScopeFilter(joinPoint, currentUser, controllerDataScope.tableAlias());
            }
        }
    }

    /**
     * 数据范围过滤
     * @param joinPoint 切点
     * @param user 用户
     * @param alias 别名
     */
    public static void dataScopeFilter(JoinPoint joinPoint, User user, String alias)
    {
        StringBuilder sqlString = new StringBuilder();

        for (Role role : user.getRoles())
        {
            String dataScope = "1";//role.getDataScope();
            if (DATA_SCOPE_ALL.equals(dataScope))
            {
                sqlString = new StringBuilder();
                break;
            }
            else if (DATA_SCOPE_CUSTOM.equals(dataScope))
            {
                sqlString.append(StringUtils.format(
                        " OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", alias,
                        role.getId()));
            }
        }

        if (org.apache.commons.lang3.StringUtils.isNotBlank(sqlString.toString()))
        {
            BaseEntity baseEntity = (BaseEntity) joinPoint.getArgs()[0];
//            baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private DataScope getAnnotationLog(JoinPoint joinPoint)
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(DataScope.class);
        }
        return null;
    }
}
