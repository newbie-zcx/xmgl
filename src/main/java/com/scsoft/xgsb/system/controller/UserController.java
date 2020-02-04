package com.scsoft.xgsb.system.controller;

import com.scsoft.xgsb.common.handler.SystemCommonHandler;
import com.scsoft.xgsb.common.shiro.EndecryptUtil;
import com.scsoft.xgsb.system.entity.Depart;
import com.scsoft.xgsb.system.entity.Role;
import com.scsoft.xgsb.system.entity.User;
import com.scsoft.xgsb.system.service.IDepartService;
import com.scsoft.xgsb.system.service.IRoleService;
import com.scsoft.xgsb.system.service.IUserService;
import com.scsoft.scpt.annotation.SysLog;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.scpt.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@Api(value = "用户相关的接口", tags = "user")
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IDepartService departService;

    private final String PREFIX="module/system";

    @RequiresPermissions("user:view")
    @RequestMapping
    public String user(Model model) {
        Integer userId= SystemCommonHandler.getLoginUserId();
        List<Role> roles = roleService.list(false);
        model.addAttribute("roles", roles);
        List<Depart> departs=departService.getByUserId(userId);
        if(departs.size()>0){
            model.addAttribute("depart",departs.get(0));
        }
        return PREFIX+"/user";
    }

    @RequestMapping("/editForm")
    public String addUser(Model model) {
        List<Role> roles = roleService.list(false);
        String departId=getParameter("departId");
        model.addAttribute("departId", departId);
        model.addAttribute("roles", roles);
        return PREFIX+"/user_form";
    }

    /**
     * 查询用户列表
     */
    @RequiresPermissions("user:view")
    @ResponseBody
    @RequestMapping("/list")
    @ApiOperation(value = "查询所有用户", notes = "查询所有用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "searchKey", value = "筛选条件字段", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "searchValue", value = "筛选条件关键字", dataType = "String", paramType = "form"),
    })
    public PageResult<User> list(Integer page, Integer limit, String searchKey, String searchValue) {
        if (page == null) {
            page = 1;
            limit = 5;
        }
        if (StringUtils.isBlank(searchValue)) {
            searchKey = null;
        }
        String departId=getParameter("departId");
        return userService.list(page, limit, true, searchKey, searchValue,departId);
    }

    /**
     * 添加用户
     **/
    @RequiresPermissions("user:add")
    @ResponseBody
    @RequestMapping("/add")
    @SysLog(operationType="add操作:",operationName="添加用户")//注意：这个不加的话，这个方法的日志记录不会被
    public JsonResult add(User user, String roleId) {
        user.setRoles(getRoles(roleId));
        user.setPassword("123456");
        String departId=getParameter("departId");
        if (userService.add(user,departId)) {
            return JsonResult.ok("添加成功");
        } else {
            return JsonResult.error("添加失败");
        }
    }

    /**
     * 修改用户
     **/
    @RequiresPermissions("user:update")
    @ResponseBody
    @RequestMapping("/update")
    @SysLog(operationType="update操作:",operationName="修改用户")
    public JsonResult update(User user, String roleId) {
        user.setRoles(getRoles(roleId));
        if (userService.update(user)) {
            return JsonResult.ok("修改成功");
        } else {
            return JsonResult.error("修改失败");
        }
    }

    private List<Role> getRoles(String roleStr) {
        List<Role> roles = new ArrayList<>();
        String[] split = roleStr.split(",");
        for (String t : split) {
            roles.add(new Role(Integer.parseInt(t)));
        }
        return roles;
    }

    /**
     * 修改用户状态
     **/
    @RequiresPermissions("user:delete")
    @ResponseBody
    @RequestMapping("/updateState")
    @SysLog(operationType="update操作:",operationName="修改用户状态")
    public JsonResult updateState(int userId, Integer state) {
        if (userService.updateState(userId, state)) {
            return JsonResult.ok();
        } else {
            return JsonResult.error();
        }
    }

    /**
     * 删除用户
     **/
    @RequiresPermissions("user:delete")
    @ResponseBody
    @RequestMapping("/delete")
    @SysLog(operationType="delete操作:",operationName="删除用户")
    public JsonResult delete(int userId) {
        if (userService.delete(userId)) {
            return JsonResult.ok();
        } else {
            return JsonResult.error();
        }
    }
    /**
     * 修改自己密码
     **/
    @ResponseBody
    @RequestMapping("/updatePsw")
    @SysLog(operationType="update操作:",operationName="修改用户密码")
    public JsonResult updatePsw(String oldPsw, String newPsw) {
        User user=SystemCommonHandler.getLoginUser();
        String finalSecret = EndecryptUtil.encrytMd5(oldPsw, user.getUserName()+user.getSalt(), 1024);
        if (!finalSecret.equals(SystemCommonHandler.getLoginUser().getPassword())) {
            return JsonResult.error("原密码输入不正确");
        }
        if (userService.updatePsw(SystemCommonHandler.getLoginUserId(), SystemCommonHandler.getLoginUserName(), newPsw)) {
            return JsonResult.ok("修改成功");
        } else {
            return JsonResult.error("修改失败");
        }
    }

    /**
     * 重置密码
     **/
    @RequiresPermissions("user:update")
    @ResponseBody
    @RequestMapping("/restPsw")
    @SysLog(operationType="rest操作:",operationName="重置密码")
    public JsonResult resetPsw(int userId) {
        User byId = userService.getById(userId);
        if (userService.updatePsw(userId, byId.getUserName(), "123456")) {
            return JsonResult.ok("重置成功");
        } else {
            return JsonResult.error("重置失败");
        }
    }
}
