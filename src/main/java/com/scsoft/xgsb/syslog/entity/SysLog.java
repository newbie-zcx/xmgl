package com.scsoft.xgsb.syslog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scsoft.scpt.base.enitity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 系统日志表
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@TableName( "sys_log")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysLog extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;
    /** 用户代理 */
    @TableField( "user_name")
    private String userName;
    /** 请求URI */
    @TableField( "request_uri")
    private String requestUri;
    /** 日志标题 */
    @TableField( "title")
    private String title;
    /** 日志类型  0操作日志  1、异常日志 */
    @TableField( "type")
    private Integer type;
    /** 操作方式 */
    @TableField( "method")
    private String method;
    /** 异常信息 code*/
    @TableField( "exception_code")
    private String exceptionCode;
    /** 异常信息 */
    @TableField( "exception")
    private String exception;
    /** 日志内容 */
    @TableField( "content")
    private String content;

    /** 操作提交的数据 */
    @TableField( "params")
    private String params;
    /** 操作IP地址 */
    @TableField( "remote_addr")
    private String remoteAddr;

    /** 日志级别 */
    @TableField( "log_level")
    private Integer logLevel;

}
