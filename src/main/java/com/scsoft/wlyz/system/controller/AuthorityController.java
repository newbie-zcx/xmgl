package com.scsoft.wlyz.system.controller;

import com.scsoft.wlyz.common.handler.SystemCommonHandler;
import com.scsoft.wlyz.system.entity.Authority;
import com.scsoft.wlyz.system.service.IAuthorityService;
import com.scsoft.wlyz.system.service.IRoleService;
import com.scsoft.scpt.annotation.SysLog;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.base.enitity.Dtree;
import com.scsoft.scpt.common.DtreeResult;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.scpt.utils.ReflectUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 权限管理
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@Controller
@RequestMapping("/system/authority")
public class AuthorityController extends BaseController {
    @Autowired
    private IAuthorityService authorityService;
    @Autowired
    private IRoleService roleService;

    private final String PREFIX="module/system";


    @RequiresPermissions("authority:view")
    @RequestMapping()
    public String authority(Model model) {
 /*       List<Role> roles = roleService.list(false);
        model.addAttribute("roles", roles);*/
        return PREFIX+"/authority";
    }

    @RequestMapping("editForm")
    public String editForm(Model model) {
        List<Authority> authority = authorityService.listMenu();
        model.addAttribute("authority", authority);
//        model.addAttribute("sysMenu", jsonArr.toJSONString());
        return PREFIX+"/authority_form";
    }

    /**
     * 查询所有权限
     **/
    @RequiresPermissions("authority:view")
    @ResponseBody
    @RequestMapping("/list")
    public PageResult<Map<String, Object>> list(Integer roleId) {
        // 权限
        List<Map<String, Object>> maps = new ArrayList<>();
        List<Authority> authorities = authorityService.list();
        List<Authority> roleAuths = authorityService.listByUserId(SystemCommonHandler.getLoginUserId());
//        List<Authority> roleAuths = authorityService.listByRoleId(roleId);
        for (Authority one : authorities) {
            Map<String, Object> map = ReflectUtil.objectToMap(one);
            map.put("checked", 0);
            for (Authority roleAuth : roleAuths) {
                if (one.getId().equals(roleAuth.getId())) {
                    map.put("checked", 1);
                    break;
                }
            }
            maps.add(map);
        }
        return new PageResult<>(maps);
    }

    /**
     * 查询所有权限
     **/
    @RequestMapping("/getPartents")
    @ResponseBody
    public DtreeResult getPartents(Integer roleId) {
        // 权限
        List<Authority> authorities = authorityService.listMenu();
        List<Dtree> dtreeList = getMenuTree(authorities, -1);
        if (dtreeList==null){
            Dtree dtree =new Dtree();
            dtree.setId("0");
            dtree.setParentId("-1");
            dtree.setTitle("无数据");
            dtreeList.add(dtree);
        }
        return DtreeResult.ok(dtreeList);
    }

    /**
     * 添加权限
     */
    @RequiresPermissions("authority:add")
    @ResponseBody
    @RequestMapping("/add")
    @SysLog(operationType="add操作:",operationName="添加权限")//注意：这个不加的话，这个方法的日志记录不会被
    public JsonResult add(Authority authority) {
        if (authorityService.save(authority)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 修改权限
     */
    @RequiresPermissions("authority:update")
    @ResponseBody
    @RequestMapping("/update")
    @SysLog(operationType="update操作:",operationName="修改权限")
    public JsonResult update(Authority authority) {
        if (authorityService.update(authority)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 删除权限
     */
    @RequiresPermissions("authority:delete")
    @ResponseBody
    @RequestMapping("/delete")
    @SysLog(operationType="delete操作:",operationName="删除权限")
    public JsonResult delete(Integer authorityId) {
        if (authorityService.delete(authorityId)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }



    /**
     * 递归转化树形菜单
     */
    private List<Dtree> getMenuTree(List<Authority> authorities, int parentId) {
        List<Dtree> dtreeList=new ArrayList<Dtree>();

        for (int i = 0; i < authorities.size(); i++) {
            Authority temp = authorities.get(i);
            if (temp.getType().equals(0) && parentId == temp.getParentId()) {
                Dtree dtree =new Dtree();
                dtree.setId(temp.getId()+"");
                dtree.setParentId(temp.getParentId()+"");
                dtree.setTitle(temp.getName());
                if (temp.getParentId()==1||temp.getParentId()==-1){
                    dtree.setSpread(true);
                }else{
                    dtree.setSpread(false);
                }
                List<Dtree> children = getMenuTree(authorities, authorities.get(i).getId());
                if (children!=null&&children.size()>0){
                    dtree.setChildren(children);
                    dtree.setIsLast(false);
                }else {
                    dtree.setIsLast(true);
                }
                dtreeList.add(dtree);
            }
        }
        return dtreeList;
    }


}
