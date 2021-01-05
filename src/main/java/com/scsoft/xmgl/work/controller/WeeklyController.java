package com.scsoft.xmgl.work.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scsoft.scpt.annotation.SysLog;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.common.handler.SystemCommonHandler;
import com.scsoft.xmgl.common.utils.DateUtils;
import com.scsoft.xmgl.work.entity.*;
import com.scsoft.xmgl.work.mapper.ProUserMapper;
import com.scsoft.xmgl.work.mapper.SCMapper;
import com.scsoft.xmgl.work.mapper.WeeklyContentMapper;
import com.scsoft.xmgl.work.service.IProjectService;
import com.scsoft.xmgl.work.service.ISCService;
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
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/work/weekly")
public class WeeklyController extends BaseController {

    @Autowired
    private IWeeklyService weeklyService;
    @Autowired
    private IProjectService projectService;
    @Autowired
    private ISCService scService;
    @Resource
    private WeeklyContentMapper weeklyContentMapper;
    @Resource
    private SCMapper scMapper;
    @Resource
    private ProUserMapper proUserMapper;

    private final String PREFIX="module/work";

    @RequestMapping
    public String toIndex(){
        return PREFIX+"/weekly";
    }

    @RequiresPermissions("weekly:add")
    @RequestMapping("/addForm")
    public String addForm(Model model){
        List<ProUser> proUserList = proUserMapper.selectByUserId(SystemCommonHandler.getLoginUserId());
        List<Project> projectList = new ArrayList<Project>();
        for (int i = 0;i<proUserList.size();i++){
            Integer proId = proUserList.get(i).getProId();
            Project project = projectService.getById(proId);
            if (project.getProEndState()==0){
                projectList.add(project);
            }
        }
        model.addAttribute("projectList",projectList);
        return PREFIX+"/addWeeklyForm";
    }

    @RequiresPermissions("weekly:edit")
    @RequestMapping("/editForm")
    public String editForm(Model model){
        List<Project> projectList = projectService.list();
        model.addAttribute("projectList",projectList);
        return PREFIX+"/editWeeklyForm";
    }

    @RequiresPermissions("weekly:view")
    @ResponseBody
    @RequestMapping("/list")
    @ApiOperation(value = "查看所有项目列表->周报",notes = "查看项目信息列表->周报")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
    })
    public PageResult<Weekly> list(Integer page, Integer limit,Integer proId,String startDate,String endDate) throws ParseException {
        if (page == null){
            page = 1;
            limit = 15;
        }
        return weeklyService.list(page,limit,proId,startDate,endDate);
    }


    /**
    * 添加周报
    **/
    @RequiresPermissions("weekly:add")
    @ResponseBody
    @RequestMapping("/add")
    @SysLog(operationType = "add操作:",operationName = "添加周报各个信息")
    public JsonResult add(Weekly weekly,String proName,String content) throws ParseException {
        weekly.setUserName(SystemCommonHandler.getLoginUser().getRealName());
        Map<String, String> map = DateUtils.getWeekDate();
        weekly.setStartDate(map.get("Saturday"));
        weekly.setEndDate(map.get("Friday"));
        SC sc = new SC();
        sc.setContent(content);
        int scId = scService.add(sc);
        Project project = projectService.getOne(new QueryWrapper<Project>().eq("pro_name", proName));
        Integer proId = null;
        if (project.getId()!=null){
            proId = project.getId();
        }
        if (weeklyService.add(weekly,scId,proId)){
            return JsonResult.ok("添加成功");
        }else {
            return JsonResult.error("添加失败");
        }
    }

    /**
     * 修改周报
     **/
    @RequiresPermissions("weekly:edit")
    @ResponseBody
    @RequestMapping("/update")
    @SysLog(operationType = "update:",operationName = "更新周报各个信息")
    public JsonResult update(Weekly weekly,int proId,String scList) throws ParseException {
        List<WeeklyContent> contents = weeklyContentMapper.selectList(new QueryWrapper<WeeklyContent>().eq("weekly_id", weekly.getId()));
        if (contents.size()>0){
            for (int j = 0;j<contents.size();j++){
                scMapper.deleteById(contents.get(j).getContentId());
            }
        }
        weeklyContentMapper.deleteByWeeklyId(weekly.getId());
        SC scEntity = new SC();
        scEntity.setContent(scList);
        int scId = scService.add(scEntity);
        if (weeklyService.update(weekly,scId,proId)){
            return JsonResult.ok("修改成功");
        }else {
            return JsonResult.error("修改失败");
        }
    }
    /**
     * 删除周报
     **/
    @RequiresPermissions("weekly:delete")
    @ResponseBody
    @RequestMapping("/delete")
    @SysLog(operationType = "delete:",operationName = "删除周报各个信息")
    public JsonResult delete(int weeklyId){
        if (weeklyService.delete(weeklyId)){
            return JsonResult.ok("删除成功");
        }else {
            return JsonResult.error("删除失败");
        }
    }
}
