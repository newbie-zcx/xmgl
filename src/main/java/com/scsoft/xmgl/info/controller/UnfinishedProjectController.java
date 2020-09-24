package com.scsoft.xmgl.info.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scsoft.scpt.annotation.SysLog;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.info.entity.Info;
import com.scsoft.xmgl.info.service.IUnfinishedProjectService;
import com.scsoft.xmgl.system.entity.User;
import com.scsoft.xmgl.system.service.IUserService;
import com.scsoft.xmgl.system.service.impl.UserServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/info/unfinished")
public class UnfinishedProjectController extends BaseController {
    @Autowired
    private IUnfinishedProjectService unfinishedProjectService;
    @Autowired
    private IUserService userService = new UserServiceImpl();

    private final String PREFIX="module/info";

    @RequiresPermissions("unfinished:view")
    @RequestMapping
    public String test(){
        return PREFIX+"/UnfinishedProject";
    }
    /**
     * 查询项目列表
     */
    @RequiresPermissions("unfinished:view")
    @ResponseBody
    @RequestMapping("/list")
    @ApiOperation(value = "查询所有未完成项目信息", notes = "查询所有未完成项目信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
    })
    public PageResult<Info> list(Integer page, Integer limit,String proName, String proManager,String proDept) {
        if (page == null) {
            page = 1;
            limit = 10;
        }
        return  unfinishedProjectService.list(page, limit, proName, proManager, proDept);
    }
    /**
     * 添加项目
     **/
    @RequiresPermissions("unfinished:add")
    @ResponseBody
    @RequestMapping("/add")
    public JsonResult add(Info info) {
        if (unfinishedProjectService.add(info)) {
            return JsonResult.ok("添加成功");
        } else {
            return JsonResult.error("添加失败");
        }
    }


    @RequestMapping("/editForm")
    public String addInfo(Model model) {
        List<User> userList = userService.list();
        List<User> nameList = new ArrayList<User>();
        for (int i = 0;i<userList.size();i++){
            String sectionPost = userList.get(i).getSectionPost();

            if (StringUtils.isBlank(sectionPost)){
            }else if (sectionPost.equals("项目经理")){
                nameList.add(userList.get(i));
            }else {
            }
        }
        if (nameList.size()!=0){
            model.addAttribute("nameList",nameList);
        }else {
            model.addAttribute("nameList","无项目经理");
        }
        return PREFIX+"/UnfinishedProject_form";
    }

    @RequestMapping("/editForm2")
    public String queryById() {
        return PREFIX+"/UnfinishedProject_form2";
    }

    /**
    * 删除项目
    **/
    @RequiresPermissions("unfinished:delete")
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult delete(int id) {
        if (unfinishedProjectService.delete(id)) {
            return JsonResult.ok();
        } else {
            return JsonResult.error();
        }
    }
    /**
     * 修改项目
     **/
    @RequiresPermissions("unfinished:update")
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(Info info) {
        if (unfinishedProjectService.update(info)) {
            return JsonResult.ok("修改成功");
        } else {
            return JsonResult.error("修改失败");
        }
    }

    /**
     * 批量导入
     **/
    @ResponseBody
    @RequestMapping("/batchImportInfo")
    @SysLog(operationType="导入用户操作:",operationName="批量导入用户")
    public JsonResult resetPsw(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        List<Object> list = EasyExcelFactory.read(new BufferedInputStream(getInputStream(file)), new Sheet(1));
        String listString = JSONObject.toJSONString(list);
        JSONArray arryList = JSONObject.parseArray(listString);
        // 处理数据
        for (int i = 1; i < arryList.size(); i++) {
            Map<String, Object> map = new HashMap<>();
//            arryList.get(i);
            JSONArray rowData = JSONObject.parseArray(JSONObject.toJSONString(arryList.get(i)));
            Info info = new Info();
            info.setProCompletion(String.valueOf(rowData.get(0)));
            info.setProStage(String.valueOf(rowData.get(1)));
            info.setProId(String.valueOf(rowData.get(2)));
            info.setContractId(String.valueOf(rowData.get(3)));
            info.setProType(String.valueOf(rowData.get(4)));
            info.setProName(String.valueOf(rowData.get(5)));
            info.setProManager(String.valueOf(rowData.get(6)));
            info.setProDept(String.valueOf(rowData.get(7)));
            info.setProDatetime(String.valueOf(rowData.get(8)));
            info.setCustomerName(String.valueOf(rowData.get(9)));
            info.setContractDatetime(String.valueOf(rowData.get(10)));
            info.setContractPeople(String.valueOf(rowData.get(11)));
            info.setContractAmount(String.valueOf(rowData.get(12)));
            info.setProFinishPlan(String.valueOf(rowData.get(13)));
            info.setProFinishActual(String.valueOf(rowData.get(14)));
            info.setProZBPeriod(String.valueOf(rowData.get(15)));
            info.setProYWPeriod(String.valueOf(rowData.get(16)));
            info.setContractPayment(String.valueOf(rowData.get(17)));
            unfinishedProjectService.add(info);
        }
        return JsonResult.ok("导入成功！");
    }
    private InputStream getInputStream(MultipartFile file) {
        try {
            return file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


















}
