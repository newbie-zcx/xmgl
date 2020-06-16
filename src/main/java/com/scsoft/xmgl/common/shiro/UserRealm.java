package com.scsoft.xmgl.common.shiro;

import com.scsoft.xmgl.system.entity.Authority;
import com.scsoft.xmgl.system.entity.Role;
import com.scsoft.xmgl.system.entity.User;
import com.scsoft.xmgl.system.service.IAuthorityService;
import com.scsoft.xmgl.system.service.IRoleService;
import com.scsoft.xmgl.system.service.IUserService;
import com.scsoft.scpt.utils.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description: 自定义realm
 * @Author: zhaopengfei
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @CreateDate: 2019/3/15 15:29
 * @Version: 1.0
 */
public class UserRealm extends AuthorizingRealm {
    private Logger logger = LoggerFactory.getLogger(UserRealm.class);
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;

    @Autowired
    private IAuthorityService authorityService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
//        String username = (String)principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 角色
        List<Role> userRoles = roleService.getByUserId(user.getId());
        Set<String> roles = new HashSet<>();
        for (int i = 0; i < userRoles.size(); i++) {
            roles.add(String.valueOf(userRoles.get(i).getId()));
        }
        authorizationInfo.setRoles(roles);
        // 权限
        List<Authority> authorities = authorityService.listByUserId(user.getId());
        Set<String> permissions = new HashSet<>();
        for (int i = 0; i < authorities.size(); i++) {
            String authority = authorities.get(i).getPermission();
            if (StringUtils.isNotBlank(authority)) {
                permissions.add(authority);
            }
        }
        authorizationInfo.setStringPermissions(permissions);
        System.out.println(permissions);
        return authorizationInfo;

    }
    /**
     * 登录认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        logger.info("验证当前Subject时获取到token为：" + token.toString());
        String username = (String) authenticationToken.getPrincipal();
        User user = userService.getByUsername(username);
        if (user == null) {
            throw new UnknownAccountException(); // 账号不存在
        }
        if (user.getStatus()==1) {
            throw new LockedAccountException();  // 账号被锁定
        }
        if (user.getStatus()==-1) {
            throw new DisabledAccountException();  // 用户禁用
        }

        //单用户登录
        //处理session
    /*    DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager) securityManager.getSessionManager();
        //获取当前已登录的用户session列表
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();
        User temp;
        for(Session session : sessions){
            //清除该用户以前登录时保存的session，强制退出
            Object attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (attribute == null) {
                continue;
            }

            temp = (User) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
            if(username.equals(temp.getUserName())) {
                sessionManager.getSessionDAO().delete(session);
            }
        }*/
        ByteSource credentialsSalt = ByteSource.Util.bytes(user.getUserName()+user.getSalt());
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), credentialsSalt, getName());
        return authenticationInfo;
    }

    /**
     * 清理缓存权限
     */
    public void clearCachedAuthorizationInfo()
    {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
    /*public static void main(String[] args) {
        String hashAlgorithmName = "MD5";//加密方式
        Object crdentials = "123456";//密码原值
        Object salt = "adminscsoft";//盐值
        int hashIterations = 1024;//加密1024次
        Object result = new SimpleHash(hashAlgorithmName,crdentials,salt,hashIterations);
        System.out.println(result);

        System.err.println("经过加密以后的密码是："+result);//00e4595eafdabb56b49cbf810aadd5aa
    }*/
}
