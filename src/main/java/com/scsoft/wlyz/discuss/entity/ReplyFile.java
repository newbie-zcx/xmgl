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
 * 回复附件关系表
 * </p>
 *
 * @author wangchao
 * @since 2020-01-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("yz_reply_file")
public class ReplyFile extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 回复附件ID
     */
    @TableId("ID")
    private Integer id;

    /**
     * 回复ID
     */
    @TableField("REPLY_ID")
    private Integer replyId;

    /**
     * 附件ID
     */
    @TableField("FILE_ID")
    private Integer fileId;


}
