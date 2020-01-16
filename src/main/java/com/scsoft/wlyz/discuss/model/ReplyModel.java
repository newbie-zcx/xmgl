package com.scsoft.wlyz.discuss.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

@Data
public class ReplyModel {

    /**
     * 议题ID
     */
    private Integer issueId;

    /**
     * 被回复内容ID
     */
    private Integer toReplyId;

    /**
     * 回复内容
     */
    private String replyDesc;

    /**
     * 显示级别
     */
    private String showLevel;

    /**
     * 被回复用户ID
     */
    private String toReplyUserId;

    /**
     * 被回复用户名称
     */
    private String toReplyUserName;

    /**
     * 文件列表
     */
    private List<Integer> fileList;

}
