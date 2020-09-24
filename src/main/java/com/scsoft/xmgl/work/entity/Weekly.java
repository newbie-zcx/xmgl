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
* 周报
**/
@TableName("work_weekly")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Weekly  extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * ID
     **/
    @TableId(type = IdType.AUTO)
    private Integer id;
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
     * 添加人
     **/
    @TableField("user_name")
    private String userName;
    /**
     * 上月周工时
     **/
    @TableField("lastmonth_hour")
    private int lastMonthHour;
    /**
     * 本月周工时
     **/
    @TableField("thismonth_hour")
    private int thisMonthHour;

    /**
     * 项目信息
     */
    @TableField(exist = false)
    private Project project;
    /**
     * 周报内容
     */
    @TableField(exist = false)
    private String scList;
    /**
     * 修改信息
     */
    @TableField(exist = false)
    private List<UpdateLog> updateLogList;
    /**
     * 本月总工时
     */
    @TableField(exist = false)
    private int monthHour;
    /**
     * 项目总工时
     */
    @TableField(exist = false)
    private int projectHour;

}
