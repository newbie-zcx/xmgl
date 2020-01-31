package com.scsoft.xgsb.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scsoft.scpt.base.enitity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 角色表
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@TableName( "sys_role")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Role extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 是否管理员（0：是；1：不是；）
     */
    public static final int ROLE_SYS = 0;
    public static final int ROLE_NSYS = 1;
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;
    /**
     * 数字代码
     */
    @TableField( "code")
    private String code;

    /**
     * 角色名称
     */
    @TableField( "name")
    private String name;

    /**
     * 是否管理员
     */
    @TableField( "is_sys")
    private Integer isSys;

    /**
     * 角色描述
     */
    @TableField( "description")
    private String description;
    public Role(int roleId, String roleName) {
        setId(roleId);
        setName(roleName);
    }
    public Role(int roleId) {
        setId(roleId);
    }
    public Role() {
    }
}
