package com.scsoft.wlyz.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scsoft.scpt.base.enitity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *人员属性定义表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_user_filed")
public class UserFiled extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "属性标识")
    @TableField("filed_name")
    private String filedName;

    @ApiModelProperty(value = "显示名称")
    @TableField("display_name")
    private String displayName;

    @ApiModelProperty(value = "显示排序")
    @TableField("display_order")
    private String displayOrder;
    //'安全级别，0：不保护，1-当前单位,
    // 2：只允许上级部门访问，3-允许上级部门及本部门访问，4-允许本部门访问，5-允许本部门及下级部门访问'
    @ApiModelProperty(value = "安全级别")
    @TableField("safe_level")
    private String safeLevel;

    //'显示级别 0不显示，1、仅平台显示 2、平台和手机端都显示'
    @ApiModelProperty(value = "显示排序")
    @TableField("display_lavel")
    private String displayLavel;

//是否通过启用用户组保护，0：不启用，1-启用
    @ApiModelProperty(value = "是否通过启用用户组保护")
    @TableField("usergroup_protect")
    private String usergroupProtect;

    //安全模式,0-表示完全隐藏，后续再定义特殊表达式来规定哪几位需要用什么特殊符号来代替，
    // 例如：（1）前4位用"*"代替；（2）第5-10位用"#"代替；（3）最后3位用"*"代替
    @ApiModelProperty(value = "安全模式")
    @TableField("protect_mode")
    private String protectMode;

    //is_edit`  '能否编辑，0-不能编辑，1-可编辑',
    //  `action_type`  '操作类型，0-无操作，1-点击跳转',
    //  `action_define`   '操作定义地址',

    @ApiModelProperty(value = "能否编辑")
    @TableField("is_edit")
    private String isEdit;

    @ApiModelProperty(value = "操作类型")
    @TableField("action_type")
    private String actionType;

    @ApiModelProperty(value = "操作定义地址")
    @TableField("action_define")
    private String actionDefine;





}
