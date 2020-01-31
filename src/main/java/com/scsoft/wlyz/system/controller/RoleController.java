package com.scsoft.xgsb.system.controller;

import com.scsoft.xgsb.system.entity.Authority;
import com.scsoft.xgsb.system.entity.Role;
import com.scsoft.xgsb.system.service.IAuthorityService;
import com.scsoft.xgsb.system.service.IRoleService;
import com.scsoft.scpt.annotation.SysLog;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.scpt.utils.JsonUtils;
import com.scsoft.scpt.utils.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 角色管理
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@Controller
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IAuthorityService authoritiesService;
    private final String PREFIX="module/system";
    @RequiresPermissions("role:view")
    @RequestMapping()
    public String role() {
        return PREFIX+"/role";
    }


    @RequestMapping("/editForm")
    public String addUser(Model model) {
        return PREFIX+"/role_form";
    }


    @RequestMapping("/auth")
    public String roleAuth(String roleId, Model model) {
        model.addAttribute("roleId", roleId);
        return PREFIX+"/role_auth";
    }

    /**
     * 查询所有角色
     **/
    @RequiresPermissions("role:view")
    @ResponseBody
    @RequestMapping("/list")
    public PageResult<Role> list(String keyword) {
        List<Role> list = roleService.list(false);
        if (keyword != null && !keyword.trim().isEmpty()) {
            keyword = keyword.trim();
            Iterator<Role> iterator = list.iterator();
            while (iterator.hasNext()) {
                Role next = iterator.next();
                boolean b = String.valueOf(next.getId()).contains(keyword) || next.getName().contains(keyword) || next.getDescription().contains(keyword);
                if (!b) {
                    iterator.remove();
                }
            }
        }
        return new PageResult<>(list);
    }

    /**
     * 添加角色
     **/
    @RequiresPermissions("role:add")
    @ResponseBody
    @RequestMapping("/add")
    @SysLog(operationType="add操作:",operationName="添加角色")
    public JsonResult add(Role role) {
        if (roleService.save(role)) {
            return JsonResult.ok("添加成功");
        } else {
            return JsonResult.error("添加失败");
        }
    }

    /**
     * 修改角色
     **/
    @RequiresPermissions("role:update")
    @ResponseBody
    @RequestMapping("/update")
    @SysLog(operationType="update操作:",operationName="修改角色")
    public JsonResult update(Role role) {
        if (roleService.updateById(role)) {
            return JsonResult.ok("修改成功！");
        } else {
            return JsonResult.error("修改失败！");
        }
    }

    /**
     * 删除角色
     **/
    @RequiresPermissions("role:delete")
    @ResponseBody
    @RequestMapping("/delete")
    @SysLog(operationType="delete操作:",operationName="删除角色")
    public JsonResult delete(int roleId) {
        if (roleService.updateState(roleId, 1)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 角色权限树
     */
    @ResponseBody
    @GetMapping("/authTree")
    public List<Map<String, Object>> authTree(int roleId) {
        List<Authority> roleAuths = authoritiesService.listByRoleId(roleId);
        List<Authority> allAuths = authoritiesService.list();
        List<Map<String, Object>> authTrees = new ArrayList<>();
        for (Authority one : allAuths) {
            Map<String, Object> authTree = new HashMap<>();
            authTree.put("id", one.getId());
            authTree.put("name", one.getName() + " " + StringUtils.getStr(one.getPermission()));
            authTree.put("pId", one.getParentId());
            authTree.put("open", true);
            authTree.put("checked", false);
            for (Authority temp : roleAuths) {
                if (temp.getId().equals(one.getId())) {
                    authTree.put("checked", true);
                    break;
                }
            }
            authTrees.add(authTree);
        }
        return authTrees;
    }

    /**
     * 修改角色权限
     */
    @RequiresPermissions("role:auth")
    @ResponseBody
    @PostMapping("/updateRoleAuth")
    @SysLog(operationType="update操作:",operationName="修改角色权限")
    public JsonResult updateRoleAuth(int roleId, String authIds) {
        if (authoritiesService.updateRoleAuth(roleId, JsonUtils.parseArray(authIds, Integer.class))) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }
}
