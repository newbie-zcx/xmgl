package com.scsoft.xgsb.yqsb.controller;

import com.scsoft.scpt.annotation.SysLog;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xgsb.yqsb.entity.Detail;
import com.scsoft.xgsb.yqsb.service.IDetailService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;


/**
 * <p>
 * 疫情上报详情 前端控制器
 * </p>
 * @author 赵鹏飞
 * @CreateDate 2020-01-31
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @Version: 1.0
 */

@Controller
@RequestMapping("/yqsb/detail")
public class DetailController extends BaseController {
   private Logger logger = LoggerFactory.getLogger(getClass());

    private String PREFIX = "module/yqsb/";

    @Autowired
    private IDetailService detailService;

    /**
     * 跳转到疫情上报详情首页
     */
    @RequiresPermissions("detail:view")
    @RequestMapping("")
    public String index(Model model, HttpServletRequest request) {
        return PREFIX + "detail";
    }

    /**
     * 跳转到疫情上报详情首页
     */
    @RequiresPermissions("detail:view")
    @RequestMapping("todaydetail")
    public String todaydetail(Model model,String type, HttpServletRequest request) {
        return PREFIX + "detail";
    }
    /**
     * 跳转到添加疫情上报详情
     */
    @RequestMapping("/detailadd")
    public String toAdd(Model model,HttpServletRequest request) {
        model.addAttribute("1","1");
        return PREFIX + "detail_add.html";
    }

    /**
     * 跳转到修改疫情上报详情
     */
    @RequestMapping("/detailupdate")
     @RequiresPermissions("detail:update")
    public String toUpdate(Model model,HttpServletRequest request) {
        return PREFIX + "detail_edit.html";
    }


   /**
     * 疫情上报详情详情
     */
    @RequestMapping(value = "/detail/{detailId}")
    @ResponseBody
    public Object toDetail(@PathVariable("detailId") Integer detailId, Model model, HttpServletRequest request) {
        return detailService.getById(detailId);
    }

    /**
     * 获取疫情上报详情列表
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions("detail:view")
    @ResponseBody
    public PageResult<Detail> list(Integer page, Integer limit, Detail detail,String condition, Model model,HttpServletRequest request) {
        return detailService.listPage(page,limit,detail);
    }


/**
     * 新增疫情上报详情
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @RequiresPermissions("detail:add")
    @SysLog(operationType="add操作:",operationName="添加疫情上报详情")
    public JsonResult add(Detail detail) {
        if (detailService.save(detail)) {
                return JsonResult.ok("添加成功");
        } else {
            return JsonResult.error("添加失败");
        }

    }

    /**
     * 删除疫情上报详情
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @RequiresPermissions("detail:delete")
    @SysLog(operationType="delete操作:",operationName="删除疫情上报详情")
    public JsonResult delete(@RequestParam Integer detailId) {
         if (detailService.removeById(detailId)) {
                  return JsonResult.ok("删除成功");
          } else {
              return JsonResult.error("删除失败");
          }
    }

    /**
     * 逻辑删除疫情上报详情
     */
    @RequestMapping(value = "/logicDelete")
    @ResponseBody
    @RequiresPermissions("detail:delete")
    @SysLog(operationType="delete操作:",operationName="逻辑删除疫情上报详情")
    public JsonResult logicDelete(@RequestParam Integer detailId) {
    Detail detail=detailService.getById(detailId);
       if (detailService.logicDelete(detail)) {
           return JsonResult.ok("删除成功");
       } else {
           return JsonResult.error("删除失败");
       }
    }


    /**
     * 修改疫情上报详情
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @RequiresPermissions("detail:update")
    @SysLog(operationType="update操作:",operationName="修改疫情上报详情")
    public JsonResult update(Detail detail) {
       if (detailService.updateById(detail)) {
           return JsonResult.ok("修改成功");
       } else {
           return JsonResult.error("修改失败");
       }
    }


    @RequestMapping("/batchDelete")
    @ResponseBody
    @RequiresPermissions("detail:delete")
    @SysLog(operationType="delete操作:",operationName="批量删除疫情上报详情")
    public JsonResult batchDelete(String departIds) {
        System.out.println(departIds);
        List<String> lis = Arrays.asList(departIds.split(","));
        if (detailService.removeByIds(lis)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

}