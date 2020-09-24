package com.scsoft.xmgl.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.xmgl.common.handler.SystemCommonHandler;
import com.scsoft.xmgl.common.shiro.EndecryptUtil;
import com.scsoft.xmgl.common.utils.DateUtils;
import com.scsoft.xmgl.common.utils.PageResultUtils;
import com.scsoft.xmgl.generator.utils.Query;
import com.scsoft.xmgl.system.entity.*;
import com.scsoft.xmgl.system.mapper.*;
import com.scsoft.xmgl.system.service.IUserService;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.scpt.exception.BusinessException;
import com.scsoft.scpt.exception.ParameterException;
import com.scsoft.xmgl.work.entity.ProUser;
import com.scsoft.xmgl.work.entity.Weekly;
import com.scsoft.xmgl.work.mapper.ProUserMapper;
import com.scsoft.xmgl.work.mapper.WeeklyMapper;
import com.scsoft.xmgl.work.service.IWeeklyService;
import org.apache.commons.lang3.StringUtils;
import org.junit.experimental.theories.FromDataPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 系统用户表 服务实现类
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements IUserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private UserDepartMapper userDepartMapper;
    @Resource
    private ProUserMapper proUserMapper;
    @Resource
    private DepartMapper departMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private WeeklyMapper weeklyMapper;
    @Autowired
    private IWeeklyService weeklyService;
    
    
    @Override
    public List<Map<String, Object>> selectUsers(String name, String beginTime, String endTime, int deptid) {

        return null;
    }


    @Override
    public User getByUsername(String username) {
        return userMapper.getByUsername(username);
    }

    @Override
    public User getByRealName(String realName) {
        return userMapper.getByRealName(realName);
    }

    @Override
    public PageResult<User> list(int pageNum, int pageSize, boolean showDelete, String column, String value,String departId) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        if (StringUtils.isNotBlank(column)) {
            wrapper.like(column, value);
        }
        if (!showDelete) {  // 不显示锁定的用户
            wrapper.eq("state", 0);
        }
        Page<User> userPage = new Page<User>(pageNum, pageSize);

        List<User> userList  = userMapper.selectPage(userPage, wrapper.inSql("id","select user_id from sys_user_depart where depart_id ="+departId).orderByDesc("create_date")).getRecords();
            if (userList != null && userList.size() > 0) {
            // 查询user的角色
            List<UserRole> userRoles = userRoleMapper.selectByUserIds(getUserIds(userList));
            for (User one : userList) {
                List<Role> tempURs = new ArrayList<>();
                for (UserRole ur : userRoles) {
                    if (one.getId().equals(ur.getUserId())) {
                        tempURs.add(new Role(ur.getRoleId(), ur.getRoleName()));
                    }
                }
                one.setRoles(tempURs);
            }
        }
        return new PageResult<User>(userList,userPage.getTotal() );
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean add(User user, String departId) throws BusinessException {
        if (userMapper.getByUsername(user.getUserName()) != null) {
            throw new BusinessException("账号已经存在");
        }
        user.setSalt(EndecryptUtil.getRandomSalt(6));
        user.setPassword(EndecryptUtil.encrytMd5(user.getPassword(), user.getUserName()+user.getSalt(), 1024));
        user.setStatus(0);
        boolean rs = userMapper.insert(user) > 0;
        if (rs) {
            UserDepart userDepart=new UserDepart();
            userDepart.setDepartId(Integer.valueOf(departId));
            userDepart.setUserId(user.getId());
            userDepartMapper.insert(userDepart);
            List<Integer> roleIds = getRoleIds(user.getRoles());
            if (userRoleMapper.insertBatch(user.getId(), roleIds) < roleIds.size()) {
                throw new BusinessException("添加失败，请重试");
            }
        }
        return rs;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(User user) {
        boolean rs = userMapper.updateById(user) > 0;
        if (rs) {
            userRoleMapper.delete(new QueryWrapper<UserRole>().eq("user_id", user.getId()));
            List<Integer> roleIds = getRoleIds(user.getRoles());
            if (userRoleMapper.insertBatch(user.getId(), roleIds) < roleIds.size()) {
                throw new BusinessException("修改失败，请重试");
            }
        }
        return rs;
    }

    @Override
    public boolean updateUser(User user) {
        return userMapper.updateById(user)>0;
    }

    /**
     * 添加用户角色
     */
    private List<Integer> getRoleIds(List<Role> roles) {
        List<Integer> rs = new ArrayList<>();
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                rs.add(role.getId());
            }
        }
        return rs;
    }

    @Override
    public boolean updateState(int userId, int state) throws ParameterException {
        if (state != 0 && state != 1) {
            throw new ParameterException("state值需要在[0,1]中");
        }
        User user = userMapper.selectById(userId);
        user.setStatus(state);
        return userMapper.updateById(user) > 0;
    }

    @Override
    public boolean updatePsw(int userId, String username, String password) {
        User user = userMapper.selectById(userId);
        user.setPassword(EndecryptUtil.encrytMd5(password, user.getUserName()+user.getSalt(), 1024));
        return userMapper.updateById(user) > 0;
    }

    @Override
    public User getById(int userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public boolean delete(int userId) {
        return userMapper.deleteById(userId) > 0;
    }

    @Override
    public User getRealNameByLoginName(String LoginName) {
        return userMapper.getByUsername(LoginName);
    }

    @Override
    public List<User> getRealNameBySection(String section) {
        return userMapper.getBySection(section);
    }

    @Override
    public List<Role> getRoleByid(int id) {
        return userMapper.getRoleByid(id);
    }

    @Override
    public PageResult<User> countList(int pageNum,int pageSize,Integer proId, String realName, String startDate, String endDate) throws ParseException {
        Map<String, String> weekDate = DateUtils.getWeekDate();
        String mondayDate = weekDate.get("mondayDate");
        String sundayDate = weekDate.get("sundayDate");
        Date dateMonday = DateUtils.StringToDate(mondayDate);
        Date dateSunday = DateUtils.StringToDate(sundayDate);
        if (proId==null){
            List<User> userList = new ArrayList<User>();
            QueryWrapper<User> wrapper = new QueryWrapper<User>();
            if (StringUtils.isNotBlank(realName)){
                wrapper.like("real_name",realName);
            }
            User user = SystemCommonHandler.getLoginUser();
            wrapper.like("real_name",user.getRealName());
            userList = userMapper.selectList(wrapper);
            List<Depart> departList = departMapper.selectByUserId(user.getId());
            List<Role> roleList = roleMapper.selectByUserId(user.getId());
            if (roleList.size()>0){
                String code = roleList.get(0).getCode();
                if (code.equals("101")){
                    //超级管理
                }else if (code.equals("102")||code.equals("108")){
                    //领导组或普通管理员
                }else if (code.equals("103")){
                    //部门经理
                    Integer departId = departList.get(0).getId();
                    List<UserDepart> userDepartList = userDepartMapper.selectList(new QueryWrapper<UserDepart>().eq("depart_id", departId));
                    List<Integer> userDepartIdList = new ArrayList<Integer>();
                    for (int i = 0;i<userDepartList.size();i++){
                        userDepartIdList.add(userDepartList.get(i).getUserId());
                    }
                    List<User> userListBydepart = userMapper.selectBatchIds(userDepartIdList);
                    //相交
                    userList.retainAll(userListBydepart);
                }else if (code.equals("104")){

                    //项目经理
                }
            }
            for (int i = 0;i<userList.size();i++){
                int totalHour = 0;
                int monthHour = 0;
                int lastMonthHour = 0;
                int thisMonthHour = 0;
                String realName1 = userList.get(i).getRealName();
                QueryWrapper<Weekly> weeklyQueryWrapper = new QueryWrapper<Weekly>();
                weeklyQueryWrapper.eq("user_name",realName1);
                if (StringUtils.isNotBlank(startDate)){
                    Date dateStart = DateUtils.StringToDate(startDate);
                    weeklyQueryWrapper.like("start_date",dateStart);
                }
                if (StringUtils.isNotBlank(endDate)){
                    Date dateEnd = DateUtils.StringToDate(endDate);
                    weeklyQueryWrapper.like("end_date",dateEnd);
                }
                List<Weekly> weeklyList = weeklyMapper.selectList(weeklyQueryWrapper);
                List<Integer> weeklyIdList = new ArrayList<Integer>();
                for (int x = 0;x<weeklyList.size();x++){
                    Weekly weekly = weeklyList.get(x);
                    weeklyIdList.add(weekly.getId());
                    totalHour = totalHour + weekly.getLastMonthHour() + weekly.getThisMonthHour();
                }
                monthHour = weeklyService.returnMonthHour(weeklyList);
                if (weeklyIdList.size()>0){
                    List<Weekly> thisWeek = weeklyMapper.getHour(weeklyIdList, dateMonday, dateSunday);
                    for (int y = 0;y<thisWeek.size();y++){
                        lastMonthHour = lastMonthHour + thisWeek.get(y).getLastMonthHour();
                        thisMonthHour = thisMonthHour + thisWeek.get(y).getThisMonthHour();
                    }
                }
                userList.get(i).setTotalHour(totalHour);
                userList.get(i).setMonthHour(monthHour);
                userList.get(i).setLastWeekHour(lastMonthHour);
                userList.get(i).setThisWeekHour(thisMonthHour);
            }
            return new PageResult<User>(PageResultUtils.paginAtion(pageNum,pageSize,userList));
        }else {
            List<User> userList1 = new ArrayList<User>();
            List<ProUser> proUsers = proUserMapper.selectByProId(proId);
            List<Integer> userIdList = new ArrayList<Integer>();
            for (int j = 0;j<proUsers.size();j++){
                userIdList.add(proUsers.get(j).getUserId());
            }
            userList1 = userMapper.selectBatchIds(userIdList);
            for (int i = 0;i<userList1.size();i++){
                int totalHour = 0;
                int monthHour = 0;
                int lastMonthHour = 0;
                int thisMonthHour = 0;
                String realName1 = userList1.get(i).getRealName();
                QueryWrapper<Weekly> weeklyQueryWrapper = new QueryWrapper<Weekly>();
                weeklyQueryWrapper.eq("user_name",realName1);
                if (StringUtils.isNotBlank(startDate)){
                    Date dateStart = DateUtils.StringToDate(startDate);
                    weeklyQueryWrapper.ge("start_date",dateStart);
                }
                if (StringUtils.isNotBlank(endDate)){
                    Date dateEnd = DateUtils.StringToDate(endDate);
                    weeklyQueryWrapper.le("end_date",dateEnd);
                }
                List<Weekly> weeklyList = weeklyMapper.selectList(weeklyQueryWrapper);
                List<Integer> weeklyIdList = new ArrayList<Integer>();
                for (int x = 0;x<weeklyList.size();x++){
                    Weekly weekly = weeklyList.get(x);
                    weeklyIdList.add(weekly.getId());
                    totalHour = totalHour + weekly.getLastMonthHour() + weekly.getThisMonthHour();
                }
                monthHour = weeklyService.returnMonthHour(weeklyList);
                if (weeklyIdList.size()>0){
                    List<Weekly> thisWeek = weeklyMapper.getHour(weeklyIdList, dateMonday, dateSunday);
                    for (int y = 0;y<thisWeek.size();y++){
                        lastMonthHour = lastMonthHour + thisWeek.get(y).getLastMonthHour();
                        thisMonthHour = thisMonthHour + thisWeek.get(y).getThisMonthHour();
                    }
                }
                userList1.get(i).setTotalHour(totalHour);
                userList1.get(i).setMonthHour(monthHour);
                userList1.get(i).setLastWeekHour(lastMonthHour);
                userList1.get(i).setThisWeekHour(thisMonthHour);
            }
            return new PageResult<User>(PageResultUtils.paginAtion(pageNum,pageSize,userList1));
        }
    }


    private List<Integer> getUserIds(List<User> userList) {
        List<Integer> userIds = new ArrayList<>();
        for (User one : userList) {
            userIds.add(one.getId());
        }
        return userIds;
    }
}
