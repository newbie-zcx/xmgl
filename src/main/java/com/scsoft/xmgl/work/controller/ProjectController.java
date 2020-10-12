package com.scsoft.xmgl.work.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.fastjson.JSONObject;
import com.scsoft.scpt.annotation.SysLog;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.common.handler.SystemCommonHandler;
import com.scsoft.xmgl.common.utils.DateUtils;
import com.scsoft.xmgl.system.entity.Depart;
import com.scsoft.xmgl.system.entity.Role;
import com.scsoft.xmgl.system.entity.User;
import com.scsoft.xmgl.system.service.IDepartService;
import com.scsoft.xmgl.system.service.IRoleService;
import com.scsoft.xmgl.system.service.IUserService;
import com.scsoft.xmgl.work.entity.ProUser;
import com.scsoft.xmgl.work.entity.Project;
import com.scsoft.xmgl.work.mapper.ProUserMapper;
import com.scsoft.xmgl.work.mapper.ProjectMapper;
import com.scsoft.xmgl.work.service.IProjectService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/work/pro")
public class ProjectController extends BaseController {

    @Autowired
    private IProjectService projectService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IDepartService departService;
    @Autowired
    private IRoleService roleService;
    @Resource
    private ProUserMapper proUserMapper;
    @Resource
    private ProjectMapper projectMapper;

    private final String PREFIX="module/work";

    @RequestMapping
    public String toIndex(Model model){
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
        return PREFIX+"/project";
    }

    @RequiresPermissions("pro:add")
    @RequestMapping("/addForm")
    public String addForm(Model model){
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
        //各部门人员名单
        List<User> userListByDepart1 = departService.getUserListByDepart(departList.get(0));
        List<User> userListByDepart2 = departService.getUserListByDepart(departList.get(1));
        List<User> userListByDepart3 = departService.getUserListByDepart(departList.get(2));
        List<User> userListByDepart4 = departService.getUserListByDepart(departList.get(3));
        model.addAttribute("List1",userListByDepart1);
        model.addAttribute("List2",userListByDepart2);
        model.addAttribute("List3",userListByDepart3);
        model.addAttribute("List4",userListByDepart4);
        return PREFIX+"/addProjectForm";
    }

    @RequiresPermissions("pro:edit")
    @RequestMapping("/editForm")
    public String editForm(Model model,int id){
        List<User> userList2 = userService.list();
        List<User> nameList = new ArrayList<User>();
        Role role = new Role();
        for (int i = 0;i<userList2.size();i++){
            List<Role> roleList = roleService.getByUserId(userList2.get(i).getId());
            if (roleList.get(0)==null){
                continue;
            }else {
                role = roleList.get(0);
            }
            if (StringUtils.isBlank(role.getName())){
            }else if (role.getName().equals("项目经理")&&StringUtils.isNotBlank(role.getName())){
                nameList.add(userList2.get(i));
            }else {
            }
        }
        if (nameList.size()!=0){
            model.addAttribute("nameList",nameList);
        }else {
            model.addAttribute("nameList","无项目经理");
        }
        List<ProUser> proUserList = proUserMapper.selectByProId(id);
        List<User> userList = new ArrayList<User>();
        if (proUserList.size()<=0){
        }else {
            for (int i = 0;i<proUserList.size();i++){
                User user = userService.getById(proUserList.get(i).getUserId());
                userList.add(user);
            }
            JSONArray userList1 = JSONArray.fromObject(userList);
            model.addAttribute("userList",userList1);
//            model.addAttribute("userList",userList);
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
        //各部门人员名单
        List<User> userListByDepart1 = departService.getUserListByDepart(departList.get(0));
        List<User> userListByDepart2 = departService.getUserListByDepart(departList.get(1));
        List<User> userListByDepart3 = departService.getUserListByDepart(departList.get(2));
        List<User> userListByDepart4 = departService.getUserListByDepart(departList.get(3));
        model.addAttribute("List1",userListByDepart1);
        model.addAttribute("List2",userListByDepart2);
        model.addAttribute("List3",userListByDepart3);
        model.addAttribute("List4",userListByDepart4);
        return PREFIX+"/editProjectForm";
    }
    /**
    * 查看项目信息列表
    **/
    @RequiresPermissions("pro:view")
    @ResponseBody
    @RequestMapping("/list")
    @ApiOperation(value = "查看所有项目信息", notes = "查询项目信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
    })
    public PageResult<Project> list(Integer page, Integer limit,String proId,String proName,String proManager,String manager,String proDept,String proSigner,Integer proEndState){
        if (page == null){
            page = 1;
            limit = 15;
        }
        return projectService.list(page,limit,proId,proName,proManager,manager,proDept,proSigner,proEndState);
    }


    /**
    * 添加项目以及成员
    **/
    @RequiresPermissions("pro:add")
    @ResponseBody
    @RequestMapping("/add")
    @SysLog(operationType = "add操作:",operationName = "添加项目信息")
    public JsonResult add(Project project, String userIds,String unUserIds){
        String[] Ids = userIds.split("-");
        String[] unIds = unUserIds.split("-");
        List<Integer> userIdList = new ArrayList<Integer>();
        List<Integer> unUserIdList = new ArrayList<Integer>();
        for (int i = 0;i<Ids.length;i++){
            if (StringUtils.isBlank(Ids[i])){
            } else {
                int ids = Integer.parseInt(Ids[i]);
                userIdList.add(ids);
            }
        }
        for (int j = 0;j<unIds.length;j++){
            if (StringUtils.isBlank(unIds[j])){
            }else {
                int idss = Integer.parseInt(unIds[j]);
                unUserIdList.add(idss);
            }
        }
        for (int x=0;x<userIdList.size();x++){
            if (unUserIdList.size()>0){
                for (int y=0;y<unUserIdList.size();y++){
                    if (userIdList.get(x)==unUserIdList.get(y)){
                        userIdList.remove(x);
                        x--;
                        unUserIdList.remove(y);
                        y--;
                        break;
                    }else {
                        continue;
                    }
                }
            }else{
                break;
            }
        }
        if(projectService.add(project,userIdList)){
            return JsonResult.ok("添加成功");
        }else {
            return JsonResult.error("添加失败");
        }
    }

    /**
    * 修改项目以及成员
    **/
    @RequiresPermissions("pro:edit")
    @ResponseBody
    @RequestMapping("/update")
    @SysLog(operationType = "update操作:",operationName = "修改项目信息")
    public JsonResult update(Project project,String userIds,String unUserIds){
        String[] Ids = userIds.split("-");
        String[] unIds = unUserIds.split("-");
        List<Integer> userIdList = new ArrayList<Integer>();
        List<Integer> unUserIdList = new ArrayList<Integer>();
        for (int i = 0;i<Ids.length;i++){
            if (StringUtils.isBlank(Ids[i])){
            } else {
                int ids = Integer.parseInt(Ids[i]);
                userIdList.add(ids);
            }
        }
        for (int j = 0;j<unIds.length;j++){
            if (StringUtils.isBlank(unIds[j])){
            }else {
                int idss = Integer.parseInt(unIds[j]);
                unUserIdList.add(idss);
            }
        }
        for (int x=0;x<userIdList.size();x++){
            if (unUserIdList.size()>0){
                for (int y=0;y<unUserIdList.size();y++){
                    if (userIdList.get(x)==unUserIdList.get(y)){
                        userIdList.remove(x);
                        x--;
                        unUserIdList.remove(y);
                        y--;
                        break;
                    }else {
                        continue;
                    }
                }
            }else{
                break;
            }
        }
        if (projectService.update(project,userIdList)){
            return JsonResult.ok("修改成功");
        }else{
            return JsonResult.error("修改失败");
        }
    }

    /**
    * 删除项目以及成员
    **/
    @RequiresPermissions("pro:delete")
    @ResponseBody
    @RequestMapping("/delete")
    @SysLog(operationType = "delete操作:",operationName = "删除项目及成员信息")
    public JsonResult delete(int proId){
        if (projectService.delete(proId)){
            return JsonResult.ok("删除成功");
        }else{
            return JsonResult.error("删除失败");
        }
    }

    /**
    * 批量导入
    **/
    @ResponseBody
    @RequestMapping("/batchImportPro")
    @SysLog(operationType = "导入项目操作:",operationName = "批量导入项目信息")
    public JsonResult importPro( @RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        boolean result = false;
        List<Object> list = EasyExcelFactory.read(new BufferedInputStream(getInputStream(file)),new Sheet(1));
        String listString = JSONObject.toJSONString(list);
        com.alibaba.fastjson.JSONArray jsonArray = JSONObject.parseArray(listString);
        // 时间格式转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 1;i < jsonArray.size();i++){
            Map<String, Object> map = new HashMap<>();
            com.alibaba.fastjson.JSONArray rowData = JSONObject.parseArray(JSONObject.toJSONString(jsonArray.get(i)));
            Project project = new Project();
            project.setProId(String.valueOf(rowData.get(0)));
            project.setProName(String.valueOf(rowData.get(1)));
            project.setNickName(String.valueOf(rowData.get(2)));
            project.setProResume(String.valueOf(rowData.get(3)));
            project.setProNature(String.valueOf(rowData.get(4)));
            project.setProType(String.valueOf(rowData.get(5)));
            project.setProManager(String.valueOf(rowData.get(6)));
            project.setManager(String.valueOf(rowData.get(7)));
            project.setProDept(String.valueOf(rowData.get(8)));
            project.setProSigner(String.valueOf(rowData.get(9)));
            Date startDate = DateUtils.StringToDate(String.valueOf(rowData.get(10)));
            project.setStartDate(DateUtils.DateToString(startDate));
            Date planCompleteDate = DateUtils.StringToDate(String.valueOf(rowData.get(11)));
            project.setStartDate(DateUtils.DateToString(planCompleteDate));
            Date planChangeDate = DateUtils.StringToDate(String.valueOf(rowData.get(12)));
            project.setPlanChangeDate(DateUtils.DateToString(planChangeDate));
            Integer proEndState = Integer.parseInt(String.valueOf(rowData.get(13)));
            project.setProEndState(proEndState);
            //插入数据库
            result =projectMapper.insert(project) > 0;
        }
        if (result){
            return JsonResult.ok("导入成功！");
        }else {
            return JsonResult.error("导入失败！");
        }
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
