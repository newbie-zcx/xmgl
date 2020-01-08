package com.scsoft.wlyz.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: sys_contacts_depart表
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年04月29日 13:20
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@TableName( "sys_contacts_depart")
@Data
public class ContactsDepart implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 通讯录id
     */
    @ApiModelProperty(value = "通讯录id")
    @TableField("contacts_id")
    private Integer contactsId;

    /**
     * 部门编号
     */
    @ApiModelProperty(value = "部门编号")
    @TableField("depart_id")
    private Integer departId;

   /* *//**
     * 用户编号
     *//*
    @ApiModelProperty(value = "用户编号")
    @TableField("user_id")
    private String userId;*/

}
