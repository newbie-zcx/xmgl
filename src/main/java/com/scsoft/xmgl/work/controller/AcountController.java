package com.scsoft.xmgl.work.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scsoft.scpt.annotation.SysLog;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.common.handler.SystemCommonHandler;
import com.scsoft.xmgl.common.utils.DateUtils;
import com.scsoft.xmgl.work.entity.*;
import com.scsoft.xmgl.work.mapper.ACMapper;
import com.scsoft.xmgl.work.mapper.AcountContentMapper;
import com.scsoft.xmgl.work.service.IACService;
import com.scsoft.xmgl.work.service.IAcountService;
import com.scsoft.xmgl.work.service.IProjectService;
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
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/work/managerAcount")
public class AcountController extends BaseController {

    @Autowired
    private IAcountService acountService;
    @Autowired
    private IProjectService projectService;
    @Autowired
    private IACService acService;
    @Resource
    private AcountContentMapper acountContentMapper;
    @Resource
    private ACMapper acMapper;

    private final String PREFIX="module/work";


    @RequestMapping
    public String toIndex(Model model){
        List<Project> projectList = projectService.list();
        model.addAttribute("projectList",projectList);
        return PREFIX+"/acount";
    }

    @RequiresPermissions("managerAcount:add")
    @RequestMapping("/addForm")
    public String addForm(Model model){
        String realName = SystemCommonHandler.getLoginUser().getRealName();
        List<Project> projectList = projectService.list(new QueryWrapper<Project>().eq("pro_manager", realName));
        model.addAttribute("projectList",projectList);
        return PREFIX+"/addAcountForm";
    }

    @RequiresPermissions("managerAcount:edit")
    @RequestMapping("/editForm")
    public String editForm(Model model){
        String realName = SystemCommonHandler.getLoginUser().getRealName();
        List<Project> projectList = projectService.list(new QueryWrapper<Project>().eq("pro_manager", realName));
        model.addAttribute("projectList",projectList);
        return PREFIX+"/editAcountForm";
    }


    @RequiresPermissions("managerAcount:view")
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

    @RequiresPermissions("managerAcount:add")
    @ResponseBody
    @RequestMapping("/add")
    @SysLog(operationType = "add操作:",operationName = "添加台账信息")
    public JsonResult add(Acount acount,int proId, String thisSummary,String nextSummary) throws ParseException {
        Integer thisId = null;
        Integer nextId = null;
        if (StringUtils.isNotBlank(thisSummary)){
            AC acThisEntity = new AC();
            acThisEntity.setThisSummary(thisSummary);
            thisId = acService.add(acThisEntity);
        }
        if (StringUtils.isNotBlank(nextSummary)){
            AC acNextEntity = new AC();
            acNextEntity.setNextSummary(nextSummary);
            nextId = acService.add(acNextEntity);
        }
        Map<String, String> map = DateUtils.getWeekDate();
        acount.setStartDate(map.get("mondayDate"));
        acount.setEndDate(map.get("sundayDate"));
        acount.setState(0);
        if (acountService.add(acount,thisId,nextId,proId)){
            return JsonResult.ok("添加成功");
        }else {
            return JsonResult.error("添加失败");
        }
    }

    @RequiresPermissions("managerAcount:edit")
    @ResponseBody
    @RequestMapping("/update")
    @SysLog(operationType = "update操作",operationName = "修改台账信息")
    public JsonResult update(Acount acount,int proId,String thisSummary,String nextSummary){
        List<AcountContent> acountContents = acountContentMapper.getByAcountId(acount.getId());
        AC ac = new AC();
        for (int i = 0;i<acountContents.size();i++){
            Integer contentId = acountContents.get(i).getContentId();
            ac.setId(contentId);
            if (contentId!=null){
                if (StringUtils.isNotBlank(thisSummary)){
                    ac.setThisSummary(thisSummary);
                    acMapper.updateById(ac);
                }else{
                    ac.setNextSummary(nextSummary);
                    acMapper.updateById(ac);
                }
            }else {
                return JsonResult.error("修改失败");
            }
        }
        if (acountService.update(acount)){
            return JsonResult.ok("修改成功");
        }else{
            return JsonResult.error("修改失败");
        }
    }

    @RequiresPermissions("managerAcount:delete")
    @ResponseBody
    @RequestMapping("/delete")
    @SysLog(operationType = "delete操作",operationName = "删除台账信息，并删除关联表")
    public JsonResult delete(int acountId){
        boolean result = acountService.delete(acountId);
        if (result){
            return JsonResult.ok("删除成功");
        }else{
            return JsonResult.error("删除失败");
        }
    }
}
