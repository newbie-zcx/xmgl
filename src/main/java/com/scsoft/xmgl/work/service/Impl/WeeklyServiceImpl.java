package com.scsoft.xmgl.work.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.common.exception.BusinessException;
import com.scsoft.xmgl.common.handler.SystemCommonHandler;
import com.scsoft.xmgl.common.utils.DateUtils;
import com.scsoft.xmgl.common.utils.PageResultUtils;
import com.scsoft.xmgl.work.entity.*;
import com.scsoft.xmgl.work.mapper.*;
import com.scsoft.xmgl.work.service.IUpdateLogService;
import com.scsoft.xmgl.work.service.IWeeklyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class WeeklyServiceImpl extends ServiceImpl<WeeklyMapper, Weekly> implements IWeeklyService {

    @Resource
    private WeeklyMapper weeklyMapper;
    @Resource
    private ProWeeklyMapper proWeeklyMapper;
    @Resource
    private WeeklyContentMapper weeklyContentMapper;
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private SCMapper scMapper;
    @Autowired
    private IUpdateLogService updateLogService;

    @Override
    public PageResult<Weekly> list(int pageNum, int PageSize,Integer proId,String startdate,String enddate) throws ParseException {
        QueryWrapper<Weekly> wrapper = new QueryWrapper<Weekly>();
        List<Weekly> weeklyList = new ArrayList<Weekly>();
        if (proId!=null){
            List<ProWeekly> proWeeklyList = proWeeklyMapper.getByProId(proId);
            List<Integer> weeklyIds = new ArrayList<Integer>();
            List<Integer> nullList = new ArrayList<Integer>();
            nullList.add(0);
            for (int x = 0;x<proWeeklyList.size();x++){
                weeklyIds.add(proWeeklyList.get(x).getWeeklyId());
            }
            Map<String, String> weekDate = DateUtils.getWeekDate();
            String monday = weekDate.get("mondayDate");
            String sunday = weekDate.get("sundayDate");
            Date startDate = DateUtils.StringToDate(monday);
            Date endDate = DateUtils.StringToDate(sunday);
            if (StringUtils.isNotBlank(startdate)){
                Date dateStart = DateUtils.StringToDate(startdate);
                startDate = dateStart;
            }
            if (StringUtils.isNotBlank(enddate)){
                Date dateEnd = DateUtils.StringToDate(enddate);
                endDate = dateEnd;
            }
            //查到本周的周报
            if (weeklyIds.size()>0){
                weeklyList = weeklyMapper.getByProId(weeklyIds,startDate,endDate);
            }else {
                weeklyList = weeklyMapper.getByProId(nullList,startDate,endDate);
            }
        }else {
            if (StringUtils.isNotBlank(startdate)){
                Date dateStart = DateUtils.StringToDate(startdate);
                wrapper.ge("start_date",dateStart);
            }
            if (StringUtils.isNotBlank(enddate)){
                Date dateEnd = DateUtils.StringToDate(enddate);
                wrapper.le("end_date",dateEnd);
            }
            wrapper.eq("user_name", SystemCommonHandler.getLoginUser().getRealName());
            weeklyList = weeklyMapper.selectList(wrapper);
        }
        Date firstDayOfMonth = DateUtils.getFirstDayOfMonth();
        Date endDayOfMonth = DateUtils.getEndDayOfMonth();
        int MonthHour = 0;
        int ProjectHour = 0;
        if (weeklyList.isEmpty()){
        }else {
            //获取月总工时以及项目总工时
            for (int j = 0;j<weeklyList.size();j++){
                String startDate = weeklyList.get(j).getStartDate();
                String endDate = weeklyList.get(j).getEndDate();
                Date dateStart = DateUtils.StringToDate(startDate);
                Date dateEnd = DateUtils.StringToDate(endDate);
                if (dateStart.getTime()>endDayOfMonth.getTime()||dateEnd.getTime()<firstDayOfMonth.getTime()){

                }else if (dateStart.getTime()>firstDayOfMonth.getTime()&&dateEnd.getTime()<endDayOfMonth.getTime()){
                    MonthHour += weeklyList.get(j).getThisMonthHour();
                }else if (dateStart.getTime()<firstDayOfMonth.getTime()&&dateEnd.getTime()>firstDayOfMonth.getTime()){
                    MonthHour += weeklyList.get(j).getThisMonthHour();
                }else if (dateStart.getTime()<endDayOfMonth.getTime()&&dateEnd.getTime()>endDayOfMonth.getTime()){
                    MonthHour += weeklyList.get(j).getLastMonthHour();
                }
                ProjectHour = ProjectHour + weeklyList.get(j).getLastMonthHour() +weeklyList.get(j).getThisMonthHour();
            }
            //获取项目信息、周报内容以及修改信息
            for (int i = 0;i<weeklyList.size();i++){
                Weekly weekly = weeklyList.get(i);
                Integer weeklyId = weekly.getId();
                //项目信息
                ProWeekly proWeekly = proWeeklyMapper.getByWeeklyId(weeklyId);
                Project project = projectMapper.selectById(proWeekly.getProId());
                weekly.setProject(project);
                //周报内容
                List<WeeklyContent> contentList = weeklyContentMapper.getByWeeklyId(weeklyId);
                List<SC> scList = new ArrayList<SC>();
                if (contentList.isEmpty()){
                }else {
                    for (int j = 0;j<contentList.size();j++){
                        SC sc = scMapper.selectById(contentList.get(j).getContentId());
                        scList.add(sc);
                    }
                }
                //修改信息
                //先查是否被修改，依据为update_log中是否有这样一条数据:entity_id=weekly.getId();update_table="weekly";update_row="lastMonthHour" or update_row="thisMonthHour"
                List<UpdateLog> lastList = updateLogService.isUpdate(weeklyId, "weekly", "lastMonthHour");
                List<UpdateLog> thisList = updateLogService.isUpdate(weeklyId, "weekly", "thisMonthHour");
                if (lastList.size()>0||thisList.size()>0){
                    //合并两个list
                    if (lastList.size()>0){
                        thisList.addAll(lastList);
                    }
                    if (thisList.size()>0){
                        weekly.setUpdateLogList(thisList);
                    }
                }
                weekly.setScList(scList.get(0).getContent());
                weekly.setStartDate(DateUtils.yyyyMMdd(weekly.getStartDate()));
                weekly.setEndDate(DateUtils.yyyyMMdd(weekly.getEndDate()));
                weekly.setMonthHour(MonthHour);
                weekly.setProjectHour(ProjectHour);
            }
        }
        return new PageResult<Weekly>(PageResultUtils.paginAtion(pageNum,PageSize,weeklyList),weeklyList.size());
    }

    public int returnMonthHour(List<Weekly> weeklyList) throws ParseException {
        Date firstDayOfMonth = DateUtils.getFirstDayOfMonth();
        Date endDayOfMonth = DateUtils.getEndDayOfMonth();
        int MonthHour = 0;
        if (weeklyList.isEmpty()) {
        } else {
            //获取月总工时以及项目总工时
            for (int j = 0; j < weeklyList.size(); j++) {
                String startDate = weeklyList.get(j).getStartDate();
                String endDate = weeklyList.get(j).getEndDate();
                Date dateStart = DateUtils.StringToDate(startDate);
                Date dateEnd = DateUtils.StringToDate(endDate);
                if (dateStart.getTime() > endDayOfMonth.getTime() || dateEnd.getTime() < firstDayOfMonth.getTime()) {

                } else if (dateStart.getTime() > firstDayOfMonth.getTime() && dateEnd.getTime() < endDayOfMonth.getTime()) {
                    MonthHour += weeklyList.get(j).getThisMonthHour();
                } else if (dateStart.getTime() < firstDayOfMonth.getTime() && dateEnd.getTime() > firstDayOfMonth.getTime()) {
                    MonthHour += weeklyList.get(j).getThisMonthHour();
                } else if (dateStart.getTime() < endDayOfMonth.getTime() && dateEnd.getTime() > endDayOfMonth.getTime()) {
                    MonthHour += weeklyList.get(j).getLastMonthHour();
                }
            }
        }
        return MonthHour;
    }

    @Override
    public boolean add(Weekly weekly,int scId,int proId) throws ParseException {
        Date startDate = DateUtils.StringToDate(weekly.getStartDate());
        Date endDate = DateUtils.StringToDate(weekly.getEndDate());
        weekly.setRemark(Integer.toString(proId));
        QueryWrapper<Weekly> wrapper = new QueryWrapper<Weekly>();
        wrapper.eq("start_date",startDate);
        wrapper.eq("end_date",endDate);
        wrapper.eq("user_name",weekly.getUserName());
        wrapper.eq("remark",weekly.getRemark());
        if (weeklyMapper.selectCount(wrapper)>0){
            scMapper.deleteById(scId);
            throw new BusinessException("周报已经存在！");
        }
        boolean result = weeklyMapper.insert(weekly) > 0;
        if (result){
            Integer weeklyId = weekly.getId();
            //插入数据至项目-周报表
            if (proWeeklyMapper.add(proId,weeklyId)<1){
                throw new BusinessException("添加失败，请重试");
            }
            //插入数据至周报-周报内容表
            if (weeklyContentMapper.addContent(weeklyId,scId)<1){
                throw new BusinessException("添加失败，请重试");
            }
        }
        return result;
    }

    @Override
    public boolean update(Weekly weekly, int scId, int proId) throws ParseException {
        boolean result = weeklyMapper.updateById(weekly) > 0;
        if (result) {
            Integer weeklyId = weekly.getId();
            //更新数据至项目-周报表
            if (proWeeklyMapper.update(proId,weeklyId)<1){
                throw new BusinessException("修改失败，请重试");
            }
            //插入数据至周报-周报内容表
            if (weeklyContentMapper.addContent(weeklyId,scId)<1){
                throw new BusinessException("修改失败，请重试");
            }
        }
        return result;
    }

    @Override
    public boolean delete(int weeklyId) {
        boolean result = weeklyMapper.deleteById(weeklyId) > 0;
        if (result){
            if (proWeeklyMapper.deleteByWeeklyId(weeklyId)<1){
                throw new BusinessException("删除失败，请重试");
            }
            List<WeeklyContent> scList = weeklyContentMapper.selectList(new QueryWrapper<WeeklyContent>().eq("weekly_id", weeklyId));
            for (int i = 0;i<scList.size();i++){
                scMapper.delete(new QueryWrapper<SC>().eq("id",scList.get(i).getContentId()));
            }
            if (weeklyContentMapper.deleteByWeeklyId(weeklyId)<1){
                throw new BusinessException("删除失败，请重试");
            }
        }
        return result;
    }

    @Override
    public List<Weekly> getListById(List<ProWeekly> proWeeklyList) {
        List<Weekly> weeklyList = new ArrayList<Weekly>();
        for (int i = 0;i<proWeeklyList.size();i++){
            Weekly weekly = weeklyMapper.selectById(proWeeklyList.get(i).getWeeklyId());
            weeklyList.add(weekly);
        }
        return weeklyList;
    }

    @Override
    public boolean updateHour(Weekly weekly) {
        return weeklyMapper.updateById(weekly) > 0;
    }
}
