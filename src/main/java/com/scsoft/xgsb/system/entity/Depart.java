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
 * @Description: 机构部门表
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@TableName( "sys_depart")
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Depart extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;
    /**
     * 机构名称
     */
    @TableField("depart_name")
    private String departName;

    /**
     * 父节点
     */
    @TableField( "parent_id")
    private Integer parentId;

    /**
     * 描述
     */
    @TableField( "description")
    private String description;

    /**
     * 父编号列表
     */
    @TableField( "parent_ids")
    private String parentIds;

    /**
     * 机构编码
     */
    @TableField( "org_code")
    private String orgCode;

    /**
     * 机构类型
     */
    @TableField( "org_type")
    private String orgType;

    /**
     * 行政区划代码
     */
    @TableField( "administrative_division")
    private String administrativeDivision;

    /**
     * 地址
     */
    @TableField( "address")
    private String address;

    /**
     * 排序
     */
    @TableField( "depart_order")
    private String departOrder;

    /**
     * 机构级别
     */
    @TableField( "depart_level")
    private String departLevel;
}
