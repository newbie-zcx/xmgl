package com.scsoft.xgsb.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scsoft.scpt.base.enitity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 *用户组
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_usergroup")
public class UserGroup extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "用户组编码")
    @TableField("group_code")
    private String groupCode;

    @ApiModelProperty(value = "用户组名称")
    @TableField("group_name")
    private String groupName;

    @ApiModelProperty(value = "用户组描述")
    @TableField("group_desc")
    private String groupDesc;

    /**
     * 1：自定义组；2：机构组；3：机构树组；4：单位组，5：集团组
     */
    @ApiModelProperty(value = "用户组类型")
    @TableField("group_type")
    private String groupType;
    @TableField(exist = false)
    private String groupTypeStr;
    public String getGroupTypeStr() {

        if("1".equals(groupType)){
            groupTypeStr="自定义组";
        } else if ("2".equals(groupType)){
            groupTypeStr="机构组";
        } else if ("3".equals(groupType)){
            groupTypeStr="机构树组";
        } else if("4".equals(groupType)){
            groupTypeStr="单位组";
        } else if ("5".equals(groupType)){
            groupTypeStr="集团组";
        } else {
            groupTypeStr=groupType;
        }
        return groupTypeStr;
    }

    public void setGroupTypeStr(String groupTypeStr) {
        this.groupTypeStr = groupTypeStr;
    }


    /**
     *0、不限制类型，1系统平台用户，2手机端用户，3其他扩展用户
     */
    @ApiModelProperty(value = "用户类型")
    @TableField("user_type")
    private String userType;
    @TableField(exist = false)
    private  String userTypeStr;

    public String getUserTypeStr() {
        if("0".equals(userType)){
            userTypeStr="不限制类型";
        } else if ("1".equals(userType)){
            userTypeStr="系统平台用户";
        }else  if ("2".equals(userType)){
            userTypeStr="手机端用户";
        }else if ("3".equals(userType)){
            userTypeStr="其他扩展用户";
        }else {
            userTypeStr=userType;
        }
        return userTypeStr;
    }
  public  String  setUserTypeStr(String userTypeStr){
    return   this.userTypeStr = userTypeStr;
  }

    //0通用1用于门户授权2用于应用授权21用于特定应用授权3用于灰度发布授权31用于特定应用版本灰度授权
    //4EMM管理权限分组；5通讯簿查看权限分组。如果用途的值>0，则只能在特定用途的业务功能中使用。6. 用户推送用户组。
    @ApiModelProperty(value = "用户组用途")
    @TableField("group_purpose")
    private String groupPurpose;

    @TableField(exist = false)
    private  String groupPurposeStr;
    public String getGroupPurposeStr() {

        if("0".equals(groupPurpose)){
            groupPurposeStr="通用";
        } else if ("1".equals(groupPurpose)){
            groupPurposeStr="门户授权";
        } else if ("2".equals(groupPurpose)){
            groupPurposeStr="应用授权";
        } else if("21".equals(groupPurpose)){
            groupPurposeStr="用于特定应用授权";
        } else if ("3".equals(groupPurpose)){
            groupPurposeStr="灰度发布授权";
        } else if ("31".equals(groupPurpose)){
            groupPurposeStr="用于特定应用版本灰度授权";
        } else if ("4".equals(groupPurpose)){
            groupPurposeStr="EMM管理权限分组";
        } else if ("5".equals(groupPurpose)){
            groupPurposeStr="通讯簿查看权限分组";
        }  else if ("6".equals(groupPurpose)){
            groupPurposeStr="用户推送用户组";
        } else {
            groupPurposeStr=groupType;
        }
        return groupPurposeStr;
    }

    public void setGroupPurposeStr(String groupPurposeStr) {
        this.groupPurposeStr = groupPurposeStr;
    }


    //当group_type=2或3时，使用此字段的值来查询机构下属用户。
    @ApiModelProperty(value = "组织机构ID")
    @TableField("org_id")
    private String orgId;

    //当group_type=4时，使用此字段的值来查询单位的下属用户。
    @ApiModelProperty(value = "单位ID")
    @TableField("corp_id")
    private String corpId;

    //当usergroup_type=5时，使用此字段的值来查询集团的下属用户
    @ApiModelProperty(value = "集体单位ID")
    @TableField("group_corp_id")
    private String groupCorpId;

    //状态标识，0-失效，1：有效
    @ApiModelProperty(value = "状态")
    @TableField("status")
    private Integer status;

    @TableField(exist = false)
    private  String statusStr;

    public String getStatusStr() {
        if("0".equals(status)){
            statusStr="失效";
        } else if ("1".equals(status)){
            statusStr="有效";
        } else {
            statusStr=status+"";
        }
        return statusStr;
    }
    public  String  setStatusStr(String statusStr){
        return   this.statusStr = statusStr;
    }
    @TableField(exist = false)
    public  List<Depart> departs;
}
