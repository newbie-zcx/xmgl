package com.scsoft.xgsb.yqsb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scsoft.scpt.base.enitity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 疫情上报详情
 * </p>
 *
 * @author 赵鹏飞
 * @since 2020-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("xg_detail")
public class Detail extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 姓名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 部门
     */
    @TableField("user_depart")
    private String userDepart;

    /**
     * 外出状态 0 在老家、1、在郑州、2、外出
     */
    @TableField("out_state")
    private String outState;

    /**
     * 是否接触武汉反郑
     */
    @TableField("wh_fz")
    private String whFz;

    /**
     * 接触详情
     */
    @TableField("wh_detail")
    private String whDetail;

    /**
     * 接触日期
     */
    @TableField("wh_date")
    private Date whDate;

    /**
     * 是否在信阳、周口、商丘、驻马店、南阳活动
     */
    @TableField("hd_xznsz")
    private String hdXznsz;

    /**
     * 是否发烧感冒咳嗽 0、正常、1发烧、2、感冒 3、咳嗽4、干呕
     */
    @TableField("fsgm")
    private String fsgm;

    /**
     * 家属是否发烧感冒咳嗽 0、正常、1发烧、2、感冒 3、咳嗽4、干呕
     */
    @TableField("jsfsgm")
    private String jsfsgm;

    /**
     * 所在地
     */
    @TableField("address")
    private String address;

    /**
     * 个人状态是否良好 0是1否
     */
    @TableField("status")
    private Integer status;

    /**
     * 交通是否影响，0、正常、1、封路 2、交通管制、3无法反郑
     */
    @TableField("jtyx")
    private String jtyx;

    /**
     * 解封时间
     */
    @TableField("jf_date")
    private Date jfDate;


}
