package com.scsoft.xmgl.work.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scsoft.scpt.base.enitity.BaseEntity;
import com.scsoft.xmgl.system.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;


/**
* 项目
**/

@TableName("work_pro")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Project extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * ID
     **/
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
    * 项目编号
    **/
    @TableField("pro_id")
    private String proId;
    /**
     * 项目名称
     **/
    @TableField("pro_name")
    private String proName;
    /**  项目简称*/
    @TableField("nick_name")
    private String nickName;
    /**  项目简述*/
    @TableField("pro_resume")
    private String proResume;
    /**
     * 项目性质
     **/
    @TableField("pro_nature")
    private String proNature;
    /**
     * 项目类型
     **/
    @TableField("pro_type")
    private String proType;
    /**
     * 技术负责人
     **/
    @TableField("pro_manager")
    private String proManager;
    /**
     * 项目经理
     **/
    @TableField("manager")
    private String manager;
    /**
     * 承担部门
     **/
    @TableField("pro_dept")
    private String proDept;
    /**
     * 商务负责人
     **/
    @TableField("pro_signer")
    private String proSigner;
    /**
     * 项目启动时间
     **/
    @TableField("start_date")
    private String startDate;
    /**
     * 计划完成时间
     **/
    @TableField("plan_complete_date")
    private String planCompleteDate;
    /**
     * 项目变更时间
     **/
    @TableField("plan_change_date")
    private String planChangeDate;
    /** 项目结束状态*/
    @TableField("pro_end_state")
    private Integer proEndState;
    /**
     * 项目组用成员
     **/
    @TableField(exist = false)
    private List<User> userList;

    /**
     * 上月周工时
     **/
    @TableField(exist = false)
    private Integer lastMonthHour;
    /**
     * 本月周工时
     **/
    @TableField(exist = false)
    private Integer thisMonthHour;
    /**
     * 项目总工时
     **/
    @TableField(exist = false)
    private Integer projectHour;
    /**
     * 月总工时
     **/
    @TableField(exist = false)
    private Integer MonthHour;
    /**
     * 上月周工时
     */
    @TableField(exist = false)
    private int lastWeekHour;

    /**
     * 本月周工时
     */
    @TableField(exist = false)
    private int thisWeekHour;
}
