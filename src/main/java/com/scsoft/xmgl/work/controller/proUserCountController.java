package com.scsoft.xmgl.work.controller;

import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.system.entity.User;
import com.scsoft.xmgl.system.service.IUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;

@Controller
@RequestMapping("/work/proUserCount")
public class ProUserCountController extends BaseController {


    @Autowired
    private IUserService userService;

    private final String PREFIX = "module/work";

    @RequiresPermissions("userCount:view")
    @RequestMapping
    public String toIndex(){
        return PREFIX+"/proUserCount";
    }


    @RequiresPermissions("userCount:view")
    @ResponseBody
    @RequestMapping("/list")
    @ApiOperation(value = "查看所有项目成员工时", notes = "查询项目成员列表工时")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
    })
    public PageResult<User> list(Integer page,Integer limit,Integer proId,String realName,String startDate,String endDate) throws ParseException {
        if (page == null){
            page = 1;
            limit = 10;
        }
        return userService.countList(page,limit,proId,realName,startDate,endDate);
    }

}
