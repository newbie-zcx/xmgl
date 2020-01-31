package com.scsoft.xgsb.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 用户-部门
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@TableName( "sys_user_depart")
@Data
public class UserDepart implements Serializable {
    private static final long serialVersionUID = 1L;
    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户主键
     */
    @TableField( "user_id")
    private Integer userId;

    /**
     * 部门主键
     */
    @TableField( "depart_id")
    private Integer departId;

}
