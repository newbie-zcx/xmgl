package com.scsoft.xmgl.info.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.common.handler.SystemCommonHandler;
import com.scsoft.xmgl.common.utils.DateUtils;
import com.scsoft.xmgl.info.entity.TaskSummary;
import com.scsoft.xmgl.info.mapper.TaskSummaryMapper;
import com.scsoft.xmgl.info.service.ITaskSummaryService;
import com.scsoft.xmgl.system.entity.User;
import com.scsoft.xmgl.system.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskSummaryServiceImpl extends ServiceImpl<TaskSummaryMapper, TaskSummary> implements ITaskSummaryService {
    @Resource
    private TaskSummaryMapper taskSummaryMapper;

    @Override
    public PageResult<TaskSummary> list1(int pageNum, int pageSize,String proName,String beginDate,String endDate,String userName) throws ParseException {
        QueryWrapper<TaskSummary> wrapper = new QueryWrapper<TaskSummary>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate1 = sdf.parse(beginDate);
        Date endDate1 = sdf.parse(endDate);
        if (StringUtils.isNotBlank(userName)) {
            wrapper.eq("user_name", userName);
        }
        if (StringUtils.isNotBlank(proName)) {
            wrapper.eq("pro_name", proName);
        }
        wrapper.eq("task_type", 0);
        wrapper.ge("begin_date", beginDate1);
        wrapper.le("end_date", endDate1);
        Page<TaskSummary> taskSummaryPage = new Page<TaskSummary>(pageNum, pageSize);
        List<TaskSummary> taskSummaryList = taskSummaryMapper.selectPage(taskSummaryPage, wrapper).getRecords();
        return new PageResult<>(taskSummaryList, taskSummaryPage.getTotal());
    }
    @Override
    public PageResult<TaskSummary> list2(int pageNum, int pageSize,String proName,String beginDate,String endDate,String userName) throws ParseException {
        QueryWrapper<TaskSummary> wrapper = new QueryWrapper<TaskSummary>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate2 = sdf.parse(DateUtils.getDateAfterNDays(beginDate,7));
        Date endDate2 = sdf.parse(DateUtils.getDateAfterNDays(endDate,7));
        if (StringUtils.isNotBlank(userName)) {
            wrapper.eq("user_name", userName);
        }
        if (StringUtils.isNotBlank(proName)) {
            wrapper.eq("pro_name", proName);
        }
        wrapper.eq("task_type", 1);
        wrapper.eq("begin_date", beginDate2);
        wrapper.eq("end_date", endDate2);
        Page<TaskSummary> taskSummaryPage = new Page<TaskSummary>(pageNum, pageSize);
        List<TaskSummary> taskSummaryList = taskSummaryMapper.selectPage(taskSummaryPage, wrapper).getRecords();
        return new PageResult<>(taskSummaryList, taskSummaryPage.getTotal());
    }

    @Override
    public boolean add(TaskSummary taskSummary) {
        return taskSummaryMapper.insert(taskSummary)>0;
    }

    @Override
    public boolean update(TaskSummary taskSummary) {
        return taskSummaryMapper.updateById(taskSummary)>0;
    }

    @Override
    public boolean delete(int id) {
        return taskSummaryMapper.deleteById(id)>0;
    }

    @Override
    public List<TaskSummary> getByThree(int taskType,String memberName, String proName, String beginDate, String endDate) throws ParseException {
        QueryWrapper<TaskSummary> wrapper = new QueryWrapper<TaskSummary>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate2 = new Date();
        Date endDate2 = new Date();
        if (taskType==0){
            beginDate2 = sdf.parse(beginDate);
            endDate2 = sdf.parse(endDate);
        }else {
            beginDate2 = sdf.parse(DateUtils.getDateAfterNDays(beginDate,7));
            endDate2 = sdf.parse(DateUtils.getDateAfterNDays(endDate,7));
        }
        if (StringUtils.isNotBlank(memberName)) {
            wrapper.eq("user_name", memberName);
        }
        if (StringUtils.isNotBlank(proName)) {
            wrapper.eq("pro_name", proName);
        }
        wrapper.eq("task_type", taskType);
        wrapper.ge("begin_date", beginDate2);
        wrapper.le("end_date", endDate2);
        return taskSummaryMapper.selectList(wrapper);
    }
}
