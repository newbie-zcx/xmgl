package com.scsoft.xmgl.work.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scsoft.scpt.base.enitity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;


/**
* 台账
**/
@TableName("work_acount")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Acount extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * ID
     **/
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
    * 项目ID
    **/
    @TableField("pro_id")
    private Integer proId;
    /**
     * 开始时间
     **/
    @TableField("start_date")
    private String startDate;
    /**
     * 结束时间
     **/
    @TableField("end_date")
    private String endDate;
    /**
     * 项目级别
     **/
    @TableField("pro_level")
    private String proLevel;
    /**
     * 执行状态
     **/
    @TableField("execution_status")
    private String executionStatus;
    /**
     * 项目现状概述
     **/
    @TableField("pro_situation")
    private String proSituation;
    /**
     * 需协同事项
     **/
    @TableField("coordinated_matters")
    private String coordinatedMatters;
    /**
     * 本周评审情况
     **/
    @TableField("this_review")
    private int thisReview;
    /**
     * 本周提交文档情况
     **/
    @TableField("this_doc")
    private int thisDoc;
    /**
     * 下周安排测试
     **/
    @TableField("next_test")
    private int nextTest;
    /**
     * 下周是否有评审
     **/
    @TableField("next_review")
    private int nextReview;
    /**
     * 下周提交文档情况
     **/
    @TableField("next_doc")
    private int nextDoc;
    /**
     * 计划里程碑
     **/
    @TableField("plan_milepost")
    private String planMilepost;
    /**
     * 实际里程碑
     **/
    @TableField("actual_milepost")
    private String actualMilepost;
    /**
    * 项目状态：0表示到项目经理；1表示到部门经理；2到技术中心
    **/
    @TableField("state")
    private int state;
    /**
     * 项目信息
     **/
    @TableField(exist = false)
    private Project project;
    /**
     * 台账内容
     **/
    @TableField(exist = false)
    private List<AC> acList;

}
