package com.scsoft.xmgl.work.controller;

import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.xmgl.work.entity.Project;
import com.scsoft.xmgl.work.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Create By zhangchenxi on 2020/10/21
 **/
@Controller
@RequestMapping("/work/deptAcount")
public class DeptAcountController extends BaseController{

    @Autowired
    private IProjectService projectService;

    private final String PREFIX="module/work";


    @RequestMapping
    public String toIndex(Model model){
        List<Project> projectList = projectService.list();
        model.addAttribute("projectList",projectList);
        return PREFIX+"/deptAcount";
    }
}
