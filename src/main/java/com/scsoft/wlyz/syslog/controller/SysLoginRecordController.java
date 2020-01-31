package com.scsoft.xgsb.syslog.controller;

import com.scsoft.xgsb.syslog.entity.SysLoginRecord;
import com.scsoft.xgsb.syslog.service.ISysLoginRecordService;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: 登陆日志记录表 前端控制器
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */

@Controller
@RequestMapping("/syslog/loginLog")
public class SysLoginRecordController extends BaseController {
    @Autowired
    private ISysLoginRecordService loginRecordService;
    private final String PREFIX="module/syslog";
    @RequiresPermissions("loginRecord:view")
    @RequestMapping("/loginRecord")
    public String loginRecord() {
        return PREFIX+"/login_record";
    }

    /**
     * 查询所有登录日志
     **/
    @RequiresPermissions("loginRecord:view")
    @ResponseBody
    @RequestMapping("/list")
    public PageResult<SysLoginRecord> list(Integer page, Integer limit, String startDate, String endDate, String account) {
        if (StringUtils.isBlank(startDate)) {
            startDate = null;
        } else {
            startDate += " 00:00:00";
        }
        if (StringUtils.isBlank(endDate)) {
            endDate = null;
        } else {
            endDate += " 23:59:59";
        }
        if (StringUtils.isNotBlank(account)) {
            account = account;
        }
        return loginRecordService.list(page,limit,startDate,endDate,account);
    }
}
