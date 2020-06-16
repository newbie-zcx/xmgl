package com.scsoft.xmgl.syslog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scsoft.xmgl.syslog.entity.SysLog;
import com.scsoft.xmgl.syslog.service.impl.SysLogServiceImpl;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: 系统日志表 前端控制器
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@Controller
@RequestMapping("/syslog/log")
public class SysLogController extends BaseController {
    @Autowired
    private SysLogServiceImpl logService;
    private final String PREFIX="module/syslog";

    @RequiresPermissions("log:view")
    @RequestMapping("logRecord")
    public String logRecord(String type, Model model) {
        model.addAttribute("type",type);
        return PREFIX+"/log_record";
    }

	/**
     * 查询所有操作日志
     **/
    @RequiresPermissions("log:view")
    @ResponseBody
    @RequestMapping("/list")
    public PageResult<SysLog> list(Integer page, Integer limit, String startDate, String endDate, String account, Integer type) {
        QueryWrapper<SysLog> wrapper = new QueryWrapper<SysLog>();
        if (StringUtils.isBlank(startDate)) {
            startDate = null;
        } else {
            startDate += " 00:00:00";
            wrapper.ge("create_time", startDate);
        }
        if (StringUtils.isBlank(endDate)) {
            endDate = null;
        } else {
            endDate += " 23:59:59";
            wrapper.le("create_time", endDate);
        }
        if (StringUtils.isNotBlank(account)) {
            account = account;
            wrapper.eq("user_name", account);
        }
        if(type!=null){
            wrapper.eq("type",type);
        }

        Page<SysLog> logPage = new Page<SysLog>(page, limit);
        IPage<SysLog> logpagList = logService.page(logPage, wrapper.orderByDesc("create_date"));
        return new PageResult<SysLog>(logpagList.getRecords(),logpagList.getTotal() );
    }
}
