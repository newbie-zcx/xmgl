package com.scsoft.xmgl.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 角色-菜单
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@TableName("sys_role_authority")
@Data
public class RoleAuthority implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 菜单编号
     */
    @TableField("authority_id")
    private Integer authorityId;
    /**
     * 角色编号
     */
    @TableField("role_id")
    private Integer roleId;

}
