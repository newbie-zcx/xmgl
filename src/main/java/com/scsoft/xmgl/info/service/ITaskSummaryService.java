package com.scsoft.xmgl.info.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.info.entity.TaskSummary;

import java.text.ParseException;
import java.util.List;

public interface ITaskSummaryService extends IService<TaskSummary> {
    PageResult<TaskSummary> list1(int pageNum, int pageSize,String proName,String beginDate,String endDate,String userName) throws ParseException;
    PageResult<TaskSummary> list2(int pageNum, int pageSize,String proName,String beginDate,String endDate,String userName) throws ParseException;
    boolean add(TaskSummary taskSummary);
    boolean update(TaskSummary taskSummary);
    boolean delete(int id);
    List<TaskSummary> getByThree(int taskType,String memberName ,String proName,String beginDate,String endDate) throws ParseException;
}

