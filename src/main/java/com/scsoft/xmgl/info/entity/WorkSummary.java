package com.scsoft.xmgl.info.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scsoft.scpt.base.enitity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@TableName("work_summary")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class WorkSummary extends BaseEntity {
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
    @TableField("add_time")
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
     *项目类型
     **/
    @TableField("pro_completion")
    private String proCompletion;
    /**
     *项目名称
     **/
    @TableField("pro_name")
    private String proName;


    /**
     *计划完成百分比
     **/
    @TableField("pop_work")
    private String popWork;
    /**
     *完成百分比
     **/
    @TableField("poc_work")
    private String pocWork;
    /**
     *任务类型
     **/
    @TableField("task_type")
    private String taskType;
    /**
     *工时（这周的工时）
     **/
    @TableField("work_hour")
    private int workHour;
    /**
     *原因
     **/
    @TableField("reason")
    private String reason;



    /**
     *下周计划工作百分比
     **/
    @TableField("plan_pop_work")
    private String planPopWork;
    /**
     *任务类型
     **/
    @TableField("plan_task_type")
    private String planTaskType;
    /**
     *工作说明
     **/
    @TableField("job_description")
    private String jobDescription;




    /**
     *项目经理审核状态 0:通过  1:未通过
     **/
    @TableField("examine_status")
    private  Integer examineStatus;
    /**
     *部门经理审核状态 0:通过  1:未通过
     **/
    @TableField("examine_status_dept")
    private  Integer examineStatusDept;
    /**
     *项目经理查看状态 0:通过  1:未查看
     **/
    @TableField("check_status")
    private  Integer checkStatus;
    /**
     *部门经理查看状态 0:通过  1:未查看
     **/
    @TableField("check_status_dept")
    private  Integer checkStatusDept;
    /**
     *评论
     **/
    @TableField("comment")
    private  String comment;

    /**
     * 任务列表
     */
    @TableField(exist = false)
    private List<TaskSummary> taskSummaries;

}
