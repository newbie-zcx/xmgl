package com.scsoft.xmgl.work.service.Impl;

import cn.hutool.core.date.Week;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.common.exception.BusinessException;
import com.scsoft.xmgl.common.handler.SystemCommonHandler;
import com.scsoft.xmgl.common.utils.DateUtils;
import com.scsoft.xmgl.common.utils.PageResultUtils;
import com.scsoft.xmgl.system.entity.Depart;
import com.scsoft.xmgl.system.entity.Role;
import com.scsoft.xmgl.system.entity.User;
import com.scsoft.xmgl.system.entity.UserRole;
import com.scsoft.xmgl.system.mapper.DepartMapper;
import com.scsoft.xmgl.system.mapper.RoleMapper;
import com.scsoft.xmgl.system.mapper.UserRoleMapper;
import com.scsoft.xmgl.work.entity.ProWeekly;
import com.scsoft.xmgl.work.entity.Project;
import com.scsoft.xmgl.work.entity.Weekly;
import com.scsoft.xmgl.work.entity.WeeklyContent;
import com.scsoft.xmgl.work.mapper.ProUserMapper;
import com.scsoft.xmgl.work.mapper.ProWeeklyMapper;
import com.scsoft.xmgl.work.mapper.ProjectMapper;
import com.scsoft.xmgl.work.mapper.WeeklyMapper;
import com.scsoft.xmgl.work.service.IProjectService;
import com.scsoft.xmgl.work.service.IWeeklyService;
import org.apache.commons.lang3.StringUtils;
import org.junit.experimental.theories.FromDataPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private WeeklyMapper weeklyMapper;
    @Resource
    private ProUserMapper proUserMapper;
    @Resource
    private ProWeeklyMapper proWeeklyMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private DepartMapper departMapper;
    @Autowired
    private IWeeklyService weeklyService;

    @Override
    public PageResult<Project> list(int pageNum, int PageSize,String proId,String proName,String proManager,String manager,String proDept,String proSigner,Integer proEndState) {
        QueryWrapper<Project> wrapper = new QueryWrapper<Project>();
        User user = SystemCommonHandler.getLoginUser();
        List<Depart> departList = departMapper.selectByUserId(user.getId());
        List<Role> roleList = roleMapper.selectByUserId(user.getId());
        if (roleList.size()>0){
            String code = roleList.get(0).getCode();
            if (code.equals("101")){
                //超级管理
                if (StringUtils.isNotBlank(proId)){
                    wrapper.like("pro_id",proId);
                }
                if (StringUtils.isNotBlank(proName)){
                    wrapper.like("pro_name",proName);
                }
                if (StringUtils.isNotBlank(manager)){
                    wrapper.eq("manager",manager);
                }
                if (StringUtils.isNotBlank(proManager)){
                    wrapper.eq("pro_manager",proManager);
                }
                if (StringUtils.isNotBlank(proDept)){
                    wrapper.eq("pro_dept",proDept);
                }
                if (StringUtils.isNotBlank(proSigner)){
                    wrapper.like("pro_signer",proSigner);
                }
                if (proEndState!=null){
                    wrapper.eq("pro_end_state",proEndState);
                }
            }else if (code.equals("102")||code.equals("108")){
                //领导组或普通管理员
                if (StringUtils.isNotBlank(proId)){
                    wrapper.like("pro_id",proId);
                }
                if (StringUtils.isNotBlank(proName)){
                    wrapper.like("pro_name",proName);
                }
                if (StringUtils.isNotBlank(proManager)){
                    wrapper.eq("pro_manager",proManager);
                }
                if (StringUtils.isNotBlank(manager)){
                    wrapper.eq("manager",manager);
                }
                if (StringUtils.isNotBlank(proDept)){
                    wrapper.eq("pro_dept",proDept);
                }
                if (StringUtils.isNotBlank(proSigner)){
                    wrapper.like("pro_signer",proSigner);
                }
                if (proEndState!=null){
                    wrapper.eq("pro_end_state",proEndState);
                }
            }else if (code.equals("103")){
                //部门经理
                if (StringUtils.isNotBlank(proId)){
                    wrapper.like("pro_id",proId);
                }
                if (StringUtils.isNotBlank(proName)){
                    wrapper.like("pro_name",proName);
                }
                if (StringUtils.isNotBlank(proManager)){
                    wrapper.eq("pro_manager",proManager);
                }
                if (StringUtils.isNotBlank(manager)){
                    wrapper.eq("manager",manager);
                }
                if (StringUtils.isNotBlank(proSigner)){
                    wrapper.like("pro_signer",proSigner);
                }
                if (proEndState!=null){
                    wrapper.eq("pro_end_state",proEndState);
                }
                wrapper.eq("pro_dept",departList.get(0).getDepartName()).or().eq("pro_dept","【技术】联合开发");
            }else if (code.equals("104")){
                //项目经理
                if (StringUtils.isNotBlank(proId)){
                    wrapper.like("pro_id",proId);
                }
                if (StringUtils.isNotBlank(proName)){
                    wrapper.like("pro_name",proName);
                }
                if (StringUtils.isNotBlank(manager)){
                    wrapper.eq("manager",manager);
                }
                if (StringUtils.isNotBlank(proDept)){
                    wrapper.eq("pro_dept",proDept);
                }
                if (StringUtils.isNotBlank(proSigner)){
                    wrapper.like("pro_signer",proSigner);
                }
                if (proEndState!=null){
                    wrapper.eq("pro_end_state",proEndState);
                }
                wrapper.eq("pro_manager", user.getRealName());
            }
        }
        Page<Project> projectPage = new Page<Project>(pageNum,PageSize);
        List<Project> projectList = projectMapper.selectPage(projectPage, wrapper.orderByDesc("create_date")).getRecords();
        return new PageResult<>(projectList,projectPage.getTotal());
    }

    @Override
    public PageResult<Project> list(int pageNum, int PageSize,String proId,String proName,String proManager,String proDept,String proSigner,String startDate,String endDate) throws ParseException {
        Map<String, String> weekDate = DateUtils.getWeekDate();
        String mondayDate = weekDate.get("mondayDate");
        String sundayDate = weekDate.get("sundayDate");
        Date dateMonday = DateUtils.StringToDate(mondayDate);
        Date dateSunday = DateUtils.StringToDate(sundayDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateStart = sdf.parse("1998-01-01");
        Date dateEnd = new Date();
        if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
            dateStart = DateUtils.StringToDate(startDate);
            dateEnd = DateUtils.StringToDate(endDate);
        }
        QueryWrapper<Project> wrapper = new QueryWrapper<Project>();
        if (StringUtils.isNotBlank(proId)){
            wrapper.like("pro_id",proId);
        }
        if (StringUtils.isNotBlank(proName)){
            wrapper.like("pro_name",proName);
        }
        if (StringUtils.isNotBlank(proManager)){
            wrapper.eq("pro_manager",proManager);
        }
        if (StringUtils.isNotBlank(proDept)){
            wrapper.eq("pro_dept",proDept);
        }
        if (StringUtils.isNotBlank(proSigner)){
            wrapper.like("pro_signer",proSigner);
        }
        User user = SystemCommonHandler.getLoginUser();
        List<Depart> departList = departMapper.selectByUserId(user.getId());
        List<Role> roleList = roleMapper.selectByUserId(user.getId());
        if (roleList.size() > 0) {
            String code = roleList.get(0).getCode();
            if (code.equals("101")) {
                //超级管理
                if (StringUtils.isNotBlank(proId)) {
                    wrapper.like("pro_id", proId);
                }
                if (StringUtils.isNotBlank(proName)) {
                    wrapper.like("pro_name", proName);
                }
                if (StringUtils.isNotBlank(proManager)) {
                    wrapper.eq("pro_manager", proManager);
                }
                if (StringUtils.isNotBlank(proDept)) {
                    wrapper.eq("pro_dept", proDept);
                }
                if (StringUtils.isNotBlank(proSigner)) {
                    wrapper.like("pro_signer", proSigner);
                }
            } else if (code.equals("102") || code.equals("108")) {
                //领导组或普通管理员
                if (StringUtils.isNotBlank(proId)) {
                    wrapper.like("pro_id", proId);
                }
                if (StringUtils.isNotBlank(proName)) {
                    wrapper.like("pro_name", proName);
                }
                if (StringUtils.isNotBlank(proManager)) {
                    wrapper.eq("pro_manager", proManager);
                }
                if (StringUtils.isNotBlank(proDept)) {
                    wrapper.eq("pro_dept", proDept);
                }
                if (StringUtils.isNotBlank(proSigner)) {
                    wrapper.like("pro_signer", proSigner);
                }
            } else if (code.equals("103")) {
                //部门经理
                if (StringUtils.isNotBlank(proId)) {
                    wrapper.like("pro_id", proId);
                }
                if (StringUtils.isNotBlank(proName)) {
                    wrapper.like("pro_name", proName);
                }
                if (StringUtils.isNotBlank(proManager)) {
                    wrapper.eq("pro_manager", proManager);
                }
                if (StringUtils.isNotBlank(proSigner)) {
                    wrapper.like("pro_signer", proSigner);
                }
                wrapper.eq("pro_dept", departList.get(0).getDepartName()).or().eq("pro_dept", "【技术】联合开发");
            } else if (code.equals("104")) {
                //项目经理
                if (StringUtils.isNotBlank(proId)) {
                    wrapper.like("pro_id", proId);
                }
                if (StringUtils.isNotBlank(proName)) {
                    wrapper.like("pro_name", proName);
                }
                if (StringUtils.isNotBlank(proSigner)) {
                    wrapper.like("pro_signer", proSigner);
                }
                wrapper.eq("pro_manager", user.getRealName());
            }
        }
            List<Project> projectList = projectMapper.selectList(wrapper.orderByDesc("create_date"));
            QueryWrapper<Weekly> weeklyQueryWrapper = new QueryWrapper<Weekly>();
            List<Integer> weeklyIdList = new ArrayList<Integer>();
            for (int i = 0; i < projectList.size(); i++) {
                List<Weekly> weeklyList = new ArrayList<Weekly>();
                List<Weekly> weeklyList1 = new ArrayList<Weekly>();
                List<ProWeekly> proWeeklies = proWeeklyMapper.getByProId(projectList.get(i).getId());
                for (int j = 0; j < proWeeklies.size(); j++) {
                    Integer weeklyId = proWeeklies.get(j).getWeeklyId();
                    weeklyIdList.add(weeklyId);
                }
                if (weeklyIdList.size() > 0) {
                    if (dateStart == null && dateEnd == null) {
                        weeklyList = weeklyMapper.selectBatchIds(weeklyIdList);
                    } else {
                        weeklyList = weeklyMapper.getHour(weeklyIdList, dateStart, dateEnd);
                        weeklyList1 = weeklyMapper.getHour(weeklyIdList, dateMonday, dateSunday);
                    }
                }
                int projectHour = 0;
                int monthHour = 0;
                int lastWeekHour = 0;
                int thisWeekHour = 0;
                if (weeklyList.size() > 0) {
                    for (int x = 0; x < weeklyList.size(); x++) {
                        Weekly weekly = weeklyList.get(x);
                        projectHour = projectHour + weekly.getLastMonthHour() + weekly.getThisMonthHour();
                    }
                }
                if (weeklyList1.size() > 0) {
                    for (int x = 0; x < weeklyList1.size(); x++) {
                        Weekly weekly = weeklyList.get(x);
                        lastWeekHour = lastWeekHour + weekly.getLastMonthHour();
                        thisWeekHour = thisWeekHour + weekly.getThisMonthHour();
                    }
                }
                monthHour = weeklyService.returnMonthHour(weeklyList);
                projectList.get(i).setProjectHour(projectHour);
                projectList.get(i).setMonthHour(monthHour);
                projectList.get(i).setLastMonthHour(lastWeekHour);
                projectList.get(i).setThisMonthHour(thisWeekHour);
            }
            return new PageResult<Project>(PageResultUtils.paginAtion(pageNum, PageSize, projectList));
    }
    @Override
    public boolean add(Project project,List<Integer> userIds) {
        if (projectMapper.getByProName(project.getProName()) != null){
            throw new BusinessException("项目已经存在！");
        }
        boolean result = projectMapper.insert(project) > 0;
        if (result){
            //插入数据至项目-用户关联表
            Integer proId = project.getId();
            if (proUserMapper.addMember(proId,userIds) < userIds.size()){
                throw  new BusinessException("添加失败，请重试");
            }
        }
        return result;
    }

    @Override
    public boolean update(Project project, List<Integer> userIds) {
        boolean result = projectMapper.updateById(project)>0;
        if (result){
            //更新项目-用户关联表的数据，即先删除后新增
            Integer proId = project.getId();
            proUserMapper.deleteByProId(proId);
            if (proUserMapper.addMember(proId,userIds) < userIds.size()){
                throw  new BusinessException("添加失败，请重试");
            }
        }
        return result;
    }

    @Override
    public boolean delete(int proId) {
        boolean result = false;
        boolean flag = false;
        result = projectMapper.deleteById(proId) > 0;
        if (result){
            flag = proUserMapper.deleteByProId(proId);
        }
        return result;
    }
}
