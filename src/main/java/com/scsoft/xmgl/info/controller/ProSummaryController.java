package com.scsoft.xmgl.info.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.common.handler.SystemCommonHandler;
import com.scsoft.xmgl.info.entity.Info;
import com.scsoft.xmgl.info.entity.TaskSummary;
import com.scsoft.xmgl.info.entity.WorkSummary;
import com.scsoft.xmgl.info.service.ITaskSummaryService;
import com.scsoft.xmgl.info.service.IUnfinishedProjectService;
import com.scsoft.xmgl.info.service.IWorkSummaryService;
import com.scsoft.xmgl.info.service.Impl.TaskSummaryServiceImpl;
import com.scsoft.xmgl.info.service.Impl.UnfinishedProjectServiceImpl;
import com.scsoft.xmgl.info.service.Impl.WorkSummaryServiceImpl;
import com.scsoft.xmgl.system.entity.Role;
import com.scsoft.xmgl.system.service.IRoleService;
import com.scsoft.xmgl.system.service.IUserService;
import com.scsoft.xmgl.system.service.impl.RoleServiceImpl;
import com.scsoft.xmgl.system.service.impl.UserServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/info/prosummary")
public class ProSummaryController extends BaseController {

    @Autowired
    IUnfinishedProjectService unfinishedProjectService = new UnfinishedProjectServiceImpl();
    @Autowired
    IUserService userService = new UserServiceImpl();
    @Autowired
    IRoleService roleService = new RoleServiceImpl();
    @Autowired
    IWorkSummaryService workSummaryService = new WorkSummaryServiceImpl();
    @Autowired
    ITaskSummaryService taskSummaryService = new TaskSummaryServiceImpl();

    private final String PREFIX="module/info";
    @RequestMapping
    public String test(){
        return PREFIX+"/prosummary";
    }

    @RequiresPermissions(value = {"prosummary:view", "prosummary:view1","prosummary:view2"}, logical = Logical.OR)
    @RequestMapping("/editForm")
    public String editForm(Model model,String proName,String proManager){
//        String realName = userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName()).getRealName();
//        model.addAttribute("realName",realName);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(date);
        model.addAttribute("format",format);
        model.addAttribute("proName",proName);
        model.addAttribute("proManager",proManager);
        return PREFIX+"/prosummaryForm";
    }


    @RequiresPermissions("prosummary:view")
    @ResponseBody
    @RequestMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
    })
    public PageResult<Info> infoList(Integer page, Integer limit){
        if (page == null) {
            page = 1;
            limit = 10;
        }
        String realName = userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName()).getRealName();
        return unfinishedProjectService.infoList(page,limit,realName);
    }
    @RequiresPermissions("prosummary:view1")
    @ResponseBody
    @RequestMapping("/list1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
    })
    public PageResult<Info> infoList1(Integer page, Integer limit){
        if (page == null) {
            page = 1;
            limit = 10;
        }
        String section = userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName()).getSection();

        return unfinishedProjectService.infoList1(page,limit,section);
    }
    @RequiresPermissions("prosummary:view2")
    @ResponseBody
    @RequestMapping("/list2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
    })
    public PageResult<Info> infoList2(Integer page, Integer limit){
        if (page == null) {
            page = 1;
            limit = 10;
        }
        return unfinishedProjectService.infoList1(page,limit,null);
    }
    @RequiresPermissions(value = {"prosummary:view", "prosummary:view1","prosummary:view2"}, logical = Logical.OR)
    @ResponseBody
    @RequestMapping("/summaryList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
    })
    public PageResult<WorkSummary> summaryList(Integer page, Integer limit, String proName,String beginDate,String endDate) throws ParseException {
        if (page == null) {
            page = 1;
            limit = 10;
        }
        List<WorkSummary> workSummaryList = new ArrayList<WorkSummary>();
        List<Role> roleList = roleService.getByUserId(userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName()).getId());
        for (Role role:roleList){
            String roleName = role.getName();
            if (roleName.equals("项目经理")||roleName.equals("部门经理")||roleName.equals("普通管理员")||roleName.equals("领导组")){
                workSummaryList = workSummaryService.getByThree3(proName, beginDate, endDate,0,0);
            }
        }
        int taskType = 0;
        List<WorkSummary> workSummaryList1 = new ArrayList<WorkSummary>();
        for (WorkSummary workSummary:workSummaryList){
            String memberName = workSummary.getUserName();
            List<TaskSummary> taskSummaryList = taskSummaryService.getByThree(taskType,memberName, proName, beginDate, endDate);
            //如果完成任务为空  不应展示其项目周报
            if (taskSummaryList.size()==0){
            }else {
                workSummary.setTaskSummaries(taskSummaryList);
                workSummaryList1.add(workSummary);
            }
        }
        Page<WorkSummary> workSummaryPage = new Page<WorkSummary>(page,limit);
        return new PageResult<WorkSummary>(workSummaryList1,workSummaryPage.getTotal());
    }

    @RequiresPermissions(value = {"prosummary:view", "prosummary:view1","prosummary:view2"}, logical = Logical.OR)
    @ResponseBody
    @RequestMapping("/summaryList1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
    })
    public PageResult<WorkSummary> summaryList1(Integer page, Integer limit, String proName,String beginDate,String endDate) throws ParseException {
        if (page == null) {
            page = 1;
            limit = 10;
        }
        List<WorkSummary> workSummaryList = new ArrayList<WorkSummary>();
        List<Role> roleList = roleService.getByUserId(userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName()).getId());
        for (Role role:roleList){
            String roleName = role.getName();
            if (roleName.equals("项目经理")||roleName.equals("部门经理")||roleName.equals("普通管理员")||roleName.equals("领导组")){
                workSummaryList = workSummaryService.getByThree3(proName, beginDate, endDate,0,0);
            }
        }
        int taskType = 1;
        List<WorkSummary> workSummaryList1 = new ArrayList<WorkSummary>();
        for (WorkSummary workSummary:workSummaryList){
            String memberName = workSummary.getUserName();
            List<TaskSummary> taskSummaryList = taskSummaryService.getByThree(taskType,memberName, proName, beginDate, endDate);
            //如果计划任务为空  不应展示其项目周报
            if (taskSummaryList.size()==0){
            }else {
                workSummary.setTaskSummaries(taskSummaryList);
                workSummaryList1.add(workSummary);
            }
        }
        Page<WorkSummary> workSummaryPage = new Page<WorkSummary>(page,limit);
        return new PageResult<WorkSummary>(workSummaryList1,workSummaryPage.getTotal());
    }
}
