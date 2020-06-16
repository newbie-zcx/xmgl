package com.scsoft.xmgl.syslog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.scsoft.scpt.base.enitity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Description: 登陆日志记录表
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysLoginRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 操作系统
     */
    private String osName;

    /**
     * 设备名
     */
    private String device;

    /**
     * 浏览器类型
     */
    private String browserType;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 登录时间
     */
    private Date createTime;
    private String nickName;  // 用户昵称
    private String userName;  // 用户账号

}
