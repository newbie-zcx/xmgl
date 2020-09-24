package com.scsoft.xmgl.info.controller;

import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.scpt.utils.StringUtils;
import com.scsoft.xmgl.info.entity.Info;
import com.scsoft.xmgl.info.entity.Milepost;
import com.scsoft.xmgl.info.service.IMilepostInfoService;
import com.scsoft.xmgl.info.service.IUnfinishedProjectService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/info/milepost")
public class MilepostInfoController extends BaseController {
    @Autowired
    private IUnfinishedProjectService projectInfoService;
    @Autowired
    private IMilepostInfoService milepostInfoService;
    private final String PREFIX="module/info";

    @RequiresPermissions("milepost:view")
    @RequestMapping
    public String test(){
        return PREFIX+"/milepostInfo";
    }

    /**
     * 查询里程碑
     */
    @RequiresPermissions("milepost:view")
    @ResponseBody
    @RequestMapping("/list")
    @ApiOperation(value = "查询所有里程碑信息", notes = "查询所有里程碑信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
    })
    public PageResult<Milepost> list(Integer page, Integer limit,String proId,String proName,String milepostStartDate,String milepostEndDate,String milepostStatus) {
        Milepost milepost = new Milepost();
        if (page == null) {
            page = 1;
            limit = 10;
        }
        if (StringUtils.isBlank(proId)) {
            proId = null;
        }
        if (StringUtils.isBlank(proName)) {
            proName = null;
        }
        if (StringUtils.isBlank(milepostStartDate)) {
            milepostStartDate = null;
        }
        if (StringUtils.isBlank(milepostEndDate)) {
            milepostEndDate = null;
        }
        if (StringUtils.isBlank(milepostStatus)) {
            milepostStatus = null;
        }
        if (proId == null&&proName == null&&milepostStartDate==null&&milepostEndDate == null&&milepostStatus == null){
            List<Info> infoList = projectInfoService.list(page, limit, null, null, null).getData();
            for (Info info:infoList){
                milepost.setId(info.getId());
                milepost.setProId(info.getProId());
                milepost.setProName(info.getProName());
                milepost.setProManager(info.getProManager());
                Milepost milepost1 = milepostInfoService.queryById(milepost.getId());
                if(milepost1==null){
                    milepostInfoService.add(milepost);
                }else {
                    if (milepost1.getId() == milepost.getId() && milepost1.getProId() == milepost.getProId() && milepost1.getProName() == milepost.getProName() && milepost1.getProManager() == milepost.getProManager()) {
                    } else {
                        milepostInfoService.add(milepost);
                    }
                }
            }
            milepostInfoService.queryall();
        }
        return milepostInfoService.list(page,limit,proId,proName,milepostStartDate,milepostEndDate,milepostStatus);
    }
    @RequestMapping("/editForm")
    public String editMilepost(){
        return PREFIX+"/milepost_form";
    }
    @RequestMapping("/editForm2")
    public String editMilepost2(){
        return PREFIX+"/milepost_form2";
    }

    /**
     * 添加里程碑
     **/
    @RequiresPermissions("milepost:add")
    @ResponseBody
    @RequestMapping("/add")
    public JsonResult add(Milepost milepost) {
        if (milepostInfoService.addMilepost(milepost)) {
            return JsonResult.ok("添加成功");
        } else {
            return JsonResult.error("添加失败");
        }
    }

    /**
     * 删除里程碑
     **/
    @RequiresPermissions("milepost:delete")
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult delete(int id) {
        if (milepostInfoService.delete(id)) {
            return JsonResult.ok();
        } else {
            return JsonResult.error();
        }
    }

    /**
     * 修改里程碑
     **/
    @RequiresPermissions("milepost:update")
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(Milepost milepost){
        if(milepostInfoService.update(milepost)){
            return JsonResult.ok("修改成功");
        }else {
            return JsonResult.error("修改失败");
        }
    }
}
