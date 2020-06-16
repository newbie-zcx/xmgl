package com.scsoft.xmgl.common.shiro;

import com.scsoft.xmgl.common.redis.RedisUtil;
import com.scsoft.scpt.constant.UserConstants;
import com.scsoft.scpt.utils.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @title: RetryLimitCredentialsMatcher
 * @Description: 验证器，增加了登录次数校验功能
 * @Author: zhaopengfei
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @CreateDate: 2019/3/20 10:52
 * @Version: 1.0
 */
public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher {
    private static final Logger log = LoggerFactory.getLogger(RetryLimitCredentialsMatcher.class);
    @Autowired
    private RedisUtil redisUtil;

    private int maxRetryCount = 5;

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }


    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        //retry count + 1
        AtomicInteger  retryCount = new AtomicInteger(0);
        String value = (String) redisUtil.get(UserConstants.SHIRO_LOGIN_COUNT+username);
        int loginCount= (value!=null)?Integer.valueOf(value):0;
        if (StringUtils.isNotBlank(value)) {
            retryCount = new AtomicInteger(Integer.parseInt(value));
        }

        if(loginCount>maxRetryCount){
            redisUtil.set(UserConstants.SHIRO_IS_LOCK+username, "LOCK");
            redisUtil.expire(UserConstants.SHIRO_IS_LOCK+username, 60);
//            if (retryCount.incrementAndGet() > maxRetryCount) {
                log.warn("用户名: " + username + " t尝试登陆次数大于五次");
                throw new ExcessiveAttemptsException("用户名: " + username + " 错误尝试次数不能大于5次");
//            }
        }
        boolean matches = super.doCredentialsMatch(token, info);//判断用户是否可用，即是否为正确的账号密码
        if (matches) {
            redisUtil.del(UserConstants.SHIRO_LOGIN_COUNT+username);
        }else {
            //存储错误次数到redis中
            redisUtil.set(UserConstants.SHIRO_LOGIN_COUNT+username,retryCount.incrementAndGet() + "",1800);
        }
        return matches;
    }


}
