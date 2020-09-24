package com.scsoft.xmgl.work.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scsoft.scpt.base.enitity.BaseEntity;
import lombok.Data;


/**
* 更新日志
**/
@TableName("update_log")
@Data
public class UpdateLog extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
    * ID
    **/
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
    * IP
    **/
    @TableField("remote_addr")
    private String remoteAddr;
    /**
     * 请求URI
     **/
    @TableField("request_uri")
    private String requestUri;
    /**
     * 用户代理
     **/
    @TableField("user_name")
    private String userName;
    /**
     * 更新原因
     **/
    @TableField("update_reason")
    private String updateReason;
    /**
     * 更新方法
     **/
    @TableField("update_method")
    private String updateMethod;
    /**
    * 更改的那条数据的id
    **/
    @TableField("entity_id")
    private Integer entityId;
    /**
     * 更新表
     **/
    @TableField("update_table")
    private String updateTable;
    /**
     * 更新列
     **/
    @TableField("update_row")
    private String updateRow;

    /**
     * 请求前数据
     **/
    @TableField("before_params")
    private String beforeParams;
    /**
     * 请求后数据
     **/
    @TableField("after_params")
    private String afterParams;
}
