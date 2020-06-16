package com.scsoft.xmgl.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.xmgl.system.entity.Role;
import com.scsoft.xmgl.system.entity.RoleAuthority;
import com.scsoft.xmgl.system.mapper.RoleAuthorityMapper;
import com.scsoft.xmgl.system.mapper.RoleMapper;
import com.scsoft.xmgl.system.service.IRoleService;
import com.scsoft.scpt.exception.ParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 角色 服务实现类
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleAuthorityMapper roleAuthorityMapper;

    @Override
    public List<Role> getByUserId(int userId) {
        return roleMapper.selectByUserId(userId);
    }

    @Override
    public List<Role> list(boolean showDelete) {
        QueryWrapper<Role> wrapper = new QueryWrapper<Role>();
        if (!showDelete) {
            wrapper.eq("is_del", 0);
        }
        return roleMapper.selectList(wrapper.orderByAsc("create_date"));
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateState(int roleId, int isDelete) {
        if (isDelete != 0 && isDelete != 1) {
            throw new ParameterException("isDelete值需要在[0,1]中");
        }
        Role role = new Role();
        role.setId(roleId);
        role.setIsDel(isDelete);
        boolean rs = roleMapper.updateById(role) > 0;
        if (rs) {
            //删除角色的权限
            roleAuthorityMapper.delete(new QueryWrapper<RoleAuthority>().eq("role_id", roleId));
        }
        return rs;
    }

    @Override
    public Role getById(int roleId) {
        return roleMapper.selectById(roleId);
    }


}
