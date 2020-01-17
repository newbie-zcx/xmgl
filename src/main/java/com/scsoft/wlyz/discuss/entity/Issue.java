package com.scsoft.wlyz.discuss.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.scsoft.scpt.base.enitity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author chengshangshu
 * @since 2020-01-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("yz_issue")
public class Issue extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 议题ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 议题标题
     */
    @TableField("TITLE")
    private String title;

    /**
     * 议题内容
     */
    @TableField("CONTENT")
    private String content;

    /**
     * 议题类型
     */
    @TableField("ISSUE_TYPE")
    private String issueType;



}
