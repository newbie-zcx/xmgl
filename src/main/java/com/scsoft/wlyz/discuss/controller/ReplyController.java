package com.scsoft.wlyz.discuss.controller;

import com.scsoft.scpt.annotation.SysLog;
import org.springframework.web.bind.annotation.*;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.scsoft.wlyz.discuss.service.IReplyService;
import com.scsoft.wlyz.discuss.entity.Reply;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scsoft.scpt.base.controller.BaseController;

import javax.servlet.http.HttpServletRequest;


/**
 * <p>
 * 回复表 前端控制器
 * </p>
 * @author wangchao
 * @CreateDate 2020-01-08
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @Version: 1.0
 */

@Controller
@RequestMapping("/discuss/reply")
public class ReplyController extends BaseController {
   private Logger logger = LoggerFactory.getLogger(getClass());

    private String PREFIX = "module/discuss/reply/";

    @Autowired
    private IReplyService replyService;

    /**
     * 跳转到回复表首页
     */
    @RequiresPermissions("reply:view")
    @RequestMapping("")
    public String index(Model model, HttpServletRequest request) {
        return PREFIX + "reply";
    }

    /**
     * 跳转到添加回复表
     */
    @RequestMapping("/replyadd")
    @RequiresPermissions("reply:add")
    public String toAdd(Model model,HttpServletRequest request) {
        return PREFIX + "reply_add.html";
    }

    /**
     * 跳转到修改回复表
     */
    @RequestMapping("/replyupdate")
     @RequiresPermissions("reply:update")
    public String toUpdate(Model model,HttpServletRequest request) {
        return PREFIX + "reply_edit.html";
    }


   /**
     * 回复表详情
     */
    @RequestMapping(value = "/detail/{replyId}")
    @ResponseBody
    public Object toDetail(@PathVariable("replyId") Integer replyId, Model model, HttpServletRequest request) {
        return replyService.getById(replyId);
    }

    /**
     * 获取回复表列表
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions("reply:view")
    @ResponseBody
    public PageResult<Reply> list(Integer page, Integer limit, Reply reply,String condition, Model model,HttpServletRequest request) {
        return replyService.listPage(page,limit,reply);
    }


    /**
     * 新增回复表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @RequiresPermissions("reply:add")
    @SysLog(operationType="add操作:",operationName="添加回复表")
    public JsonResult add(Reply reply) {
        if (replyService.save(reply)) {
            return JsonResult.ok("添加成功");
        } else {
            return JsonResult.error("添加失败");
        }
    }

}
