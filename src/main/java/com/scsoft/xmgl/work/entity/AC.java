package com.scsoft.xmgl.work.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scsoft.scpt.base.enitity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@TableName("work_ac")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AC extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
    * id
    **/
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
    * 本周工作总结
    **/
    @TableField("this_summary")
    private String thisSummary;
    /**
     * 下周或下阶段工作计划
     **/
    @TableField("next_summary")
    private String nextSummary;
}
