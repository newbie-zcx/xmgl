package com.scsoft.wlyz.discuss.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

@Data
public class IssueModel {

    /**
     * 议题标题
     */
    private String title;

    /**
     * 议题内容
     */
    private String content;

    /**
     * 议题类型
     */
    private String issueType;

    /**
     * 议题权限类型
     */
    private String issueShiroType;

    /**
     * 文件列表
     */
    private List<Integer> fileList;

    /**
     * 用户列表
     */
    private List<String> userList;
}
