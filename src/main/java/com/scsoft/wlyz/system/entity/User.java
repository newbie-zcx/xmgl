package com.scsoft.wlyz.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scsoft.scpt.base.enitity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @Description: 系统用户表
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@TableName( "sys_user")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 是否锁定（1：锁定；-1：删除；0：正常；）
     */
    public static final int STATUS_DELETE = -1;
    public static final int STATUS_LOCKED = 1;
    public static final int STATUS_NORMAL = 0;
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 盐值
     */
    @TableField("salt")
    private String salt;
    /**
     * 姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 性别
     */
    @TableField("sex")
    private String sex;

    /**
     * 出生日期
     */
    @TableField("birth_date")
    private Date birthDate;

    /**
     * 手机号
     */
    @TableField("mobile_phone")
    private String mobilePhone;

    /**
     * 地址
     */
    @TableField("address")
    private String address;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 系统状态
     */
    @TableField("status")
    private Integer status;

    @TableField("user_type")
    private Integer userType;

    /**
     * 角色列表
     */
    @TableField(exist = false)
    private List<Role> roles;
   // user_type

}
