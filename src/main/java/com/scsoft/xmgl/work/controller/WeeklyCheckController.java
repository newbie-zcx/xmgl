package com.scsoft.xmgl.work.controller;

import com.scsoft.scpt.annotation.SysLog;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.common.handler.SystemCommonHandler;
import com.scsoft.xmgl.common.utils.DateUtils;
import com.scsoft.xmgl.system.entity.Depart;
import com.scsoft.xmgl.system.entity.Role;
import com.scsoft.xmgl.system.entity.User;
import com.scsoft.xmgl.system.service.IDepartService;
import com.scsoft.xmgl.system.service.IRoleService;
import com.scsoft.xmgl.system.service.IUserService;
import com.scsoft.xmgl.work.entity.ProWeekly;
import com.scsoft.xmgl.work.entity.Project;
import com.scsoft.xmgl.work.entity.Weekly;
import com.scsoft.xmgl.work.mapper.ProWeeklyMapper;
import com.scsoft.xmgl.work.mapper.WeeklyMapper;
import com.scsoft.xmgl.work.service.IProjectService;
import com.scsoft.xmgl.work.service.IUpdateLogService;
import com.scsoft.xmgl.work.service.IWeeklyService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/work/check")
public class WeeklyCheckController extends BaseController {

    @Autowired
    private IProjectService projectService;
    @Autowired
    private IWeeklyService weeklyService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IDepartService departService;
    @Autowired
    private IUpdateLogService updateLogService;
    @Resource
    private ProWeeklyMapper proWeeklyMapper;
    @Resource
    private WeeklyMapper weeklyMapper;

    private final String PREFIX="module/work";

    @RequestMapping
    public String toIndex(Model model) {
        List<User> userList = userService.list();
        List<User> nameList = new ArrayList<User>();
        Role role = new Role();
        for (int i = 0;i<userList.size();i++){
            List<Role> roleList = roleService.getByUserId(userList.get(i).getId());
            if (roleList.get(0)==null){
                continue;
            }else {
                role = roleList.get(0);
            }
            if (StringUtils.isBlank(role.getName())){
            }else if (role.getName().equals("项目经理")&&StringUtils.isNotBlank(role.getName())){
                nameList.add(userList.get(i));
            }else {
            }
        }
        if (nameList.size()!=0){
            model.addAttribute("nameList",nameList);
        }else {
            model.addAttribute("nameList","无项目经理");
        }
        Integer userId = SystemCommonHandler.getLoginUser().getId();
        List<Depart> userDepart = departService.getByUserId(userId);
        Depart depart = userDepart.get(0);
        List<Depart> departList = new ArrayList<Depart>();
        if (depart.getDepartLevel().equals("3")){
            departList = departService.getByPId(depart.getParentId());
        }else if (depart.getDepartLevel().equals("2")){
            departList = departService.getByPId(depart.getId());
        }else {
            //部门等级为1的操作
        }
        model.addAttribute("departList",departList);
        return PREFIX+"/weeklyCheck";
    }

    @RequiresPermissions("check:view")
    @ResponseBody
    @RequestMapping("/list")
    @ApiOperation(value = "查看项目以审核周报",notes = "查看项目以审核周报")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "第几页",required = true,dataType = "Integer",paramType = "form"),
            @ApiImplicitParam(name = "limit",value = "每页多少条",required = true,dataType = "Integer",paramType = "form"),
    })
    public PageResult<Project> list(Integer page, Integer limit,String proId,String proName,String proManager,String manager,String proDept,String proSigner,Integer proEndState) throws ParseException {
        if (page == null){
            page = 1;
            limit = 5;
        }
        PageResult<Project> projectPageResult = projectService.list(page, limit,proId,proName,proManager,manager,proDept,proSigner,proEndState);
        List<Project> projectList = projectPageResult.getData();
        for (int i = 0;i<projectList.size();i++) {
            Project project = projectList.get(i);
            Integer ProId = project.getId();
            List<ProWeekly> proWeeklyList = proWeeklyMapper.getByProId(ProId);
            List<Weekly> weeklyList = weeklyService.getListById(proWeeklyList);
            int nowMonth = DateUtils.getMonth();
            int lastMonthHour = 0;
            int thisMonthHour = 0;
            Map<String, String> map = DateUtils.getWeekDate();
            String mondayDate = map.get("mondayDate");
            String sundayDate = map.get("sundayDate");
            if(weeklyList.size()>0){
                for (int j = 1;j<weeklyList.size();j++) {
                    Weekly weekly = weeklyList.get(j);
                    String start = weekly.getStartDate();
                    Date startDate = DateUtils.StringToDate(start);
                    start = DateUtils.DateToString(startDate);
                    String end = weekly.getEndDate();
                    Date endDate = DateUtils.StringToDate(end);
                    end = DateUtils.DateToString(endDate);
                    int startMonth = DateUtils.getMonthByDate(startDate);
                    int endMonth = DateUtils.getMonthByDate(endDate);
                    if (startMonth<nowMonth&&endMonth==nowMonth){
                        lastMonthHour += weekly.getLastMonthHour();
                        thisMonthHour += weekly.getThisMonthHour();
                    }else if (startMonth==nowMonth&&endMonth==nowMonth){
                        if (start.equals(mondayDate)&&end.equals(sundayDate)){
                            thisMonthHour += weekly.getThisMonthHour();
                        }
                    }
                }
            }
            project.setLastMonthHour(lastMonthHour);
            project.setThisMonthHour(thisMonthHour);
        }
        return projectPageResult;
    }

    /**
     * 审核周报
     **/
    @RequiresPermissions("check:edit")
    @ResponseBody
    @RequestMapping("/updateHour")
    @SysLog(operationType = "update:",operationName = "更新周报工时信息")
    public JsonResult checkWeekly(int entityId, String beforeParams, String afterParams, String updateTable, String updateRow, String updateReason) throws ClassNotFoundException {
        Weekly weekly = weeklyMapper.selectById(entityId);
        if (updateRow.equals("lastMonthHour")){
            weekly.setLastMonthHour(Integer.parseInt(afterParams));
        }else if (updateRow.equals("thisMonthHour")){
            weekly.setThisMonthHour(Integer.parseInt(afterParams));
        }
        updateLogService.add(entityId,beforeParams,afterParams,updateTable,updateRow,updateReason);
        if (weeklyService.updateHour(weekly)){
            return JsonResult.ok("修改成功");
        }else {
            return JsonResult.error("修改失败");
        }
    }
}
