package com.scsoft.xmgl.work.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
* 周报-内容
**/
@TableName("work_weekly_content")
@Data
public class WeeklyContent implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
    * id
    **/
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
    * 周报主键id
    **/
    @TableField("weekly_id")
    private Integer weeklyId;
    /**
    * 周报内容主键id
    **/
    @TableField("content_id")
    private Integer contentId;
}
