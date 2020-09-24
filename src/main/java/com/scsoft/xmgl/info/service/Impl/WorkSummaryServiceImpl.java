package com.scsoft.xmgl.info.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.scpt.exception.ParameterException;
import com.scsoft.xmgl.common.exception.BusinessException;
import com.scsoft.xmgl.info.entity.Info;
import com.scsoft.xmgl.info.entity.TaskSummary;
import com.scsoft.xmgl.info.entity.WorkSummary;
import com.scsoft.xmgl.info.mapper.TaskSummaryMapper;
import com.scsoft.xmgl.info.mapper.UnfinishedProjectMapper;
import com.scsoft.xmgl.info.mapper.WorkSummaryMapper;
import com.scsoft.xmgl.info.service.IWorkSummaryService;
import com.scsoft.xmgl.system.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WorkSummaryServiceImpl extends ServiceImpl<WorkSummaryMapper, WorkSummary> implements IWorkSummaryService {

    @Resource
    private WorkSummaryMapper workSummaryMapper;
    @Resource
    private TaskSummaryMapper taskSummaryMapper;
    @Resource
    private UnfinishedProjectMapper unfinishedProjectMapper;

    @Override
    public boolean add(WorkSummary workSummary) throws BusinessException, ParseException {
        workSummary.setProCompletion(unfinishedProjectMapper.selectByProName(workSummary.getProName()).getProCompletion());
        String beginDate = workSummary.getBeginDate();
        String endDate = workSummary.getEndDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate2 = sdf.parse(beginDate);
        Date endDate2 = sdf.parse(endDate);
        WorkSummary workSummary1 = workSummaryMapper.isExit(workSummary.getUserName(), workSummary.getProName(), beginDate2, endDate2);
        if (workSummary1==null){
            return workSummaryMapper.insert(workSummary) > 0;
        }else {
            return false;
        }
    }

    @Override
    public PageResult<WorkSummary> list(int pageNum, int pageSize, String realName,String addTime,String userName) {
        QueryWrapper<WorkSummary> wrapper = new QueryWrapper<WorkSummary>();
        if (StringUtils.isNotBlank(addTime)) {
            wrapper.like("add_time", addTime);
        }
        if (StringUtils.isNotBlank(userName)) {
            wrapper.like("user_name", userName);
        }
        if(realName!=null){
            wrapper.eq("user_name", realName);
        }
        wrapper.eq("examine_status", 0);
        Page<WorkSummary> workSummaryPage = new Page<WorkSummary>(pageNum,pageSize);
        List<WorkSummary> workSummaryList = workSummaryMapper.selectPage(workSummaryPage, wrapper).getRecords();
        return new PageResult<>(workSummaryList, workSummaryPage.getTotal());
    }
    @Override
    public PageResult<WorkSummary> list1(int pageNum, int pageSize, String realName,String addTime,String userName,String proName,int examineStatus,int examineStatusDept) {
        QueryWrapper<WorkSummary> wrapper = new QueryWrapper<WorkSummary>();
        if (StringUtils.isNotBlank(addTime)) {
            wrapper.like("add_time", addTime);
        }
        if (StringUtils.isNotBlank(userName)) {
            wrapper.like("user_name", userName);
        }
        if (StringUtils.isNotBlank(proName)) {
            wrapper.like("pro_name", proName);
        }
        if(realName!=null){
            wrapper.eq("user_name", realName);
        }
        wrapper.eq("examine_status", examineStatus);
        wrapper.eq("examine_status_dept", examineStatusDept);
        Page<WorkSummary> workSummaryPage = new Page<WorkSummary>(pageNum,pageSize);
        List<WorkSummary> workSummaryList = workSummaryMapper.selectPage(workSummaryPage, wrapper).getRecords();
        return new PageResult<>(workSummaryList, workSummaryPage.getTotal());
    }
    @Override
    public PageResult<WorkSummary> list2(int pageNum, int pageSize, String realName,String addTime,String userName) {
        QueryWrapper<WorkSummary> wrapper = new QueryWrapper<WorkSummary>();
        if (StringUtils.isNotBlank(addTime)) {
            wrapper.like("add_time", addTime);
        }
        if (StringUtils.isNotBlank(userName)) {
            wrapper.like("user_name", userName);
        }
        if(realName!=null){
            wrapper.eq("user_name", realName);
        }
        Page<WorkSummary> workSummaryPage = new Page<WorkSummary>(pageNum,pageSize);
        List<WorkSummary> workSummaryList = workSummaryMapper.selectPage(workSummaryPage, wrapper).getRecords();
        return new PageResult<>(workSummaryList, workSummaryPage.getTotal());
    }
    @Override
    public boolean update(WorkSummary workSummary) {
        workSummary.setProCompletion(unfinishedProjectMapper.selectByProName(workSummary.getProName()).getProCompletion());
        return workSummaryMapper.updateById(workSummary) > 0 ;
    }

    @Override
    public boolean delete(int id) throws ParseException {
        WorkSummary workSummary = queryById(id);
        String proName = workSummary.getProName();
        String userName = workSummary.getUserName();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Date endDate = new Date();
        beginDate = sdf.parse(workSummary.getBeginDate());;
        endDate = sdf.parse(workSummary.getEndDate());;
        Map<String,Object> map = new HashMap<>();
        map.put("proName",proName);
        map.put("userName",userName);
        map.put("beginDate",beginDate);
        map.put("endDate",endDate);
        boolean b = taskSummaryMapper.deleteByWorkSummary(map);
        boolean flag = b;
        return workSummaryMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateState(int id, int examineStatus,int examineStatusDept,int checkStatus,int checkStatusDept) throws ParameterException {
        WorkSummary workSummary = workSummaryMapper.selectById(id);
        workSummary.setExamineStatus(examineStatus);
        workSummary.setExamineStatusDept(examineStatusDept);
        workSummary.setCheckStatus(checkStatus);
        workSummary.setCheckStatusDept(checkStatusDept);
        if (examineStatusDept != 0 && examineStatusDept != 1) {
            throw new ParameterException("examineStatus值需要在[0,1]中");
        }
        if (examineStatusDept != 0 && examineStatusDept != 1) {
            throw new ParameterException("examineStatusDept值需要在[0,1]中");
        }
        return workSummaryMapper.updateById(workSummary) > 0;
    }

    @Override
    public WorkSummary queryById(int id) {
        return workSummaryMapper.selectById(id);
    }

    @Override
    public Info queryByProName(String proName) {
        return unfinishedProjectMapper.selectByProName(proName);
    }

    @Override
    public boolean addComment(WorkSummary workSummary) {
        return workSummaryMapper.addComment(workSummary);
    }

    @Override
    public List<WorkSummary> getByuserName(String userName) {
        return workSummaryMapper.getByuserName(userName);
    }

    @Override
    public List<WorkSummary> getByproName(String proName) {
        QueryWrapper<WorkSummary> wrapper = new QueryWrapper<WorkSummary>();
        wrapper.eq("pro_name", proName);
        return workSummaryMapper.selectList(wrapper);
    }

    @Override
    public List<WorkSummary> getByThree1(String proName, String beginDate, String endDate) {
        QueryWrapper<WorkSummary> wrapper = new QueryWrapper<WorkSummary>();
        if (StringUtils.isNotBlank(proName)) {
            wrapper.eq("pro_name", proName);
        }
        wrapper.ge("begin_date",beginDate);
        wrapper.le("end_date",endDate);
        return workSummaryMapper.selectList(wrapper);
    }
    @Override
    public List<WorkSummary> getByThree2(String proName, String beginDate, String endDate,int examineStatus) {
        QueryWrapper<WorkSummary> wrapper = new QueryWrapper<WorkSummary>();
        if (StringUtils.isNotBlank(proName)) {
            wrapper.eq("pro_name", proName);
        }
        wrapper.eq("examine_status", examineStatus);
        wrapper.ge("begin_date",beginDate);
        wrapper.le("end_date",endDate);
        return workSummaryMapper.selectList(wrapper);
    }
    @Override
    public List<WorkSummary> getByThree3(String proName, String beginDate, String endDate,int examineStatus,int examineStatusDept) {
        QueryWrapper<WorkSummary> wrapper = new QueryWrapper<WorkSummary>();
        if (StringUtils.isNotBlank(proName)) {
            wrapper.eq("pro_name", proName);
        }
        wrapper.eq("examine_status", examineStatus);
        wrapper.eq("examine_status_dept", examineStatusDept);
        wrapper.ge("begin_date",beginDate);
        wrapper.le("end_date",endDate);
        return workSummaryMapper.selectList(wrapper);
    }

}
