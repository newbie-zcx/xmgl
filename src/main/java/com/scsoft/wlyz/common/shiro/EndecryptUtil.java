package com.scsoft.xgsb.common.shiro;

import com.scsoft.scpt.utils.CommonUtil;
import org.apache.commons.codec.binary.Hex;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.security.MessageDigest;

/**
 * 加密解密工具类 (依赖shiro的加密)
 * @Author: zhaopengfei
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @CreateDate: 2019/3/15 15:29
 * @Version: 1.0
 */
public class EndecryptUtil {
    /**
     * 加盐参数
     */
    public final static String hashAlgorithmName = "MD5";

    /**
     * 循环次数
     */
    public final static int hashIterations = 1024;
    /**
     * md5加密(没有盐)
     *
     * @param password 要加密的字符串
     */
    public static String encrytMd5(String password) {
        return new Md5Hash(password).toHex();
    }
    /**
     * shiro密码加密工具类
     *
     * @param credentials 密码
     * @param saltSource 密码盐
     * @return
     */
    public static String md5(String credentials, String saltSource) {
        ByteSource salt = new Md5Hash(saltSource);
        return new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations).toString();
    }
    /**
     * 指定盐salt进行md5加密
     *
     * @param password       要加密的字符串
     * @param salt           盐
     * @param hashIterations 散列次数
     * @return
     */
    public static String encrytMd5(String password, String salt, int hashIterations) {
        String password_cipherText = new Md5Hash(password, salt, hashIterations).toHex();
        return password_cipherText;
    }
    /**
     * 获取随机盐值
     * @param length
     * @return
     */
    public static String getRandomSalt(int length) {
        return CommonUtil.getRandomString(length);
    }

    public static String hexSHA1(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(value.getBytes("utf-8"));
            byte[] digest = md.digest();
            return byteToHexString(digest);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    public static String byteToHexString(byte[] bytes) {
        return String.valueOf(Hex.encodeHex(bytes));
    }


    /**
     * @Comment SHA1实现
     * @Author Ron
     * @Date 2017年9月13日 下午3:30:36
     * @return
     */
    public static String shaEncode(String inStr) throws Exception {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }


}
