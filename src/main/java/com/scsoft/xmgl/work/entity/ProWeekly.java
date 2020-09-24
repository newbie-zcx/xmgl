package com.scsoft.xmgl.work.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
* 项目-周报
**/
@TableName("work_pro_weekly")
@Data
public class ProWeekly implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
    * id
    **/
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
    * 项目主键id
    **/
    @TableField("pro_id")
    private Integer proId;
    /**
     * 周报主键id
     **/
    @TableField("weekly_id")
    private Integer weeklyId;
}
