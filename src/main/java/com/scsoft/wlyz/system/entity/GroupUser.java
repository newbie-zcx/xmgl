package com.scsoft.wlyz.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: group_user表
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年04月29日 13:20
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@TableName( "sys_group_user")
@Data
public class GroupUser  implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "用户编号")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "用户组编号")
    @TableField("group_id")
    private String groupId;
}
