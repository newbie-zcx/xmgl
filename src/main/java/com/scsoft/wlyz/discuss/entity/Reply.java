package com.scsoft.wlyz.discuss.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.scsoft.scpt.base.enitity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 回复表
 * </p>
 *
 * @author wangchao
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("yz_reply")
public class Reply extends BaseEntity {

    private static final long serialVersionUID=1L;

    @TableId("ID")
    private Integer id;

    /**
     * 议题ID
     */
    @TableField("ISSUE_ID")
    private Integer issueId;

    /**
     * 回复内容
     */
    @TableField("REPLY_DESC")
    private String replyDesc;

    /**
     * 被回复内容ID
     */
    @TableField("TO_REPLY_ID")
    private Integer toReplyId;

    /**
     * 被回复用户ID
     */
    @TableField("TO_REPLY_USER_ID")
    private String toReplyUserId;

    /**
     * 被回复用户名称
     */
    @TableField("TO_REPLY_USER_NAME")
    private String toReplyUserName;

    /**
     * 显示级别
     */
    @TableField("SHOW_LEVEL")
    private String showLevel;


}
