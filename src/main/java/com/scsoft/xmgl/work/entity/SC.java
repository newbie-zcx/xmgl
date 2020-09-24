package com.scsoft.xmgl.work.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scsoft.scpt.base.enitity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
* 周报内容
**/
@TableName("work_sc")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SC extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
    * id
    **/
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
    * 内容
    **/
    @TableField("content")
    private String content;

}
