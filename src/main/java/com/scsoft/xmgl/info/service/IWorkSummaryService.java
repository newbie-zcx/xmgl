package com.scsoft.xmgl.info.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.scpt.exception.ParameterException;
import com.scsoft.xmgl.info.entity.Info;
import com.scsoft.xmgl.info.entity.WorkSummary;

import java.text.ParseException;
import java.util.List;

public interface IWorkSummaryService extends IService<WorkSummary> {
    boolean add(WorkSummary workSummary) throws ParseException;
    PageResult<WorkSummary> list(int pageNum, int pageSize,String realName,String addTime,String userName);
    PageResult<WorkSummary> list1(int pageNum, int pageSize,String realName,String addTime,String userName,String proName,int examineStatus,int examineStatusDept);
    PageResult<WorkSummary> list2(int pageNum, int pageSize,String realName,String addTime,String userName);
    boolean update(WorkSummary workSummary);
    boolean delete(int id) throws ParseException;
    boolean updateState(int id, int examineStatus,int examineStatusDept,int checkStatus,int checkStatusDept) throws ParameterException;
    WorkSummary queryById(int id);
    Info queryByProName(String proName);
    boolean addComment(WorkSummary workSummary);
    List<WorkSummary> getByuserName(String userName);
    List<WorkSummary> getByproName(String proName);
    List<WorkSummary> getByThree1(String proName,String beginDate,String endDate);
    List<WorkSummary> getByThree2(String proName,String beginDate,String endDate,int examineStatus);
    List<WorkSummary> getByThree3(String proName,String beginDate,String endDate,int examineStatus,int examineStatusDept);
}
