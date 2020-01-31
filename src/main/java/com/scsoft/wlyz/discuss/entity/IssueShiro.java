package com.scsoft.xgsb.discuss.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.scsoft.scpt.base.enitity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 议题权限关系表
 * </p>
 *
 * @author wangchao
 * @since 2020-01-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("yz_issue_shiro")
public class IssueShiro extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 议题ID
     */
    @TableField("ISSUE_ID")
    private Integer issueId;

    /**
     * 用户名称
     */
    @TableField("USER_NAME")
    private String userName;


}
