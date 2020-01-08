package com.scsoft.wlyz.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scsoft.wlyz.system.entity.Dict;
import com.scsoft.wlyz.system.entity.DictGroup;
import com.scsoft.wlyz.system.service.IDictGroupService;
import com.scsoft.scpt.annotation.SysLog;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 字典分组 前端控制器
 * </p>
 * @author zhaopengfei
 * @CreateDate 2019-04-26
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @Version: 1.0
 */
@Controller
@RequestMapping("/system/dictGroup")
public class DictGroupController extends BaseController {
    private String PREFIX = "module/system/dictGroup/";

    @Autowired
    private IDictGroupService dictGroupService;


    /**
     * 跳转到字典分组首页
     */

    @RequestMapping("")
    public String index(Model model, HttpServletRequest request) {
        return PREFIX + "dictGroup";
    }

    /**
     * 跳转到添加字典组页面
     */ @RequestMapping("/dictGroup_add")

    public String dictGroupAdd(Model model,HttpServletRequest request) {

        return PREFIX + "dictGroup_add";
    }




    // 跳转查看详情
    @RequestMapping(value = "/dict_show")
    public String  dictshow(Integer gid, Model model,HttpServletRequest request) {
        model.addAttribute("gid", gid);
        return PREFIX + "dictGroup_show";
    }


    /**
     * 两个表查询
     * @param gid
     * @param page
     * @param limit
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/dictlist")
    @ResponseBody
    public  PageResult  edit(Integer gid, Integer page, Integer limit,Model model,HttpServletRequest request) {
        DictGroup dictGroup = dictGroupService.findById(gid);
      List<Dict> dicts = dictGroup.getDicts();
      PageResult<DictGroup> pageResult=new PageResult<>();
        pageResult.setCode(0);
        List<DictGroup> list=new ArrayList<>();
        list.add(dictGroup);
        pageResult.setData(list);
        return pageResult;
    }
    /**
     * 获取字典分组列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public PageResult<DictGroup> list(Integer page, Integer limit, DictGroup dictGroup,String code, Model model,HttpServletRequest request) {
        Map<String,Object> map=new HashMap<String,Object>();
        if (page==null){
            page=1;
            limit=10;
        }
        if (StringUtils.isBlank(code)){
            code=null;
        }
        Page<DictGroup> mypage = new Page<DictGroup>(page, limit);
        PageResult all = dictGroupService.findAll(mypage, code);
        return all;
    }


    /**
     * 新增字典分组
     */
    @RequestMapping(value = "/add")
    @ResponseBody

    @SysLog(operationType="add操作:",operationName="添加字典分组")
    public JsonResult add(DictGroup dictGroup) {
        if (dictGroupService.save(dictGroup)) {
            return JsonResult.ok("添加成功");
        } else {
            return JsonResult.error("添加失败");
        }

    }

    /**
     * 删除字典分组
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @SysLog(operationType="delete操作:",operationName="删除字典分组")
    public JsonResult delete(int dicgroupid) {
        if (dictGroupService.removeById(dicgroupid)) {
            return JsonResult.ok("删除成功");
        } else {
            return JsonResult.error("删除失败");
        }
    }

    /**
     * 逻辑删除字典分组
     */
    @RequestMapping(value = "/logicDelete")
    @ResponseBody

    @SysLog(operationType="delete操作:",operationName="逻辑删除字典分组")
    public JsonResult logicDelete(@RequestParam Integer dictGroupId) {
        DictGroup dictGroup=dictGroupService.getById(dictGroupId);
        if (dictGroupService.logicDelete(dictGroup)) {
            return JsonResult.ok("删除成功");
        } else {
            return JsonResult.error("删除失败");
        }

    }


    /**
     * 修改字典分组
     */
    @RequestMapping(value = "/update")
    @ResponseBody

    @SysLog(operationType="update操作:",operationName="修改字典分组")
    public JsonResult update(DictGroup dictGroup) {
        if (dictGroupService.updateById(dictGroup)) {
            return JsonResult.ok("修改成功");
        } else {
            return JsonResult.error("修改失败");
        }
    }


    @RequestMapping("/batchDelete")
    @ResponseBody

    @SysLog(operationType="delete操作:",operationName="批量删除字典分组")
    public JsonResult batchDelete(String departIds) {
        System.out.println(departIds);
        List<String> lis = Arrays.asList(departIds.split(","));
//        if (departService.removeByIds(lis)) {
//            return JsonResult.ok("删除成功");
//        }
        return JsonResult.error("删除失败");
    }

}
