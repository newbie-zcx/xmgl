package com.scsoft.xmgl.info.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scsoft.scpt.annotation.SysLog;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.common.handler.SystemCommonHandler;
import com.scsoft.xmgl.common.utils.DateUtils;
import com.scsoft.xmgl.common.utils.PageResultUtils;
import com.scsoft.xmgl.info.entity.Info;
import com.scsoft.xmgl.info.entity.TaskSummary;
import com.scsoft.xmgl.info.entity.WorkSummary;
import com.scsoft.xmgl.info.mapper.TaskSummaryMapper;
import com.scsoft.xmgl.info.mapper.WorkSummaryMapper;
import com.scsoft.xmgl.info.service.ITaskSummaryService;
import com.scsoft.xmgl.info.service.IUnfinishedProjectService;
import com.scsoft.xmgl.info.service.IWorkSummaryService;
import com.scsoft.xmgl.info.service.Impl.TaskSummaryServiceImpl;
import com.scsoft.xmgl.info.service.Impl.UnfinishedProjectServiceImpl;
import com.scsoft.xmgl.info.service.Impl.WorkSummaryServiceImpl;
import com.scsoft.xmgl.system.entity.Role;
import com.scsoft.xmgl.system.entity.User;
import com.scsoft.xmgl.system.service.IUserService;
import com.scsoft.xmgl.system.service.impl.UserServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ddf.EscherSerializationListener;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/info/worksummary")
public class WorkSummaryController extends BaseController {
    @Autowired
    IUserService userService = new UserServiceImpl();
    @Autowired
    IUnfinishedProjectService unfinishedProjectService = new UnfinishedProjectServiceImpl();
    @Autowired
    IWorkSummaryService workSummaryService = new WorkSummaryServiceImpl();
    @Autowired
    ITaskSummaryService taskSummaryService = new TaskSummaryServiceImpl();
    @Resource
    private WorkSummaryMapper workSummaryMapper;

    private final String PREFIX="module/info";

//    @RequestMapping
//    public String level(Model model) throws ParseException {
//        User user = userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName());
//        List<Info> infoList = unfinishedProjectService.listInfoByManager(user.getRealName());
//        model.addAttribute("infoList", infoList);
//        return PREFIX+"/worksummary";
//    }

    @RequiresPermissions("worksummary:add")
    @RequestMapping("/editForm")
    public String test1(Model model){
        model.addAttribute("realName",userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName()).getRealName());
        List<Info> infoList = unfinishedProjectService.listInfo();
        model.addAttribute("infoList",infoList);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
        String format = sdf.format(date);
        model.addAttribute("format",format);
        model.addAttribute("mondayDate", DateUtils.getWeekDate().get("mondayDate"));
        model.addAttribute("sundayDate",DateUtils.getWeekDate().get("sundayDate"));
        return PREFIX+"/worksummaryForm";
    }

    @RequiresPermissions("worksummary:add")
    @RequestMapping("/editForm2")
    public String test2(int id,int examineStatus,int examineStatusDept,int checkStatus,int checkStatusDept,String proName,String beginDate,String endDate,String userName,Model model){
        Integer id1 = userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName()).getId();
        List<Role> roleList = userService.getRoleByid(id1);
        model.addAttribute("proName",proName);
        model.addAttribute("beginDate",beginDate);
        model.addAttribute("endDate",endDate);
        model.addAttribute("userName",userName);
        List<Info> infoList = unfinishedProjectService.listInfo();
        model.addAttribute("infoList",infoList);
        User user = userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName());
        String realName = user.getRealName();
        model.addAttribute("realName",realName);
        model.addAttribute("workSummaryId",id);
        for (Role role:roleList){
            if(role.getName().equals("普通用户")||role.getName().equals("普通管理员")||role.getName().equals("手机端用户")){
            }else if(role.getName().equals("项目经理")){
                checkStatus = 0;
                workSummaryService.updateState(id, examineStatus, examineStatusDept, checkStatus, checkStatusDept);
            }else if (role.getName().equals("部门经理")){
                checkStatusDept = 0;
                workSummaryService.updateState(id, examineStatus, examineStatusDept, checkStatus, checkStatusDept);
            }
        }
        return PREFIX+"/worksummaryForm2";
    }
//    @RequestMapping("/editForm3")
//    public String test3(){
//        return PREFIX+"/commentForm";
//    }

    /**
     * 查询部门周报列表level1
     */
    @RequiresPermissions("worksummary:viewLevel1")
    @ResponseBody
    @RequestMapping("/list1")
    @ApiOperation(value = "查询所有部门周报信息", notes = "查询所有部门周报信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
    })
    public PageResult<WorkSummary> list(Integer page, Integer limit,String addTime,String userName) throws ParseException {
        if (page == null) {
            page = 1;
            limit = 10;
        }
        //计算总工时
        List<WorkSummary> workSummaryListHour = workSummaryService.list();
        for (WorkSummary workSummary:workSummaryListHour){
            int workHour = 0;
            List<TaskSummary> taskSummaryList = taskSummaryService.getByThree(0, workSummary.getUserName(), workSummary.getProName(), workSummary.getBeginDate(), workSummary.getEndDate());
            for (TaskSummary taskSummary:taskSummaryList){
                workHour = workHour+taskSummary.getTaskHour();
            }
            if (workHour!=0){
                workSummary.setWorkHour(workHour);
                workSummaryService.update(workSummary);
            }
        }
        Map<String, WorkSummary> map = new HashMap<String, WorkSummary>();
        String loginUserName = SystemCommonHandler.getLoginUserName();
        User user1 = userService.getRealNameByLoginName(loginUserName);
        String sectionPost = user1.getSectionPost();
        String section = userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName()).getSection();
        List<WorkSummary> workSummaryList = new ArrayList<WorkSummary>();
        List<WorkSummary> workSummaryList1 = new ArrayList<WorkSummary>();
        List<WorkSummary> workSummaries = new ArrayList<WorkSummary>();
        if (sectionPost.equals("部门经理")&&sectionPost.length()>0){
            List<User> userlist = userService.getRealNameBySection(section);
            for (User user:userlist){
                workSummaryList = workSummaryService.getByuserName(user.getRealName());
                if(workSummaryList!=null&&!workSummaryList.isEmpty()){
                    for (WorkSummary workSummary:workSummaryList){
                        String key = workSummary.getId()+workSummary.getUserName();
                        map.put(key,workSummary);
                    }
                }
            }
        }
        List<Info> infoList = unfinishedProjectService.list(new QueryWrapper<Info>().eq("pro_manager", user1.getRealName()));
        for (Info info:infoList){
            workSummaries = workSummaryService.getByproName(info.getProName());
            if (!workSummaries.isEmpty()){
                for (WorkSummary workSummary:workSummaries){
                    String key = workSummary.getId()+workSummary.getUserName();
                    map.put(key,workSummary);
                }
            }
        }
        QueryWrapper<WorkSummary> wrapper = new QueryWrapper<WorkSummary>();
        if (StringUtils.isNotBlank(addTime)) {
            wrapper.like("add_time", addTime);
        }
        if (StringUtils.isNotBlank(userName)) {
            wrapper.like("user_name", userName);
        }
        wrapper.eq("examine_status", 0);
        List<WorkSummary> workSummaryList2 = workSummaryMapper.selectList(wrapper);
        if (workSummaryList2!=null&&workSummaryList2.isEmpty()){
            for (WorkSummary workSummary:workSummaryList2){
                String key = workSummary.getId()+workSummary.getUserName();
                map.put(key,workSummary);
            }
        }
        for (Map.Entry<String, WorkSummary> entry : map.entrySet()) {
            workSummaryList1.add(entry.getValue());
        }
        if (StringUtils.isBlank(addTime)&&StringUtils.isBlank(userName)){
            return new PageResult<WorkSummary>(PageResultUtils.paginAtion(page,limit,workSummaryList1),workSummaryList1.size());
        }else {
            return new PageResult<WorkSummary>(PageResultUtils.paginAtion(page,limit,workSummaryList2),workSummaryList2.size());
        }
    }
    /**
     * 查询个人周报列表level2
     */
    @RequiresPermissions("worksummary:viewLevel2")
    @ResponseBody
    @RequestMapping("/list2")
    @ApiOperation(value = "个人查询周报信息", notes = "个人查询周报信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
    })
    public PageResult<WorkSummary> list(Integer page, Integer limit,String addTime) throws ParseException {
        String realName = userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName()).getRealName();
        if (page == null) {
            page = 1;
            limit = 10;
        }
        //计算总工时
        List<WorkSummary> workSummaryListHour = workSummaryService.list();
        for (WorkSummary workSummary:workSummaryListHour){
            int workHour = 0;
            List<TaskSummary> taskSummaryList = taskSummaryService.getByThree(0, workSummary.getUserName(), workSummary.getProName(), workSummary.getBeginDate(), workSummary.getEndDate());
            for (TaskSummary taskSummary:taskSummaryList){
                workHour = workHour+taskSummary.getTaskHour();
            }
            if (workHour!=0){
                workSummary.setWorkHour(workHour);
                workSummaryService.update(workSummary);
            }
        }
        return  workSummaryService.list2(page, limit, realName,addTime,null);
    }
    /**
     * 查询全部周报列表level3
     */
    @RequiresPermissions("worksummary:viewLevel3")
    @ResponseBody
    @RequestMapping("/list3")
    @ApiOperation(value = "查询审核通过的周报信息", notes = "查询审核通过的周报信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
    })
    public PageResult<WorkSummary> list1(Integer page, Integer limit,String addTime,String userName,String proName) throws ParseException {
        if (page == null) {
            page = 1;
            limit = 10;
        }
        //计算总工时
        List<WorkSummary> workSummaryListHour = workSummaryService.list();
        for (WorkSummary workSummary:workSummaryListHour){
            int workHour = 0;
            List<TaskSummary> taskSummaryList = taskSummaryService.getByThree(0, workSummary.getUserName(), workSummary.getProName(), workSummary.getBeginDate(), workSummary.getEndDate());
            for (TaskSummary taskSummary:taskSummaryList){
                workHour = workHour+taskSummary.getTaskHour();
            }
            if (workHour!=0){
                workSummary.setWorkHour(workHour);
                workSummaryService.update(workSummary);
            }
        }
        Map<String, WorkSummary> map = new HashMap<String, WorkSummary>();
        PageResult<WorkSummary> workSummaryPageResult = workSummaryService.list1(page, limit, null, addTime, userName, proName, 0, 0);
        List<WorkSummary> workSummaryList = workSummaryPageResult.getData();
        for (WorkSummary workSummary:workSummaryList){
            String key = workSummary.getId()+workSummary.getUserName();
            map.put(key,workSummary);
        }
        List<WorkSummary> workSummaryList1 = workSummaryService.getByuserName(userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName()).getRealName());
            for (WorkSummary workSummary:workSummaryList1){
            String key = workSummary.getId()+workSummary.getUserName();
            map.put(key,workSummary);
        }
        List<WorkSummary> workSummaryList2 = new ArrayList<WorkSummary>();
        for (Map.Entry<String, WorkSummary> entry : map.entrySet()) {
            workSummaryList2.add(entry.getValue());
        }
        workSummaryPageResult.setData(workSummaryList2);
        return workSummaryPageResult;
    }
    /**
     * 查询项目成员周报列表level4
     */
//    @RequiresPermissions("worksummary:viewLevel4")
//    @ResponseBody
//    @RequestMapping("/list4")
//    @ApiOperation(value = "查询本项目的周报信息", notes = "查询本项目的周报信息列表")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
//            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
//    })
//    public PageResult<WorkSummary> list2(Integer page, Integer limit,String addTime,String userName,String proName) throws ParseException {
//        if (page == null) {
//            page = 1;
//            limit = 10;
//        }
//        //计算总工时
//        List<WorkSummary> workSummaryListHour = workSummaryService.list();
//        for (WorkSummary workSummary:workSummaryListHour){
//            int workHour = 0;
//            List<TaskSummary> taskSummaryList = taskSummaryService.getByThree(0, workSummary.getUserName(), workSummary.getProName(), workSummary.getBeginDate(), workSummary.getEndDate());
//            for (TaskSummary taskSummary:taskSummaryList){
//                workHour = workHour+taskSummary.getTaskHour();
//            }
//            if (workHour!=0){
//                workSummary.setWorkHour(workHour);
//                workSummaryService.update(workSummary);
//            }
//        }
//        Map<String, WorkSummary> map = new HashMap<String, WorkSummary>();
//        String loginUserName = SystemCommonHandler.getLoginUserName();
//        User user1 = userService.getRealNameByLoginName(loginUserName);
//        String sectionPost = user1.getSectionPost();
//        List<WorkSummary> workSummaryList = new ArrayList<WorkSummary>();
//        List<WorkSummary> workSummaryList1 = new ArrayList<WorkSummary>();
//        if (sectionPost.equals("项目经理")&&sectionPost.length()>0){
//            List<Info> infoList = unfinishedProjectService.listInfoByManager(user1.getRealName());
//            for (Info info:infoList){
//                workSummaryList = workSummaryService.getByproName(info.getProName());
//                if(workSummaryList!=null && !workSummaryList.isEmpty()){
//                    for (WorkSummary workSummary:workSummaryList){
//                        String key = workSummary.getId()+workSummary.getUserName();
//                        map.put(key,workSummary);
//                    }
//                }
//            }
//        }
//        List<WorkSummary> workSummaries = workSummaryService.getByuserName(user1.getRealName());
//        for (WorkSummary workSummary:workSummaries){
//            String key = workSummary.getId()+workSummary.getUserName();
//            map.put(key,workSummary);
//        }
//        QueryWrapper<WorkSummary> wrapper = new QueryWrapper<WorkSummary>();
//        if (StringUtils.isNotBlank(addTime)) {
//            wrapper.like("add_time", addTime);
//        }
//        if (StringUtils.isNotBlank(userName)) {
//            wrapper.like("user_name", userName);
//        }
//        if (StringUtils.isNotBlank(proName)) {
//            wrapper.like("pro_name", proName);
//        }
//        List<WorkSummary> workSummaryList2 = workSummaryMapper.selectList(wrapper);
//        //取交集
//        if (workSummaryList2!=null&&workSummaryList2.isEmpty()){
//            for (WorkSummary workSummary:workSummaryList2){
//                String key = workSummary.getId()+workSummary.getUserName();
//                map.put(key,workSummary);
//            }
//        }
//        for (Map.Entry<String, WorkSummary> entry : map.entrySet()) {
//            workSummaryList1.add(entry.getValue());
//        }
//
//        if (StringUtils.isBlank(addTime)&&StringUtils.isBlank(userName)&&StringUtils.isBlank(proName)){
//            return new PageResult<WorkSummary>(PageResultUtils.paginAtion(page,limit,workSummaryList1),workSummaryList1.size());
//        }else {
//            return new PageResult<WorkSummary>(PageResultUtils.paginAtion(page,limit,workSummaryList2),workSummaryList2.size());
//        }
//    }



    /**
     * 添加个人工作周报
     **/
    @RequiresPermissions("worksummary:add")
    @ResponseBody
    @RequestMapping("/add")
    public JsonResult add(WorkSummary workSummary) throws ParseException {
        int status = 1;
        String sectionPost = userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName()).getSectionPost();
        if (sectionPost.equals("普通员工")){
            workSummary.setExamineStatus(status);
            workSummary.setCheckStatus(status);
            workSummary.setExamineStatusDept(status);
            workSummary.setCheckStatusDept(status);
        }else if (sectionPost.equals("项目经理")||sectionPost.equals("部门经理")){
            workSummary.setExamineStatus(0);
            workSummary.setCheckStatus(0);
            workSummary.setExamineStatusDept(status);
            workSummary.setCheckStatusDept(status);
        }
        if (workSummary.getProName().equals("其它项目")&&workSummary.getTaskType().equals("其它")){
            workSummary.setExamineStatus(0);
            workSummary.setCheckStatus(0);
            workSummary.setExamineStatusDept(status);
            workSummary.setCheckStatusDept(status);
        }
        if (workSummaryService.add(workSummary)){
            return JsonResult.ok("添加成功");
        }else {
            return JsonResult.error("添加失败：本周周报已添加！");
        }
    }
    /**
     * 添加评论
     **/
    @ResponseBody
    @RequestMapping("/addComment")
    public JsonResult addComment(int id,String comment) throws UnsupportedEncodingException {
        WorkSummary workSummary = new WorkSummary();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String Comment = "\r\n"+comment+"\r\n"+"------评论人："+userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName()).getRealName()+"\r\n"+sdf.format(new Date());
        if (comment.getBytes("utf-8").length>255){
            return JsonResult.error("字符过长溢出！");
        }else {
            workSummary.setId(id);
            workSummary.setComment(Comment);
            if (workSummaryService.addComment(workSummary)){
                return JsonResult.ok("评论成功");
            }else {
                return JsonResult.error("评论失败");
            }
        }
    }
    /**
     * 修改工作周报
     **/
    @RequiresPermissions("worksummary:update")
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(WorkSummary workSummary,String beforeComment){
        String sectionPost = userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName()).getSectionPost();
        if (sectionPost.equals("项目经理")){
            workSummary.setCheckStatus(0);
        }else if (sectionPost.equals("部门经理")){
            workSummary.setCheckStatusDept(0);
        }else {

        }
        if(workSummaryService.update(workSummary)){
            return JsonResult.ok("修改成功");
        }else {
            return JsonResult.error("修改失败");
        }
    }
    /**
     * 部门经理修改工作周报审核状态
     **/
    @RequiresPermissions("worksummary:viewLevel1")
    @ResponseBody
    @RequestMapping("/updateState1")
    @SysLog(operationType="update操作:",operationName="部门经理修改工作周报审核状态")
    public JsonResult updateState1(int id, Integer examineStatusDept) {
        WorkSummary workSummary = workSummaryService.queryById(id);
        int checkStatusDept = workSummary.getCheckStatusDept();
        int checkStatus = workSummary.getCheckStatus();
        int examineStatus = 0;
        if(checkStatusDept==1){
            return JsonResult.error("您还未查看！");
        }else {
            if (workSummaryService.updateState(id,examineStatus,examineStatusDept,checkStatus,0)) {
                return JsonResult.ok();
            } else {
                return JsonResult.error();
            }
        }
    }
    /**
    *部门经理一键审核
     **/
    @RequiresPermissions("worksummary:viewLevel1")
    @ResponseBody
    @RequestMapping("/updateState2")
    @SysLog(operationType="update操作:",operationName="部门经理一键审核周报审核状态")
    public JsonResult updateState2(@RequestBody List<WorkSummary> workSummaryList){
        List<WorkSummary> workSummaryList1 = new ArrayList<WorkSummary>();
        for (WorkSummary workSummary:workSummaryList){
            if (workSummary.getCheckStatusDept()==0){
                if (workSummary.getExamineStatusDept()==1){
                    workSummary.setExamineStatusDept(0);
                }else {
//                    workSummary.setExamineStatusDept(1);
                }
                workSummaryList1.add(workSummary);
            }
        }
        if (workSummaryList1.size()>0){
            if (workSummaryService.updateBatchById(workSummaryList1)){
                return JsonResult.ok("审核成功");
            }else {
                return JsonResult.error("审核失败");
            }
        }else {
            return JsonResult.error("当前选中均未查看，请先查看");
        }
    }
    /**
     *项目经理一键审核
     **/
    @RequiresPermissions("worksummary:viewLevel4")
    @ResponseBody
    @RequestMapping("/updateState3")
    @SysLog(operationType="update操作:",operationName="项目经理一键审核周报审核状态")
    public JsonResult updateState3(List<WorkSummary> workSummaryList){
        List<WorkSummary> workSummaryList1 = new ArrayList<WorkSummary>();
        for (WorkSummary workSummary:workSummaryList){
            if (workSummary.getCheckStatus()==0){
               if (workSummary.getExamineStatus()==1){
                   workSummary.setExamineStatus(0);
               }else {
                   workSummary.setExamineStatus(1);
               }
                workSummaryList1.add(workSummary);
            }
        }
        if (workSummaryList1.size()>0){
            if (workSummaryService.updateBatchById(workSummaryList1)){
                return JsonResult.ok("审核成功");
            }else {
                return JsonResult.error("审核失败");
            }
        }else {
            return JsonResult.error("当前选中均未查看，请先查看");
        }
    }

    /**
     * 项目经理修改工作周报审核状态
     **/
    @RequiresPermissions("worksummary:viewLevel4")
    @ResponseBody
    @RequestMapping("/updateState4")
    @SysLog(operationType="update操作:",operationName="项目经理修改工作周报审核状态")
    public JsonResult updateState4(int id, Integer examineStatus) {
        WorkSummary workSummary = workSummaryService.queryById(id);
        int checkStatus = workSummary.getCheckStatus();
        int checkStatusDept = workSummary.getCheckStatusDept();
        int examineStatusDept = 1;
        if(checkStatus==1){
            return JsonResult.error("您还未查看！");
        }else {
            if (workSummaryService.updateState(id, examineStatus, examineStatusDept,0,checkStatusDept)) {
                return JsonResult.ok();
            } else {
                return JsonResult.error();
            }
        }
    }
    /**
     * 删除工作周报
     **/
    @RequiresPermissions("worksummary:delete")
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult delete(int id,String userName) throws ParseException {
        if(userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName()).getRealName().equals(userName)){
            if (workSummaryService.delete(id)) {
                return JsonResult.ok();
            } else {
                return JsonResult.error();
            }
        }else {
            return JsonResult.error("无权限删除此周报！");
        }

    }
}
