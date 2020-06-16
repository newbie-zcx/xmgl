package com.scsoft.xmgl.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scsoft.scpt.base.enitity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 通讯录管理
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年06月12日
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@TableName( "sys_contacts")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Contacts  extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;
    /**
     * 通讯录编码
     */
    @TableField("contacts_code")
    private String contactsCode;

    /**
     * 通讯录名称
     */
    @TableField( "contacts_name")
    private String contactsName;

    /**
     * 描述
     */
    @TableField( "contacts_desc")
    private String contactsDesc;

    /**
     * 通讯录状态 0显示 1不显示
     */
    @TableField( "contacts_status")
    private String contactsStatus;

    /**
     * 通讯录状态 0显示 1不显示
     */
    @TableField( "portal_id")
    private String PortalId;
}
