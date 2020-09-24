package com.scsoft.xmgl.info.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scsoft.scpt.base.enitity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;


@TableName("staff_out_register")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OutRegister extends BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     *部门
     **/
    @TableField("dept")
    private String dept;
    /**
     *日期
     **/
    @TableField("out_date")
    private String outDate;
    /**
     *外出时间
     **/
    @TableField("out_time")
    private String outTime;
    /**
     *姓名
     **/
    @TableField("user_name")
    private String userName;
    /**
     *外出事由
     **/
    @TableField("reason")
    private String reason;
    /**
     *返回时间
     **/
    @TableField("in_time")
    private String inTime;
    /**
     *领导批准
     **/
    @TableField("approval")
    private Integer approval;
    /**
     *备注
     **/
    @TableField("remark")
    private String remark;
}
