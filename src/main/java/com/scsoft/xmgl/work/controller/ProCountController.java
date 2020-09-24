package com.scsoft.xmgl.work.controller;

import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.common.handler.SystemCommonHandler;
import com.scsoft.xmgl.system.entity.Depart;
import com.scsoft.xmgl.system.entity.Role;
import com.scsoft.xmgl.system.entity.User;
import com.scsoft.xmgl.system.mapper.DepartMapper;
import com.scsoft.xmgl.system.mapper.RoleMapper;
import com.scsoft.xmgl.system.service.IDepartService;
import com.scsoft.xmgl.system.service.IRoleService;
import com.scsoft.xmgl.system.service.IUserService;
import com.scsoft.xmgl.work.entity.ProUser;
import com.scsoft.xmgl.work.entity.Project;
import com.scsoft.xmgl.work.mapper.ProUserMapper;
import com.scsoft.xmgl.work.service.IProjectService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Controller
@RequestMapping("/work/proCount")
public class ProCountController extends BaseController {

    @Autowired
    private IProjectService projectService;
    @Resource
    private ProUserMapper proUserMapper;
    @Autowired
    private IDepartService departService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUserService userService;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private DepartMapper departMapper;

    private final String PREFIX = "module/work";

    @RequestMapping
    public String toIndex(Model model) {
        List<User> userList = userService.list();
        List<User> nameList = new ArrayList<User>();
        Role role = new Role();
        for (int i = 0;i<userList.size();i++){
            List<Role> roleList = roleService.getByUserId(userList.get(i).getId());
            if (roleList.get(0)==null){
                continue;
            }else {
                role = roleList.get(0);
            }
            if (StringUtils.isBlank(role.getName())){
            }else if (role.getName().equals("项目经理")&&StringUtils.isNotBlank(role.getName())){
                nameList.add(userList.get(i));
            }else {
            }
        }
        if (nameList.size()!=0){
            model.addAttribute("nameList",nameList);
        }else {
            model.addAttribute("nameList","无项目经理");
        }
        Integer userId = SystemCommonHandler.getLoginUser().getId();
        List<Depart> userDepart = departService.getByUserId(userId);
        Depart depart = userDepart.get(0);
        List<Depart> departList = new ArrayList<Depart>();
        if (depart.getDepartLevel().equals("3")){
            departList = departService.getByPId(depart.getParentId());
        }else if (depart.getDepartLevel().equals("2")){
            departList = departService.getByPId(depart.getId());
        }else {
            //部门等级为1的操作
        }
        model.addAttribute("departList",departList);
        return PREFIX+"/proCount";
    }

    @RequiresPermissions("proCount:view")
    @RequestMapping("/showForm")
    public String showMember(Model model,Integer id){
        User user = SystemCommonHandler.getLoginUser();
        List<Depart> departList = departMapper.selectByUserId(user.getId());
        List<Role> roleList = roleMapper.selectByUserId(user.getId());
        if (roleList.size()>0){
            String code = roleList.get(0).getCode();
            if (code.equals("101")){
                //超级管理
            }else if (code.equals("102")||code.equals("108")){
                //领导组或普通管理员
            }else if (code.equals("103")){
                //部门经理
            }else if (code.equals("104")){
                //项目经理
            }
        }

        List<ProUser> proUsers = proUserMapper.selectByProId(id);
        List<Integer> userIdList = new ArrayList<Integer>();
        for (int i =0;i<proUsers.size();i++){
            userIdList.add(proUsers.get(i).getUserId());
        }
        Collection<User> users = userService.listByIds(userIdList);
        model.addAttribute("nameList",users);
        model.addAttribute("proId",id);
        return PREFIX+"/showForm";
    }

    @RequiresPermissions("proCount:view")
    @ResponseBody
    @RequestMapping("/list")
    @ApiOperation(value = "查看所有项目及工时", notes = "查询项目工时信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
    })
    public PageResult<Project> list(Integer page, Integer limit,String proId,String proName,String proManager,String proDept,String proSigner,String startDate,String endDate) throws ParseException {
        if (page == null){
            page = 1;
            limit = 15;
        }
        return projectService.list(page,limit,proId,proName,proManager,proDept,proSigner,startDate,endDate);
    }
}
