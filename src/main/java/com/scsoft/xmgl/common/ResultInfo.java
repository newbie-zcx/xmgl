package com.scsoft.xmgl.common;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * Create with IntelliJ IDEA.
 * 接口统一返回类型,所有接口必须返回该类型
 * @author: hezhuangzhuang@cecdat.com
 * Date: 2018/09/26
 * Time: 15:25
 */
@Data
public class ResultInfo<T> implements Serializable{
    private static final long serialVersionUID = -1396532510017715908L;
    private int code = -1; // -1:失败,0成功
    private String message = "";// 如果返回的是字符串 resType str
    private T data;// 如果调用该方法的时候需要返回一个对象 可以塞进去 resType bean

    private ResultInfo() {
        super();
    }


    public ResultInfo(int code, String message, T data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }
    /**
     * 是否成功
     */
    public boolean isSuccess() {
        return code == 0;
    }

    /**
     * 成功返回
     *
     * @param message 成功返回消息
     */
    public static ResultInfo success(String message) {
        ResultInfo rtn = new ResultInfo();
        rtn.code = 0;
        rtn.data = new JSONObject();
        rtn.message = message;
        return rtn;
    }

    /**
     * 成功返回
     *
     * @param data 返回数据
     * @return
     * @version v0.0.1
     */
    public static ResultInfo success(Object data) {
        ResultInfo rtn = new ResultInfo();
        rtn.code = 0;
        rtn.data = data;
        return rtn;
    }

    /**
     * 成功返回
     *
     * @param message 成功返回消息
     * @param data    返回数据
     * @return
     * @version v0.0.1
     */
    public static ResultInfo success(String message, Object data) {
        ResultInfo rtn = new ResultInfo();
        rtn.code = 0;
        rtn.data = data;
        rtn.message = message;
        return rtn;
    }



    /**
     * 失败返回
     *
     * @param message 失败信息
     * @param data    返回数据
     * @version v0.0.1
     */
    public static ResultInfo fail(String message, Object data) {
        ResultInfo rtn = new ResultInfo();
        rtn.code = -1;
        rtn.data = data;
        rtn.message = message;
        return rtn;
    }

    /**
     * 失败返回
     *
     * @param code    返回code
     * @param message 返回信息
     * @param data    返回数据
     * @return
     */
    public static ResultInfo fail(int code, String message, Object data) {
        ResultInfo rtn = new ResultInfo();
        rtn.code = code;
        rtn.data = data;
        rtn.message = message;
        return rtn;
    }

}