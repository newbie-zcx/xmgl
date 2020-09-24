package com.scsoft.xmgl.info.controller;

import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.common.handler.SystemCommonHandler;
import com.scsoft.xmgl.common.utils.DateUtils;
import com.scsoft.xmgl.info.entity.TaskSummary;
import com.scsoft.xmgl.info.entity.WorkSummary;
import com.scsoft.xmgl.info.service.ITaskSummaryService;
import com.scsoft.xmgl.info.service.IWorkSummaryService;
import com.scsoft.xmgl.info.service.Impl.TaskSummaryServiceImpl;
import com.scsoft.xmgl.info.service.Impl.WorkSummaryServiceImpl;
import com.scsoft.xmgl.system.entity.User;
import com.scsoft.xmgl.system.service.IUserService;
import com.scsoft.xmgl.system.service.impl.UserServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Controller
@RequestMapping("/info/tasksummary")
public class TaskSummaryController extends BaseController {

    @Autowired
    ITaskSummaryService taskSummaryService = new TaskSummaryServiceImpl();
    @Autowired
    IWorkSummaryService workSummaryService = new WorkSummaryServiceImpl();
    @Autowired
    IUserService userService = new UserServiceImpl();
    private final String PREFIX="module/info";

    @RequestMapping("/editFormadd")
    public String testadd(Model model,String beginDate,String endDate,int workSummaryId){
        User user = userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName());
        String realName = user.getRealName();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(date);
        model.addAttribute("format",format);
        model.addAttribute("realName",realName);
        model.addAttribute("beginDate",beginDate);
        model.addAttribute("endDate",endDate);
        model.addAttribute("workSummaryId",workSummaryId);
        return PREFIX+"/tasksummaryForm";
    }
    @RequestMapping("/editFormadd1")
    public String testadd1(Model model,String beginDate,String endDate,int workSummaryId){
        User user = userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName());
        String realName = user.getRealName();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(date);
        model.addAttribute("format",format);
        model.addAttribute("realName",realName);
        model.addAttribute("beginDate",beginDate);
        model.addAttribute("endDate",endDate);
        model.addAttribute("workSummaryId",workSummaryId);
        return PREFIX+"/tasksummaryForm1";
    }
    @RequestMapping("/editFormedit")
    public String testedit(Model model,int id,int workSummaryId){
        TaskSummary taskSummary = taskSummaryService.getById(id);
        model.addAttribute("taskType",taskSummary.getTaskType());
        model.addAttribute("workSummaryId",workSummaryId);
        return PREFIX+"/tasksummaryEditForm";
    }
    @RequestMapping("/editFormedit1")
    public String testedit1(Model model,int id,int workSummaryId){
        TaskSummary taskSummary = taskSummaryService.getById(id);
        model.addAttribute("taskType",taskSummary.getTaskType());
        model.addAttribute("workSummaryId",workSummaryId);
        return PREFIX+"/tasksummaryEditForm1";
    }
    @RequestMapping("/editForm1")
    public String test2(String proName,String beginDate,String endDate,String userName,int id,Model model){
        model.addAttribute("proName",proName);
        model.addAttribute("beginDate",beginDate);
        model.addAttribute("endDate",endDate);
        model.addAttribute("userName",userName);
        User user = userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName());
        String realName = user.getRealName();
        model.addAttribute("realName",realName);
        model.addAttribute("workSummaryId",id);
        return PREFIX+"/tasksummary";
    }


    @ResponseBody
    @RequestMapping("/list1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
    })
    public PageResult<TaskSummary> list1(Integer page, Integer limit,String proName,String beginDate,String endDate,String userName) throws ParseException {
        if (page == null) {
            page = 1;
            limit = 10;
        }
        return taskSummaryService.list1(page, limit, proName, beginDate, endDate,userName);
    }
    @ResponseBody
    @RequestMapping("/list2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
    })
    public PageResult<TaskSummary> list2(Integer page, Integer limit,String proName,String beginDate,String endDate,String userName) throws ParseException {
        if (page == null) {
            page = 1;
            limit = 10;
        }
        return taskSummaryService.list2(page, limit,proName,beginDate,endDate,userName);
    }


    @ResponseBody
    @RequestMapping("/add")
    public JsonResult add(TaskSummary taskSummary,String beginDate1,String endDate1,int workSummaryId) throws ParseException {
        if (taskSummary.getTaskType()==1){
            taskSummary.setBeginDate(DateUtils.getWeekDate().get("nextMondayDate"));
            taskSummary.setEndDate(DateUtils.getWeekDate().get("nextSundayDate"));
        }
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        long begintime = sdf.parse(beginDate1).getTime();//周报起始时间
        long endtime = sdf.parse(endDate1).getTime();//周报结束时间
        long nowtime = new Date().getTime();//现在时间
        WorkSummary workSummary = workSummaryService.getById(workSummaryId);
        String sectionPost = userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName()).getSectionPost();
        if (sectionPost.equals("部门经理")){
            if (begintime<=nowtime&&nowtime<=endtime){
                if (taskSummaryService.add(taskSummary)){
                    return JsonResult.ok("添加成功");
                }else {
                    return JsonResult.error("添加失败");
                }
            }else {
                return JsonResult.error("填报时间已过，请联系管理员！");
            }
        }else if (sectionPost.equals("项目经理")){
            if (workSummary.getExamineStatusDept()==1){
                if (begintime<=nowtime&&nowtime<=endtime){
                    if (taskSummaryService.add(taskSummary)){
                        return JsonResult.ok("添加成功");
                    }else {
                        return JsonResult.error("添加失败");
                    }
                }else {
                    return JsonResult.error("填报时间已过，请联系管理员！");
                }
            }else {
                return JsonResult.error("部门经理已审核，请勿添加");
            }
        }else {
            if (workSummary.getExamineStatusDept()==1||workSummary.getExamineStatus()==1){
                if (begintime<=nowtime&&nowtime<=endtime){
                    if (taskSummaryService.add(taskSummary)){
                        return JsonResult.ok("添加成功");
                    }else {
                        return JsonResult.error("添加失败");
                    }
                }else {
                    return JsonResult.error("填报时间已过，请联系管理员！");
                }
            }else {
                return JsonResult.error("经理已审核，请勿添加");
            }
        }
    }
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(TaskSummary taskSummary,int workSummaryId) {
        WorkSummary workSummary = workSummaryService.getById(workSummaryId);
        String sectionPost = userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName()).getSectionPost();
        if (sectionPost.equals("部门经理")){
            if (taskSummaryService.update(taskSummary)) {
                return JsonResult.ok("修改成功");
            } else {
                return JsonResult.error("修改失败");
            }
        }else if (sectionPost.equals("项目经理")){
            if (workSummary.getExamineStatusDept()==1){
                if (taskSummaryService.update(taskSummary)) {
                    return JsonResult.ok("修改成功");
                } else {
                    return JsonResult.error("修改失败");
                }
            }else {
                return JsonResult.error("经理已审核，请勿修改");
            }
        }else {
            if (workSummary.getExamineStatus()==1&&workSummary.getExamineStatusDept()==1){
                if (taskSummaryService.update(taskSummary)) {
                    return JsonResult.ok("修改成功");
                } else {
                    return JsonResult.error("修改失败");
                }
            }else {
                return JsonResult.error("已审核，请勿修改");
            }
        }
    }
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult delete(int id,int workSummaryId) {
        WorkSummary workSummary = workSummaryService.getById(workSummaryId);
        String sectionPost = userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName()).getSectionPost();
        if (sectionPost.equals("部门经理")){
            if (taskSummaryService.delete(id)) {
                return JsonResult.ok();
            } else {
                return JsonResult.error();
            }
        }else if (sectionPost.equals("项目经理")){
            if (workSummary.getExamineStatusDept()==1){
                if (taskSummaryService.delete(id)) {
                    return JsonResult.ok();
                } else {
                    return JsonResult.error();
                }
            }else {
                return JsonResult.error("经理已审核，请勿修改");
            }
        }else {
            if (workSummary.getExamineStatus()==1&&workSummary.getExamineStatusDept()==1){
                if (taskSummaryService.delete(id)) {
                    return JsonResult.ok();
                } else {
                    return JsonResult.error();
                }
            }else{
                return JsonResult.error("已审核，请勿删除");
            }
        }
    }
}
