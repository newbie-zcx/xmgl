package com.scsoft.xmgl.work.controller;

import com.scsoft.scpt.annotation.SysLog;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;

import com.scsoft.xmgl.common.handler.SystemCommonHandler;
import com.scsoft.xmgl.info.entity.Info;
import com.scsoft.xmgl.info.service.IUnfinishedProjectService;
import com.scsoft.xmgl.system.entity.User;
import com.scsoft.xmgl.system.service.IUserService;
import com.scsoft.xmgl.work.service.IScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


/**
*项目进度管理
**/
@Api(value = "项目进度的接口", tags = "schedule")
@Controller
@RequestMapping("/work/schedule")
public class ScheduleController extends BaseController {

    @Autowired
    private IUnfinishedProjectService unfinishedProjectService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IScheduleService scheduleService;

    private final String PREFIX="module/work";

    @RequiresPermissions("schedule:view")
    @RequestMapping
    public String schedule(Model model){
        return PREFIX+"/schedule";
    }

    @RequiresPermissions("schedule:edit")
    @RequestMapping("/editForm")
    public String scheduleForm(int id,Model model){
        model.addAttribute("id",id);
        List<User> userList = userService.list();
        model.addAttribute("userList",userList);
        return PREFIX+"/schedule_form";
    }

    @RequiresPermissions("schedule:view")
    @ResponseBody
    @RequestMapping("/list")
    @ApiOperation(value = "查询项目经理所属项目", notes = "查询项目经理所属项目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "searchKey", value = "筛选条件字段", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "searchValue", value = "筛选条件关键字", dataType = "String", paramType = "form"),
    })
    public PageResult<Info> list(Integer page, Integer limit, String searchKey, String searchValue) {
        if (page == null) {
            page = 1;
            limit = 5;
        }
        String realName = SystemCommonHandler.getLoginUser().getRealName();
        return unfinishedProjectService.listInfoByManager(page,limit,realName);
    }

    @RequiresPermissions("schedule:add")
    @RequestMapping("/proMember")
    @ResponseBody
    @SysLog(operationType="edit操作:",operationName="edit项目组用户")
    public JsonResult editMember(int id,String member){
        String[] memberName = member.split(",");
        List<Integer> userIdList = new ArrayList();
        for (int i = 0;i<memberName.length;i++){
            Integer userId = Integer.parseInt(memberName[i]);
            userIdList.add(userId);
        }
        if (scheduleService.addMember(id, userIdList)){
            return JsonResult.ok("保存成功");
        }else {
            return JsonResult.error("保存失败");
        }
    }

}
