package com.scsoft.xmgl.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scsoft.scpt.annotation.SysLog;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.base.enitity.Dtree;
import com.scsoft.scpt.common.DtreeResult;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.scpt.utils.StringUtils;
import com.scsoft.xmgl.common.handler.SystemCommonHandler;
import com.scsoft.xmgl.system.entity.Depart;
import com.scsoft.xmgl.system.service.IDepartService;
import com.scsoft.xmgl.system.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @title: DepartController
 * @Description:  机构管理
 * @Author: zhaopengfei
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @CreateDate: 2019/3/26 9:14
 * @Version: 1.0
 */
@Controller
@RequestMapping("/system/depart")
public class DepartController extends BaseController {
    @Autowired
    private IDepartService departService;
    private final String PREFIX="module/system";
    @Autowired
    private IUserService userService;
    @RequiresPermissions("depart:view")
    @RequestMapping()
    public String toDepart(Model model) {
        Integer userId= SystemCommonHandler.getLoginUserId();
        List<Depart> departs=departService.getByUserId(userId);
        Depart depart=departService.getById(1);
        if(departs.size()>0){
            model.addAttribute("depart",depart);
        }
        return PREFIX+"/depart";
    }


    @RequiresPermissions("depart:view")
    @RequestMapping("/treeDepart")
    @ResponseBody
    public DtreeResult toTreeDepart(Model model, Integer departId) {
//        User user=SystemCommonHandler.getLoginUser();
        try {
        List<Depart> deps = new ArrayList<Depart>();
        deps.add(departService.getById(departId));
        List<Dtree> dtreeList=getByPId(deps);
        if (dtreeList==null){
            Dtree dtree =new Dtree();
            dtree.setId("0");
            dtree.setParentId("-1");
            dtree.setTitle("无数据");
            dtreeList.add(dtree);
        }
        return DtreeResult.ok(dtreeList);
        }catch (Exception e){
            e.printStackTrace();
            return DtreeResult.error(400);
        }
    }
// 查找需要添加的部门，以树形图展示

    @RequiresPermissions("depart:view")
    @RequestMapping("/addTreeDepart")
    @ResponseBody
    public DtreeResult toaddTreeDepart(Model model, Integer departId) {
//        User user=SystemCommonHandler.getLoginUser();
        try {
            // 查找没有的
            List<Depart> deps = departService.selectNotInId(departId);
            List<Dtree> dtreeList=getByPId(deps);
            if (dtreeList==null){
                Dtree dtree =new Dtree();
                dtree.setId("0");
                dtree.setParentId("-1");
                dtree.setTitle("无数据");
                dtreeList.add(dtree);
            }
            return DtreeResult.ok(dtreeList);
        }catch (Exception e){
            e.printStackTrace();
            return DtreeResult.error(400);
        }
    }



    @RequestMapping("editForm")
    public String editForm(Model model, String departId) {
        model.addAttribute("departId", departId);
        return PREFIX+"/depart_form";
    }


    /**
     * 查询所有权限
     **/
    @RequiresPermissions("depart:view")
    @ResponseBody
    @RequestMapping("/list")
    public PageResult<Depart> list(Integer page, Integer limit, Integer departId, String departName){
        LambdaQueryWrapper<Depart> lwrapper=new LambdaQueryWrapper<Depart>();
        if (StringUtils.isNotBlank(departName)) {
            lwrapper.eq(Depart::getDepartName, departName);
        }
        lwrapper.eq(Depart::getParentId,departId).or().eq(Depart::getId,departId);
        Page<Depart> userPage = new Page<Depart>(page, limit);
        IPage<Depart> ipage = departService.page(userPage, lwrapper);
        return new PageResult<Depart>(ipage.getRecords(),ipage.getTotal());
    }
    /**
     * 添加部门
     */
    @RequiresPermissions("depart:add")
    @ResponseBody
    @RequestMapping("/add")
    @SysLog(operationType="add操作:",operationName="添加部门")//注意：这个不加的话，这个方法的日志记录不会被
    public JsonResult add(Depart depart, String departId) {
        /*父节点*/
        Depart pdepart = departService.getById(departId);
        depart.setParentId(Integer.valueOf(departId));
        depart.setParentIds(pdepart.getParentId()+","+pdepart.getId());
        depart.setDescription(pdepart.getDescription()+"-"+depart.getDepartName());
        if (departService.save(depart)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 修改部门
     */
    @RequiresPermissions("depart:update")
    @ResponseBody
    @RequestMapping("/update")
    @SysLog(operationType="update操作:",operationName="修改部门")
    public JsonResult update(Depart depart) {
        if (departService.updateById(depart)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 删除部门
     */
    @RequiresPermissions("depart:delete")
    @ResponseBody
    @RequestMapping("/delete")
    @SysLog(operationType="delete操作:",operationName="删除部门")
    public JsonResult delete(int departId) {
        if (departService.removeById(departId)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    @RequiresPermissions("depart:delete")
    @ResponseBody
    @RequestMapping("/batchDelete")
    @SysLog(operationType="delete操作:",operationName="批量删除部门")
    public JsonResult batchDelete(String departIds) {
        System.out.println(departIds);
        List<String> lis = Arrays.asList(departIds.split(","));
        if (departService.removeByIds(lis)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }


    public List<Dtree> getByPId(List<Depart> departs) {
        List<Dtree> dtreeList=new ArrayList<Dtree>();
        for (Depart depart:departs){
            Dtree dtree =new Dtree();
            int depId=depart.getId();
            dtree.setId(depId+"");
            dtree.setParentId(depart.getParentId()+"");
            dtree.setTitle(depart.getDepartName());
            if (depart.getParentId()==1||depart.getParentId()==-1){
                dtree.setSpread(true);
            }else{
                dtree.setSpread(false);
            }
            List<Depart> deplist=departService.getByPId(depId);
            if (deplist!=null&&deplist.size()>0){
                dtree.setChildren(getByPId(deplist));
                dtree.setIsLast(false);
            }else {
                dtree.setIsLast(true);
            }
            dtreeList.add(dtree);
        }
        return dtreeList;
    }


    /**
     * 查看没有添加的机构
     * //device/bind/departlist
     */
    @RequiresPermissions(value = {"devicebind:view"})
    @RequestMapping("/adddepartlist")
    @ResponseBody
    public PageResult<Depart> adddepartlist(Integer page, Integer limit, Integer gid, String departName, String departId  ) {
        LambdaQueryWrapper<Depart> lwrapper=new LambdaQueryWrapper<Depart>();
        if (page == null) {
            page = 1;
            limit = 5;
        }
        if (StringUtils.isBlank(departName)) {
            departName = null;
        }
        if (StringUtils.isNotBlank(departName)) {
            lwrapper.eq(Depart::getDepartName, departName);
        }

        Depart depart=new Depart();
        lwrapper.notIn(Depart::getId,departId);
        Page<Depart> userPage = new Page<Depart>(page, limit);
        IPage<Depart> ipage = departService.page(userPage, lwrapper);
        return new PageResult<Depart>(ipage.getRecords(),ipage.getTotal());
    }
}
