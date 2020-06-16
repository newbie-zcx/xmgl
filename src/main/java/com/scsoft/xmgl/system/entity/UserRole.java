package com.scsoft.xmgl.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * @Description: 用户-角色
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 *
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@TableName("sys_user_role")
@Data
public class UserRole  implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 编号 */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户编号
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 角色编号
     */
    @TableField("role_id")
    private Integer roleId;
    
    @TableField(exist = true)
    private String roleName;  // 角色名称
}
