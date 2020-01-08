package com.scsoft.wlyz.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.wlyz.common.shiro.EndecryptUtil;
import com.scsoft.wlyz.system.entity.Role;
import com.scsoft.wlyz.system.entity.User;
import com.scsoft.wlyz.system.entity.UserDepart;
import com.scsoft.wlyz.system.entity.UserRole;
import com.scsoft.wlyz.system.mapper.UserDepartMapper;
import com.scsoft.wlyz.system.mapper.UserMapper;
import com.scsoft.wlyz.system.mapper.UserRoleMapper;
import com.scsoft.wlyz.system.service.IUserService;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.scpt.exception.BusinessException;
import com.scsoft.scpt.exception.ParameterException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    @Override
    public List<Map<String, Object>> selectUsers(String name, String beginTime, String endTime, int deptid) {

        return null;
    }


    @Override
    public User getByUsername(String username) {
        return userMapper.getByUsername(username);
    }
    @Override
    public User getByname(String username) {
        QueryWrapper<User> qw=new QueryWrapper<User>();
        qw.in("user_name",username);
        List<User> users=userMapper.selectList(qw);
        if (null!=users&&users.size()>0){
            return userMapper.selectList(qw).get(0);
        }else {
            return null;
        }

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

    /**
     * 添加用户角色 sssscc
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

    private List<Integer> getUserIds(List<User> userList) {
        List<Integer> userIds = new ArrayList<>();
        for (User one : userList) {
            userIds.add(one.getId());
        }
        return userIds;
    }
}
