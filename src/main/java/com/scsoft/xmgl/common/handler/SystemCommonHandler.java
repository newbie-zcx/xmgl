package com.scsoft.xmgl.common.handler;

import com.scsoft.xmgl.system.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Description: 公共handler
 * @Author: zhaopengfei
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @CreateDate: 2019/3/18 9:25
 * @Version: 1.0
 */
public class SystemCommonHandler {
    public static final String LOGIN_CURRENTUSER = "current:";
    public static final String SYSTEM_DICT = "system:dict:";
    public static final String FILE_VIEW = "/system/file/view?fileId=";
    public static final String DOWNLOAD_FILE = "/system/file/downloadFile/";
    @Value("${xmgl.version}")
    public static final String version="v1.0";
    public static final String NEW_PATH = "/api/"+version+"/dynamic/newView?editorId=";
    public static Subject getSubject()
    {
        return SecurityUtils.getSubject();
    }
    public static Session getSession()
    {
        return SecurityUtils.getSubject().getSession();
    }
    public static void logout()
    {
        getSubject().logout();
    }

    public static String RONGYUN_TOKEN = "http://api-cn.ronghub.com/user/getToken.json";

    /**
     * 获取当前登录的user
     */
    public static User getLoginUser() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            User user = (User) subject.getPrincipal();
          /*  if (object != null) {
                return (User) object;
            }*/
            return user;
        }
        return null;
    }

    /**
     * 获取当前登录的userId
     */
    public static int getLoginUserId() {
        User loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getId();
    }

    /**
     * 获取当前登录的username
     */
    public static String getLoginUserName() {
        User loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getUserName();
    }
    public static String getIp()
    {
        return getSubject().getSession().getHost();
    }

    public static String getSessionId()
    {
        return String.valueOf(getSubject().getSession().getId());
    }
}
