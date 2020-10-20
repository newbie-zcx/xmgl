package com.scsoft.xmgl.work.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scsoft.scpt.annotation.SysLog;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.common.handler.SystemCommonHandler;
import com.scsoft.xmgl.common.utils.DateUtils;
import com.scsoft.xmgl.work.entity.AC;
import com.scsoft.xmgl.work.entity.Acount;
import com.scsoft.xmgl.work.entity.Project;
import com.scsoft.xmgl.work.entity.Weekly;
import com.scsoft.xmgl.work.service.IACService;
import com.scsoft.xmgl.work.service.IAcountService;
import com.scsoft.xmgl.work.service.IProjectService;
import com.scsoft.xmgl.work.service.ISCService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/work/acount")
public class AcountController extends BaseController {

    @Autowired
    private IAcountService acountService;
    @Autowired
    private IProjectService projectService;
    @Autowired
    private IACService acService;

    private final String PREFIX="module/work";


    @RequestMapping
    public String toIndex(Model model){
        List<Project> projectList = projectService.list();
        model.addAttribute("projectList",projectList);
        return PREFIX+"/acount";
    }

    @RequiresPermissions("acount:add")
    @RequestMapping("/addForm")
    public String addForm(Model model){
        String realName = SystemCommonHandler.getLoginUser().getRealName();
        List<Project> projectList = projectService.list(new QueryWrapper<Project>().eq("pro_manager", realName));
        model.addAttribute("projectList",projectList);
        return PREFIX+"/addAcountForm";
    }

    @RequiresPermissions("acount:edit")
    @RequestMapping("/editForm")
    public String editForm(Model model){
        String realName = SystemCommonHandler.getLoginUser().getRealName();
        List<Project> projectList = projectService.list(new QueryWrapper<Project>().eq("pro_manager", realName));
        model.addAttribute("projectList",projectList);
        return PREFIX+"/editAcountForm";
    }


    @RequiresPermissions("acount:view")
    @ResponseBody
    @RequestMapping("/list")
    @ApiOperation(value = "查看所有台账",notes = "查看台账信息列表")
    public PageResult<Acount> list(Integer page,Integer limit,String proId,String startDate,String endDate) throws ParseException {
        if (page == null){
            page = 1;
            limit = 15;
        }
        return acountService.list(page,limit,proId,startDate,endDate);
    }

    @RequiresPermissions("acount:add")
    @ResponseBody
    @RequestMapping("/add")
    @SysLog(operationType = "add操作:",operationName = "添加台账各个信息")
    public JsonResult add(Acount acount,int proId, String acThis,String acNext) throws ParseException {
        String[] acThisList = acThis.split("-");
        String[] acNextList = acNext.split("-");
        List<Integer> acThisIdList = new ArrayList<Integer>();
        List<Integer> acNextIdList = new ArrayList<Integer>();
        for (int i = 1;i<acThisList.length;i++){
            if (StringUtils.isNotBlank(acThisList[i])){
                AC acThisEntity = new AC();
                acThisEntity.setThisSummary(acThisList[i]);
                int thisId = acService.add(acThisEntity);
                acThisIdList.add(thisId);
            }
        }
        for (int j = 1;j<acNextList.length;j++){
            if (StringUtils.isNotBlank(acNextList[j])){
                AC acNextEntity = new AC();
                acNextEntity.setNextSummary(acNextList[j]);
                int nextId = acService.add(acNextEntity);
                acNextIdList.add(nextId);
            }
        }
        Map<String, String> map = DateUtils.getWeekDate();
        acount.setStartDate(map.get("mondayDate"));
        acount.setEndDate(map.get("sundayDate"));
        if (acountService.add(acount,acThisIdList,acNextIdList,proId)){
            return JsonResult.ok("添加成功");
        }else {
            return JsonResult.error("添加失败");
        }
    }



}
