package com.scsoft.wlyz.discuss.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.scsoft.scpt.annotation.SysLog;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.wlyz.common.handler.SystemCommonHandler;
import com.scsoft.wlyz.discuss.entity.Issue;
import com.scsoft.wlyz.discuss.model.IssueModel;
import com.scsoft.wlyz.discuss.service.IIssueService;
import com.scsoft.wlyz.system.entity.Depart;
import com.scsoft.wlyz.system.entity.User;
import com.scsoft.wlyz.system.service.IDepartService;
import com.scsoft.wlyz.system.service.IUserService;
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
 *  前端控制器  紧急议题
 * </p>
 * @author chengshangshu
 * @CreateDate 2020-01-09
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @Version: 1.0
 */

@Controller
@RequestMapping("/discuss/issueUrgent")
public class IssueUrgentController extends BaseController {
   private Logger logger = LoggerFactory.getLogger(getClass());

    private String PREFIX = "/module/discuss/issue/";

    @Autowired
    private IIssueService issueService;
    @Autowired
    private IDepartService departService;
    @Autowired
    private IUserService userService;
    /**
     * 跳转到首页
     */
    @RequiresPermissions("issueUrgent:view")
    @RequestMapping("")
    public String index(Model model,HttpServletRequest request) {
        return PREFIX + "discussissue_urgent";
    }


    /**
     * 获取紧急列表
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions("issueUrgent:view")
    @ResponseBody
    public PageResult<Issue> listHost(Integer page, Integer limit, Issue issue,String condition, Model model,HttpServletRequest request) {
        return issueService.listPage(page,limit,issue);
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/issueadd")
    @RequiresPermissions("issue:add")
    public String toAdd(Model model,HttpServletRequest request) {
        String id= IdWorker.getIdStr();
        model.addAttribute("id", id);

        //查询要发送部门
        int userId= SystemCommonHandler.getLoginUserId();
        String userName=SystemCommonHandler.getLoginUserName();
        List<Depart> departList=departService.getByUserId(userId);
        Depart depart=departList.get(0);//当前登录人的部门
        QueryWrapper<Depart> query = new QueryWrapper<Depart>();
//        if (depart.getId().equals("1")){
//            query.in("parent_id", "34");//.and(wapper -> wapper.ne("ADMINISTRATIVE_DIVISION",depart.getAdministrativeDivision()));
//        }else {
        query.in("parent_id", depart.getId());//.and(wapper -> wapper.ne("ADMINISTRATIVE_DIVISION",depart.getAdministrativeDivision()));
        String LoginDepart=depart.getDepartName();//当前登陆人部门
        int LoginDepartID=depart.getId();
        //}
        List<Depart> departList1=departService.list(query);
        model.addAttribute("userName", userName);
        model.addAttribute("userId", userId);
        model.addAttribute("departList", departList1);
        model.addAttribute("LoginDepart", LoginDepart);
        model.addAttribute("LoginDepartID", LoginDepartID);
        List<User> users = userService.list();
        model.addAttribute("users", users);
        return PREFIX + "discussissue_add";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/issueupdate")
     @RequiresPermissions("issue:update")
    public String toUpdate(Model model, HttpServletRequest request) {
        return PREFIX + "discussissue_edit";
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/view/{issueId}")
    public String toDetailView(@PathVariable("issueId") Integer issueId, Model model, HttpServletRequest request) {
        model.addAttribute("issueId", issueId);
        return PREFIX + "discussissue_view";
    }




   /**
     * 详情
     */
    @RequestMapping(value = "/detail")
    public Object toDetail(Integer issueId, Model model, HttpServletRequest request) {
        int a=1;
        model.addAttribute("issue",issueService.getById(issueId));
        return PREFIX + "discussissue_view";
    }






/**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @RequiresPermissions("issue:add")
    @SysLog(operationType="add操作:",operationName="添加")
    public JsonResult add(IssueModel issue) {
        if (issueService.saveModel(issue)) {
                return JsonResult.ok("添加成功");
        } else {
            return JsonResult.error("添加失败");
        }

    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @RequiresPermissions("issue:delete")
    @SysLog(operationType="delete操作:",operationName="删除")
    public JsonResult delete(@RequestParam Integer issueId) {
         if (issueService.removeById(issueId)) {
                  return JsonResult.ok("删除成功");
          } else {
              return JsonResult.error("删除失败");
          }
    }

    /**
     * 逻辑删除
     */
    @RequestMapping(value = "/logicDelete")
    @ResponseBody
    @RequiresPermissions("issue:delete")
    @SysLog(operationType="delete操作:",operationName="逻辑删除")
    public JsonResult logicDelete(@RequestParam Integer issueId) {
    Issue issue=issueService.getById(issueId);
       if (issueService.logicDelete(issue)) {
           return JsonResult.ok("删除成功");
       } else {
           return JsonResult.error("删除失败");
       }
    }


    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @RequiresPermissions("issue:update")
    @SysLog(operationType="update操作:",operationName="修改")
    public JsonResult update(Issue issue) {
       if (issueService.updateById(issue)) {
           return JsonResult.ok("修改成功");
       } else {
           return JsonResult.error("修改失败");
       }
    }


    @RequestMapping("/batchDelete")
    @ResponseBody
    @RequiresPermissions("issue:delete")
    @SysLog(operationType="delete操作:",operationName="批量删除")
    public JsonResult batchDelete(String departIds) {
        System.out.println(departIds);
        List<String> lis = Arrays.asList(departIds.split(","));
        if (issueService.removeByIds(lis)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

}

