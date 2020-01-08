package com.scsoft.wlyz.common.utils;

import com.scsoft.wlyz.common.redis.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @title: TokenUtil
 * @Description:
 * @Author: zhaopengfei
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @CreateDate: 2019/5/20 15:57
 * @Version: 1.0
 * <p>jwt token工具类</p>
 * <pre>
 *     jwt的claim里一般包含以下几种数据:
 *         1. iss -- token的发行者
 *         2. sub -- 该JWT所面向的用户
 *         3. aud -- 接收该JWT的一方
 *         4. exp -- token的失效时间
 *         5. nbf -- 在此时间段之前,不会被处理
 *         6. iat -- jwt发布时间
 *         7. jti -- jwt唯一标识,防止重复使用
 * </pre>
 */
@Component
public class TokenUtil {
    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_CREATED = "creattime";
    static final String CLAIM_KEY_SECRET = "appSecret";
    public static final String REDIS_TOKEN = "token:";
    public static final String TOKEN = "Authorization";
    @Value("${jwt.token.secret}")
    private String secret;
    @Value("${jwt.token.expiration}")
    private Long expiration;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取jwt的payload部分
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }/*catch (ExpiredJwtException ex){//token过期异常 token已经失效需要重新登录
//            throw new BusinessException(HttpStatus.TOKEN_EXP);
        }catch (SignatureException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e){//不支持的token
//            throw new IException(RoleException.MSG_TOKEN_ERROR);
        }catch (Exception e){
//            log.error("验证token时出现未知错误: " + CommonUtil.getDetailExceptionMsg(e));
//            throw new IException(RoleException.MSG_UNKNOWN_ERROR);
        }*/
        return claims;
    }


    /**
     * 生成token
     * @param username 用户名
     * @return
     */
    public String generateToken(String username,String salt) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, username);
        claims.put(CLAIM_KEY_SECRET, salt);
        claims.put(CLAIM_KEY_CREATED, new Date());
        String token="Bearer "+generateToken(claims);
        redisUtil.set(REDIS_TOKEN+username,token,getExpRedisTime());
        return token;
    }

    private String generateToken(Map<String, Object> claims) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, this.secret)
                .compact();
    }

    /**
     * 生成tokeng过期时间 = 当前时间 + expiration（properties中配置的失效时间）
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }
    /**
     * 生成redis tokeng过期时间 = 过期时间的两倍
     * @return
     */
    private Long getExpRedisTime() {
        return System.currentTimeMillis() + expiration * 1000*2;
    }

    /**
     * 根据token获取用户名
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
            String username;
            try {
                final Claims claims = getClaimsFromToken(token);
                username = claims.getSubject();
            } catch (Exception e) {
                username = null;
            }
            return username;
    }

    /**
     * 判断token失效时间是否到了
     * @param token
     * @return
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 获取设置的token失效时间
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

     /**
      * Token失效校验
      * @param token token字符串
      * @return
      */
     public Boolean validateToken(String token) {
         final String username = getUsernameFromToken(token);
         return redisUtil.hasKey(REDIS_TOKEN+username)&&!isTokenExpired(token);
     }
    /**
     * Token过期校验
     * @param token token字符串
     * @return
     */
    public Boolean valiRefreshToken(String token) {
        final String username = getUsernameFromToken(token);
        return redisUtil.hasKey(REDIS_TOKEN+username)&&isTokenExpired(token);
    }
    public String refreshToken(String token) {
        String username = getUsernameFromToken(token);
        final Claims claims = this.getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        String newtoken=generateToken(claims);
        redisUtil.set(REDIS_TOKEN+username,newtoken,getExpRedisTime());
        return newtoken;
    }



    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }
}
