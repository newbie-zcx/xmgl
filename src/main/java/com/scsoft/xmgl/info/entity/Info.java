package com.scsoft.xmgl.info.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scsoft.scpt.base.enitity.BaseEntity;
import com.scsoft.xmgl.system.entity.User;
import com.scsoft.xmgl.work.entity.ProUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@TableName("info_technology")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Info extends BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 完成情况
     */
    @TableField("pro_completion")
    private String proCompletion;
    /**
     * 所处阶段
     */
    @TableField("pro_stage")
    private String proStage;
    /**
     * 项目id
     */
    @TableField("pro_id")
    private String proId;
    /**
     * 合同id
     */
    @TableField("contract_id")
    private String contractId;
    /**
     * 项目类型
     */
    @TableField("pro_type")
    private String proType;
    /**
     * 项目名称
     */
    @TableField("pro_name")
    private String proName;
    /**
     * 项目经理
     */
    @TableField("pro_manager")
    private String proManager;
    /**
     * 承担部门
     */
    @TableField("pro_dept")
    private String proDept;
    /**
     * 联合开发_部门
     */
    @TableField("joint_dev")
    private String jointDev;
    /**
     * 项目启动时间
     */
    @TableField("pro_datetime")
    private String proDatetime;
    /**
     * 客户名称
     */
    @TableField("customer_name")
    private String customerName;
    /**
     * 签约时间
     */
    @TableField("contract_datetime")
    private String contractDatetime;
    /**
     * 签约人
     */
    @TableField("contract_people")
    private String contractPeople;
    /**
     * 合同金额
     */
    @TableField("contract_amount")
    private String contractAmount;
    /**
     * 计划完成时间
     */
    @TableField("pro_finish_plan")
    private String proFinishPlan;
    /**
     * 验收时间
     */
    @TableField("pro_finish_actual")
    private String proFinishActual;
    /**
     * 质保期限
     */
    @TableField("pro_zb_period")
    private String proZBPeriod;
    /**
     * 质保结束时间
     */
    @TableField("pro_zb_end")
    private String proZBEnd;
    /**
     * 运维期限
     */
    @TableField("pro_yw_period")
    private String proYWPeriod;
    /**
     * 运维结束时间
     */
    @TableField("pro_yw_end")
    private String proYWEnd;
    /**
     * 付款方式
     */
    @TableField("contract_payment")
    private String contractPayment;
    /**
    * 项目组用成员
    **/
    @TableField(exist = false)
    private List<User> userList;
}
