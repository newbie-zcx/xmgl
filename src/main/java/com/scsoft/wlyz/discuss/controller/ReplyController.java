package com.scsoft.wlyz.discuss.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scsoft.scpt.annotation.SysLog;
import com.scsoft.wlyz.discuss.entity.Issue;
import com.scsoft.wlyz.discuss.model.ReplyModel;
import com.scsoft.wlyz.discuss.service.IIssueService;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;


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
@Slf4j
@Controller
@RequestMapping("/discuss/reply")
public class ReplyController extends BaseController {
   private Logger logger = LoggerFactory.getLogger(getClass());

    private String PREFIX = "module/discuss/reply/";

    @Autowired
    private IReplyService replyService;

    @Autowired
    private IIssueService issueService;

    /**
     * 跳转到回复表首页
     */
    @RequestMapping("")
    public String index(Model model,Integer issueId, Integer toReplyUserId, String toReplyUserName, HttpServletRequest request) {
        /*议题测试数据，需要修改*/
        model.addAttribute("issueId", issueId);
        Issue issue = issueService.getById(issueId);
        model.addAttribute("issueName", issue.getCreateName());
        model.addAttribute("toReplyUserId", issue.getCreateId());
        return PREFIX + "reply_main";
    }

    /**
     * 跳转到添加回复表
     */
    @RequestMapping("/reply_add")
    public String toAdd(Model model,Integer id, Integer issueId, HttpServletRequest request) {
        if(null != id){
            Reply reply = replyService.getById(id);
            model.addAttribute("id", id);
            model.addAttribute("issueId", reply.getIssueId());
            model.addAttribute("toReplyUserId",reply.getCreateId());
            model.addAttribute("toReplyUserName", reply.getCreateName());
        }else {
            model.addAttribute("issueId", issueId);
        }
        return PREFIX + "reply_add";
    }

    @RequestMapping("/reply_more")
    public String replyMore(Model model,Integer id, HttpServletRequest request) {
        model.addAttribute("toReplyId", id);
        return PREFIX + "reply_more";
    }

    /**
     * 跳转到修改回复表
     */
    @RequestMapping("/replyupdate")
    public String toUpdate(Model model,HttpServletRequest request) {
        return PREFIX + "reply_edit";
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
     * 获取回复表列表，根据议题ID，不分页
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public PageResult<Reply> list(Reply reply, String condition, Model model, HttpServletRequest request) {
        PageResult<Reply> result = new PageResult<>();
        if(null == reply || null == reply.getIssueId()){
            result.setCode(500);
        }
        List<Reply> list = replyService.selectListByIssue(reply);
        result.setData(list);
        result.setCount(list.size());
        result.setCode(200);
        return result;
    }

    /**
     * 获取回复表列表，根据议题ID，分页
     */
    @RequestMapping(value = "/page/list")
    @ResponseBody
    public PageResult<Reply> pageList(Reply reply, Integer page, Integer limit) {
        PageResult<Reply> result = new PageResult<>();
        if(null == reply || null == reply.getIssueId()){
            result.setCode(500);
        }
        result = replyService.selectPageListByIssue(reply, page, limit);
        if(result.getCode() == 0){
            result.setCode(200);
        }
        return result;
    }

    /**
     * 获取回复表列表，根据回复ID，不分页
     */
    @RequestMapping(value = "/list/replyId")
    @ResponseBody
    public PageResult<Reply> listByReplyId(Reply reply, String condition, Model model, HttpServletRequest request) {
        PageResult<Reply> result = new PageResult<>();
        if(null == reply || null == reply.getToReplyId()){
            result.setCode(500);
        }
        List<Reply> list = replyService.selectListByReplyId(reply);
        result.setData(list);
        result.setCount(list.size());
        result.setCode(200);
        return result;
    }

    /**
     * 新增回复表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @SysLog(operationType="add操作:",operationName="添加回复表")
    public JsonResult add(ReplyModel replyModel) {
        if (replyService.saveModel(replyModel)) {
            return JsonResult.ok("添加成功");
        } else {
            return JsonResult.error("添加失败");
        }
    }
}
