package com.scsoft.xmgl.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.work.entity.Project;

import java.text.ParseException;
import java.util.List;

public interface IProjectService extends IService<Project> {
    PageResult<Project> list(int pageNum,int PageSize,String proId,String proName,String proManager,String manager,String proDept,String proSigner,Integer proEndState);
    PageResult<Project> list(int pageNum,int PageSize,String proId,String proName,String proManager,String proDept,String proSigner,String startDate,String endDate) throws ParseException;
    boolean add(Project project, List<Integer> userIds);
    boolean update(Project project,List<Integer> userIds);
    boolean delete(int proId);
}
