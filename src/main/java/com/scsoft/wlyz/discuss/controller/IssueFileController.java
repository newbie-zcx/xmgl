package com.scsoft.wlyz.discuss.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scsoft.scpt.annotation.SysLog;
import com.scsoft.wlyz.file.entity.FileInfo;
import com.scsoft.wlyz.file.service.IFileService;
import org.springframework.web.bind.annotation.*;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.scsoft.wlyz.discuss.service.IIssueFileService;
import com.scsoft.wlyz.discuss.entity.IssueFile;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import com.scsoft.scpt.base.controller.BaseController;

import javax.servlet.http.HttpServletRequest;


/**
 * <p>
 * 议题附件关系表 前端控制器
 * </p>
 * @author wangchao
 * @CreateDate 2020-01-18
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @Version: 1.0
 */

@Controller
@RequestMapping("/discuss/issueFile")
public class IssueFileController extends BaseController {
   private Logger logger = LoggerFactory.getLogger(getClass());

    private String PREFIX = "module/discuss/issueFile/";

    @Autowired
    private IIssueFileService issueFileService;

    @Autowired
    private IFileService fileService;

    /**
     * 跳转到议题附件关系表首页
     */
    @RequiresPermissions("issueFile:view")
    @RequestMapping("")
    public String index(Model model,HttpServletRequest request) {
        return PREFIX + "issueFile";
    }

    /**
     * 跳转到添加议题附件关系表
     */
    @RequestMapping("/issueFileadd")
    @RequiresPermissions("issueFile:add")
    public String toAdd(Model model, HttpServletRequest request) {
        return PREFIX + "issueFile_add.html";
    }

    /**
     * 跳转到修改议题附件关系表
     */
    @RequestMapping("/issueFileupdate")
     @RequiresPermissions("issueFile:update")
    public String toUpdate(Model model,HttpServletRequest request) {
        return PREFIX + "issueFile_edit.html";
    }


   /**
     * 议题附件关系表详情
     */
    @RequestMapping(value = "/detail/{issueFileId}")
    @ResponseBody
    public Object toDetail(@PathVariable("issueFileId") Integer issueFileId, Model model, HttpServletRequest request) {
        return issueFileService.getById(issueFileId);
    }

    /**
     * 获取议题附件关系表列表
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions("issueFile:view")
    @ResponseBody
    public PageResult<IssueFile> list(Integer page, Integer limit, IssueFile issueFile,String condition, Model model,HttpServletRequest request) {
        return issueFileService.listPage(page,limit,issueFile);
    }

    /**
     *
     * 获取议题附件关系表列表
     */
    @RequestMapping(value = "/list/{issueId}")
    @ResponseBody
    public List<FileInfo> listByIssueId(@PathVariable("issueId") Integer issueId, String condition, Model model, HttpServletRequest request) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("ISSUE_ID", issueId);
        List<IssueFile> issueFiles = issueFileService.list(queryWrapper);
        List<Integer> fildIds = issueFiles.stream().map(IssueFile::getFileId).collect(Collectors.toList());
        if(null == fildIds || fildIds.size() == 0){
            return new ArrayList<>();
        }
        return (List<FileInfo>)fileService.listByIds(fildIds);
    }


/**
     * 新增议题附件关系表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @RequiresPermissions("issueFile:add")
    @SysLog(operationType="add操作:",operationName="添加议题附件关系表")
    public JsonResult add(IssueFile issueFile) {
        if (issueFileService.save(issueFile)) {
                return JsonResult.ok("添加成功");
        } else {
            return JsonResult.error("添加失败");
        }

    }

    /**
     * 删除议题附件关系表
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @RequiresPermissions("issueFile:delete")
    @SysLog(operationType="delete操作:",operationName="删除议题附件关系表")
    public JsonResult delete(@RequestParam Integer issueFileId) {
         if (issueFileService.removeById(issueFileId)) {
                  return JsonResult.ok("删除成功");
          } else {
              return JsonResult.error("删除失败");
          }
    }

    /**
     * 逻辑删除议题附件关系表
     */
    @RequestMapping(value = "/logicDelete")
    @ResponseBody
    @RequiresPermissions("issueFile:delete")
    @SysLog(operationType="delete操作:",operationName="逻辑删除议题附件关系表")
    public JsonResult logicDelete(@RequestParam Integer issueFileId) {
    IssueFile issueFile=issueFileService.getById(issueFileId);
       if (issueFileService.logicDelete(issueFile)) {
           return JsonResult.ok("删除成功");
       } else {
           return JsonResult.error("删除失败");
       }
    }


    /**
     * 修改议题附件关系表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @RequiresPermissions("issueFile:update")
    @SysLog(operationType="update操作:",operationName="修改议题附件关系表")
    public JsonResult update(IssueFile issueFile) {
       if (issueFileService.updateById(issueFile)) {
           return JsonResult.ok("修改成功");
       } else {
           return JsonResult.error("修改失败");
       }
    }


    @RequestMapping("/batchDelete")
    @ResponseBody
    @RequiresPermissions("issueFile:delete")
    @SysLog(operationType="delete操作:",operationName="批量删除议题附件关系表")
    public JsonResult batchDelete(String departIds) {
        System.out.println(departIds);
        List<String> lis = Arrays.asList(departIds.split(","));
        if (issueFileService.removeByIds(lis)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

}
