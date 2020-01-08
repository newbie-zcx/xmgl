package com.scsoft.wlyz.common.exception;

import com.scsoft.wlyz.common.utils.HttpStatus;

/**
 * 自定义异常基类
 * @Author: zhaopengfei
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @CreateDate: 2019/3/15 15:29
 * @Version: 1.0
 */
public abstract class IException extends RuntimeException {
    private static final long serialVersionUID = -1582874427218948396L;
    private Integer code;

    public IException() {
    }
    public IException(HttpStatus status) {
        super(status.msg());
        this.code = status.code();
    }
    public IException(String message) {
        super(message);
    }

    public IException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
