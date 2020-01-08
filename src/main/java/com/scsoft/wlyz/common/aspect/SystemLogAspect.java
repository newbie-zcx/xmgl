package com.scsoft.wlyz.common.aspect;

import com.scsoft.wlyz.syslog.entity.SysLog;
import com.scsoft.wlyz.syslog.service.ISysLogService;
import com.scsoft.wlyz.system.entity.User;
import com.scsoft.scpt.utils.WebUtil;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

/**
 * @title: SystemLogAspect
 * @Description: 日志切人点
 * @Author: zhaopengfei
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @CreateDate: 2019/3/15 15:29
 * @Version: 1.0
 */
@Aspect
@Component
public class SystemLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);
    @Autowired
    private ISysLogService logService;

    /**
     * Controller层切点
     *  //第一个*代表所有的返回值类型
     *     //第二个*代表模糊匹配
     *     //第三个*代表所有的类
     *     //第四个*代表类所有方法
     *     //最后一个..代表所有的参数。
     */
//    @Pointcut("execution (* com.scsoft.scpt.*.controller..*.*(..))")
    @Pointcut("@annotation(com.scsoft.scpt.annotation.SysLog)")
    public void controllerAspect() {
    }


    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */

    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user==null){
            user=new User();
            user.setUserName("非注册用户");
        }
        //请求的IP
        String ip= WebUtil.getRemoteAddr(request);
        try {
            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();

            String operationType = "";
            String operationName = "";
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    if(method.isAnnotationPresent(com.scsoft.scpt.annotation.SysLog.class)){
                        Class[] clazzs = method.getParameterTypes();
                        if (clazzs.length == arguments.length) {
                            operationType = method.getAnnotation(com.scsoft.scpt.annotation.SysLog.class).operationType();
                            operationName = method.getAnnotation(com.scsoft.scpt.annotation.SysLog.class).operationName();
                            break;
                        }
                    }else {
                        return;
                    }
                }
            }
            String params = "";
            if (joinPoint.getArgs() !=  null && joinPoint.getArgs().length > 0) {
                for ( int i = 0; i < joinPoint.getArgs().length; i++) {
                    params+=joinPoint.getArgs()[i]+";";
                }
            }

            //*========控制台输出=========*//
            System.out.println("=====controller前置通知开始=====");
            System.out.println("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()")+"."+operationType);
            System.out.println("方法描述:" + operationName);
            System.out.println("请求人:" + user.getUserName());
            System.out.println("请求IP:" + ip);
            String username=user.getUserName();
            SysLog log=new SysLog();
            log.setContent(operationName);
            log.setRequestUri(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()");
            log.setTitle(operationName);
            log.setType(0);
            log.setCreateDate(new Date());
            log.setRemoteAddr(ip);
            log.setParams(params);
            log.setUserName(username);
            log.setMethod(operationType);
            //*========保存数据库日志=========*//
            System.out.println(log);
            logService.save(log);
            //保存数据库
        }  catch (Exception e) {
            //记录本地异常日志
            logger.error("==前置通知异常==");
            logger.error("异常信息:{}", e.getMessage());
        }
    }

    /**
     *
     * @author： zhaopengfei
     * @time：2019-3-31 下午2:24:36
     * @param joinPoint 切点
     * @describtion：异常通知 用于拦截记录异常日志
     */
    @AfterThrowing(pointcut = "controllerAspect()", throwing="e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        //读取session中的用户
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user==null){
            user=new User();
            user.setUserName("非注册用户");
        }
        //请求的IP
        String ip= WebUtil.getRemoteAddr(request);
        String params = "";
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            params= Arrays.toString(joinPoint.getArgs());
        }
        try {
            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            String operationType = "";
            String operationName = "";
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    if(method.isAnnotationPresent(com.scsoft.scpt.annotation.SysLog.class)) {
                        Class[] clazzs = method.getParameterTypes();
                        if (clazzs.length == arguments.length) {
                            operationType = method.getAnnotation(com.scsoft.scpt.annotation.SysLog.class).operationType();
                            operationName = method.getAnnotation(com.scsoft.scpt.annotation.SysLog.class).operationName();
                            break;
                        }
                    }else {
                        break;
                    }
                }
            }
            /*========控制台输出=========*/
            System.out.println("=====异常通知开始=====");
            System.out.println("异常代码:" + e.getClass().getName());
            System.out.println("异常信息:" + e.getMessage());
            System.out.println("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()")+"."+operationType);
            System.out.println("方法描述:" + operationName);
            System.out.println("请求人:" + user.getUserName());
            System.out.println("请求IP:" + ip);
            System.out.println("请求参数:" + params);
            //==========数据库日志=========
            SysLog log=new SysLog();
            log.setContent(operationName);
            log.setRequestUri(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()");
            log.setTitle(operationName);
            log.setType(1);
            log.setCreateDate(new Date());
            log.setRemoteAddr(ip);
            log.setParams(params);
            log.setUserName(user.getUserName());
            log.setMethod(operationType);
            log.setExceptionCode(e.getClass().getName());
            log.setException(e.getMessage());
            //*========保存数据库日志=========*//
            System.out.println(log);
            logService.save(log);
            System.out.println("=====异常通知结束=====");
        } catch (Exception ex) {
            //记录本地异常日志
            logger.error("==异常通知异常==");
            logger.error("异常信息:{}", ex.getMessage());
        }
        //==========记录本地异常日志==========
        logger.error("异常方法:{}异常代码:{}异常信息:{}参数:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage(), params);

    }




}
