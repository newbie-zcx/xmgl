package com.scsoft.wlyz.common.filter;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scsoft.wlyz.common.utils.TokenUtil;
import com.scsoft.scpt.utils.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends AuthenticatingFilter{
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";
    @Autowired
    private TokenUtil tokenUtil;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httprequest=(HttpServletRequest)request;
        String url=httprequest.getRequestURI();
        //登陆请求直接放行
        if(url.indexOf("login")>0){
            return true;
        }
        //  拒绝，统一交给 onAccessDenied 处理
        return false ;
    }

    @Override
    @ResponseBody
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // 先从Header里面获取
        String token = httpRequest.getHeader(TokenUtil.TOKEN);
        if(StringUtils.isBlank(token)){
            // 获取不到再从Parameter中拿
            token = httpRequest.getParameter(TokenUtil.TOKEN);
        }
        if (token == null || !token.startsWith("Bearer")) {
            JSONObject o = new JSONObject();
            o.put("code", "402");
            o.put("message","token无效，请重新登陆");
            WebUtil.write(httpResponse,o);
            return false;
        }else {
            if(tokenUtil.validateToken(token)){
                return true;
            }else if(tokenUtil.valiRefreshToken(token)){
                //刷新token
                String newToken=tokenUtil.refreshToken(token);
//                WebUtil.write(httpResponse,ResJson.ok(201,"刷新token",newToken));
                JSONObject o = new JSONObject();
                o.put("code", "201");
                o.put("message","刷新token");
                o.put("data",newToken);
                WebUtil.write(httpResponse,o);
                return false;
            }else{
                //重新登陆
//                throw new BusinessException(402,"token过期，请重新登陆");
                JSONObject o = new JSONObject();
                o.put("code", "402");
                o.put("message","token过期,重新登陆");
                WebUtil.write(httpResponse,o);
//                httpResponse.getWriter().write(ResJson.ok(402,"token过期,重新登陆",null).toString());
//                throw new BusinessException(402,"token过期，请重新登陆");
                return false;
            }
        }
    }


    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return null;
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
                                     ServletResponse response) throws Exception {
        return true;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request,
                                     ServletResponse response) {
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", HttpServletResponse.SC_UNAUTHORIZED);
        jsonObject.put("msg","登录认证失败，无权访问");
        jsonObject.put("timestamp", System.currentTimeMillis());
        try {
            servletResponse.setCharacterEncoding("UTF-8");
            servletResponse.setContentType("application/json;charset=UTF-8");
            servletResponse.setHeader("Access-Control-Allow-Origin","*");
            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(jsonObject));
        } catch (IOException e) {
        }
        return false;
    }
}
