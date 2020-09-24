package com.scsoft.xmgl.info.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scsoft.scpt.base.enitity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@TableName("info_milepost")
@Data
@EqualsAndHashCode(callSuper=true)
@Accessors(chain = true)
public class Milepost extends BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
    *项目编号
    **/
    @TableField("pro_id")
    private String proId;
    /**
     *项目名称
     **/
    @TableField("pro_name")
    private String proName;
    /**
     *项目经理
     **/
    @TableField("pro_manager")
    private String proManager;
    /**
     *里程碑名称
     **/
    @TableField("milepost_name")
    private String milepostName;
    /**
     *里程碑开始时间
     **/
    @TableField("milepost_start_date")
    private String milepostStartDate;
    /**
     *里程碑结束时间
     **/
    @TableField("milepost_end_date")
    private String milepostEndDate;
    /**
     *入口条件
     **/
    @TableField("entrance_conditions")
    private String entranceConditions;
    /**
     *出口条件
     **/
    @TableField("exit_conditions")
    private String exitConditions;
    /**
     *输出文档
     **/
    @TableField("output_doc")
    private String outputDoc;
    /**
     *里程碑状态
     **/
    @TableField("milepost_status")
    private String milepostStatus;
    /**
     *剩余时间
     **/
    @TableField("milepost_remainder")
    private int milepostRemainder;
    /**
     *备注
     **/
    @TableField("remark")
    private String remark;
}
