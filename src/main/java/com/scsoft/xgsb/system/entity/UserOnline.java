package com.scsoft.xgsb.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scsoft.scpt.base.enitity.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Description: 在线用户
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@TableName("sys_user_online")
@Data
public class UserOnline extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;
    @TableField("user_id")
    private Integer userId;
    @TableField("username")
    private String username;
    /**
     * 用户主机地址
     */
    @TableField("host")
    private String host;
    /**
     * 用户登录时系统IP
     */
    @TableField("system_host")
    private String systemHost;
    /**
     * 用户浏览器类型
     */
    @TableField("user_agent")
    private String userAgent;
    /**
     * 在线状态
     */
    @TableField("status")
    private String status;
    /**
     * session创建时间
     */
    @TableField("start_timestsamp")
    private Date startTimestsamp;
    /**
     * session最后访问时间
     */
    @TableField("last_access_time")
    private LocalDateTime lastAccessTime;
    /**
     * 超时时间
     */
    @TableField("online_timeout")
    private Long onlineTimeout;
    /**
     * 备份的当前用户会话
     */
    @TableField("online_session")
    private String onlineSession;

}
