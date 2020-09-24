package com.scsoft.xmgl.work.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
* 用户-项目
**/
@TableName("work_pro_user")
@Data
public class ProUser implements Serializable {
    private static final long serialVersionUID = 1L;
    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
    * 用户主键
    **/
    @TableField("user_id")
    private Integer userId;
    /**
     * 部门主键
     **/
    @TableField("pro_id")
    private Integer proId;

    @TableField(exist = true)
    private String realName;
}
