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
     * 回复内容
     */
    private String replyDesc;

    /**
     * 显示级别
     */
    private String showLevel;

    /**
     * 文件列表
     */
    private List<Integer> fileList;

}
