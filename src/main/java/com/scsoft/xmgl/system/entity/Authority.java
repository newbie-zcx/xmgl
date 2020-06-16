package com.scsoft.xmgl.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scsoft.scpt.base.enitity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
* @Description: 数据菜单权限表
* @author: Zhaopengfei
* @copyright: 雪城软件有限公司
* @CreatedDate: 2019年03月15日 15:22
* @Copyright: All rights Reserved，Designed By Scsoft
*/
@TableName("sys_authority")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Authority extends BaseEntity {

   private static final long serialVersionUID = 1L;
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;
   /**
    * 菜单地址打开方式
    */
   @TableField( "iframe")
   private String iframe;

   /**
    * 菜单等级
    */
   @TableField( "level")
   private String level;

   /**
    * 资源路径
    */
   @TableField( "name")
   private String name;

   /**
    * 资源类型
    */
   @TableField( "type")
   private Integer type;

   /**
    * 点击后前往的地址
    */
   @TableField( "url")
   private String url;

   /**
    * 父编号
    */
   @TableField( "parent_id")
   private Integer parentId;

   /**
    * 父编号列表
    */
   @TableField( "parent_ids")
   private String parentIds;

   /**
    * 权限字符串
    * 权限标识（如果为空不会添加在shiro的权限列表中）
    */
   @TableField( "permission")
   private String permission;

   /**
    * 是否显示
    */
   @TableField( "isshow")
   private Integer isshow;

   /**
    * 排序
    */
   @TableField( "sort")
   private Integer sort;

   /**
    * 图标
    */
   @TableField( "menu_icon")
   private String menuIcon;

   /**
    * 桌面图标ID
    */
   @TableField( "desk_iconid")
   private String deskIconid;

   @TableField(exist=false)
   private List<Authority> children=new ArrayList<Authority>();

   public void addChild(Authority sysMenu){
      children.add(sysMenu);
   }
}
