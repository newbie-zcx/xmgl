package com.scsoft.xmgl.info.controller;

import com.scsoft.scpt.annotation.SysLog;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.common.handler.SystemCommonHandler;
import com.scsoft.xmgl.info.entity.OutRegister;
import com.scsoft.xmgl.info.entity.WorkSummary;
import com.scsoft.xmgl.info.service.IOutRegisterService;
import com.scsoft.xmgl.info.service.Impl.OutRegisterServiceImpl;
import com.scsoft.xmgl.system.entity.Depart;
import com.scsoft.xmgl.system.entity.User;
import com.scsoft.xmgl.system.service.IDepartService;
import com.scsoft.xmgl.system.service.IUserService;
import com.scsoft.xmgl.system.service.impl.DepartServiceImpl;
import com.scsoft.xmgl.system.service.impl.UserServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("info/outregister")
public class OutRegisterController extends BaseController {

    @Autowired
    IUserService userService = new UserServiceImpl();
    @Autowired
    IDepartService departService = new DepartServiceImpl();
    @Autowired
    IOutRegisterService outRegisterService = new OutRegisterServiceImpl();

    private final String PREFIX="module/info";

    @RequestMapping
    public String test(){
        return PREFIX+"/outregister";
    }
    @RequiresPermissions("outregister:add")
    @RequestMapping("/editForm")
    public String test1(Model model){
        User user = userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName());
        model.addAttribute("realName",user.getRealName());
        List<Depart> departList = departService.getByUserId(user.getId());
        model.addAttribute("departList",departList.get(0).getAddress());
        return PREFIX+"/outregisterForm";
    }
    @RequiresPermissions("outregister:update")
    @RequestMapping("/editFormEdit")
    public String test2(){
        return PREFIX+"/outregisterFormEdit";
    }
    /**
     * 查询个人外出登记
     */
    @RequiresPermissions("outregister:view")
    @ResponseBody
    @RequestMapping("/list")
    @ApiOperation(value = "个人查询外出登记信息", notes = "个人查询外出登记信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
    })
    public PageResult<OutRegister> list(Integer page, Integer limit) {
        if (page == null) {
            page = 1;
            limit = 10;
        }
        User user = userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName());
        return outRegisterService.list(page,limit,user.getRealName());
    }
    /**
    * 查看部门外出情况
    **/
    @RequiresPermissions("outregister:deptview")
    @ResponseBody
    @RequestMapping("/deptlist")
    @ApiOperation(value = "查询部门外出登记信息", notes = "查询部门外出登记信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
    })
    public PageResult<OutRegister> deptlist(Integer page, Integer limit) {
        if (page == null) {
            page = 1;
            limit = 10;
        }
        User user = userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName());
        List<User> userList = userService.getRealNameBySection(user.getSection());
        return outRegisterService.deptList(page,limit,userList);
    }
    /**
    * 新增外出登记
    **/
    @RequiresPermissions("outregister:add")
    @ResponseBody
    @RequestMapping("/add")
    public JsonResult add(OutRegister outRegister) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        outRegister.setOutDate(sdf.format(new Date()));
        if (outRegisterService.add(outRegister)){
            return JsonResult.ok("添加成功");
        }else {
            return JsonResult.error("添加失败");
        }
    }
    /**
     *修改外出记录
     **/
    @RequiresPermissions("outregister:update")
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(OutRegister outRegister){
//        Date date = new Date();
//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        String inTime = sdf1.format(date);
//        outRegister.setInTime(inTime);
        if (userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName()).getRealName().equals(outRegister.getUserName())){
            if (outRegisterService.update(outRegister)){
                return JsonResult.ok("修改成功");
            }else {
                return JsonResult.error("修改失败");
            }
        }else {
            return JsonResult.error("无权限修改此外出记录");
        }
    }

    @RequiresPermissions("outregister:deptview")
    @ResponseBody
    @RequestMapping("/updateState")
    @SysLog(operationType="update操作:",operationName="部门经理修改外出审核状态")
    public JsonResult updateState(int id, Integer approval) {
        if (outRegisterService.updateState(id,approval)){
            return JsonResult.ok("修改成功");
        }else {
            return JsonResult.error("修改失败");
        }
    }
    @RequiresPermissions("outregister:delete")
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult delete(int id,String userName){
        if (userService.getRealNameByLoginName(SystemCommonHandler.getLoginUserName()).getRealName().equals(userName)){
            if (outRegisterService.delete(id)){
                return JsonResult.ok("删除成功");
            }else {
                return JsonResult.error("删除失败");
            }
        }else {
            return JsonResult.error("无权限删除此外出记录");
        }
    }
}
