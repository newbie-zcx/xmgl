package com.scsoft.xmgl.syslog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.xmgl.syslog.entity.SysLoginRecord;
import com.scsoft.scpt.common.PageResult;

/**
 * @Description: 登陆日志记录表 服务类
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
public interface ISysLoginRecordService extends IService<SysLoginRecord> {
    PageResult<SysLoginRecord> list(Integer page, Integer limit, String startDate, String endDate, String account);
}
