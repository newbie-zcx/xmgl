package com.scsoft.xmgl.info.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scsoft.scpt.base.enitity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@TableName("task_summary")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TaskSummary extends BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     *用户名
     **/
    @TableField("user_name")
    private String userName;
    /**
     *创建时间
     **/
    @TableField("addtime")
    private String addTime;
    /**
     *周报起始日期
     **/
    @TableField("begin_date")
    private String beginDate;
    /**
     *周报结束日期
     **/
    @TableField("end_date")
    private String endDate;
    /**
     *项目名称
     **/
    @TableField("pro_name")
    private String proName;


    /**
     *任务内容
     **/
    @TableField("task")
    private String task;
    /**
    * 任务类型 0:当前  1:计划
    **/
    @TableField("task_type")
    private int taskType;
    /**
     * 阶段
     **/
    @TableField("task_stage")
    private String taskStage;
    /**
     *工时（任务的工时）
     **/
    @TableField("task_hour")
    private Integer taskHour;
}
