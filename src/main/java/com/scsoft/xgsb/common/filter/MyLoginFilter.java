package com.scsoft.xgsb.common.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;

/**
 * 自定义shiro过滤器
 * @Author: zhaopengfei
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @CreateDate: 2019/3/15 15:29
 * @Version: 1.0
 */
public class MyLoginFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            if (isAjax((HttpServletRequest) servletRequest)) {
                servletResponse.setContentType("application/json;charset=UTF-8");
                PrintWriter out = servletResponse.getWriter();
                out.write("{\"msg\":\"登录过期，请重新登录\",\"code\":401}");
                out.flush();
                return false;
            }
        }
        //如果 isAuthenticated 为 false 证明不是登录过的，同时 isRememberd 为true 证明是没登陆直接通过记住我功能进来的
     /*   if(!subject.isAuthenticated() && subject.isRemembered()) {
            //获取session看看是不是空的
            Session session = subject.getSession(true);
            session.touch();
            User user = (User) subject.getPrincipal();
            if (null!=user){
                ByteSource credentialsSalt = ByteSource.Util.bytes(user.getUserName()+user.getSalt());
                SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), credentialsSalt, getName());
                System.out.println(authenticationInfo.getCredentials());
//                subject.login(authenticationInfo);
            }
            //随便拿session的一个属性来看session当前是否是空的，
            if (session.getAttribute("userName") == null) {
                //如果是空的才初始化，否则每次都要初始化，项目得慢死
                //这边根据前面的前提假设，拿到的是username
                String username = subject.getPrincipal().toString();

            }

        }*/




//        System.out.println(subject.getSession().getId()+"==="+subject.getSession().getTimeout()+"===="+ DateUtil.format(subject.getSession().getLastAccessTime()));
//        return subject.isAuthenticated() || subject.isRemembered();
        return true;
    }

    /**
     * 判断是不是ajax请求
     */
    private boolean isAjax(HttpServletRequest request) {
        String xHeader = request.getHeader("X-Requested-With");
        if (xHeader != null && xHeader.contains("XMLHttpRequest")) {
            return true;
        }
        return false;
    }

}
