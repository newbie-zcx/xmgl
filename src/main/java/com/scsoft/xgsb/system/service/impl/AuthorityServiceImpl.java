package com.scsoft.xgsb.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.xgsb.system.entity.Authority;
import com.scsoft.xgsb.system.entity.RoleAuthority;
import com.scsoft.xgsb.system.mapper.AuthorityMapper;
import com.scsoft.xgsb.system.mapper.RoleAuthorityMapper;
import com.scsoft.xgsb.system.service.IAuthorityService;
import com.scsoft.scpt.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 数据菜单权限表 服务实现类
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@Service
public class AuthorityServiceImpl extends ServiceImpl<AuthorityMapper, Authority> implements IAuthorityService {
    @Resource
    private AuthorityMapper authMapper;

    @Resource
    private RoleAuthorityMapper roleAuthMapper;

    @Override
    public List<Authority> listByUserId(int userId) {
        return authMapper.listByUserId(userId);
    }

    @Override
    public List<Authority> list() {
        return authMapper.selectList(new QueryWrapper<Authority>().orderByAsc("sort"));
    }

    @Override
    public List<Authority> listMenu() {
        return authMapper.selectList(new QueryWrapper<Authority>().eq("type", 0).orderByAsc("sort"));
    }

    @Override
    public List<Authority> listByRoleIds(List<String> roleIds) {
        if (roleIds == null || roleIds.size() == 0) {
            return new ArrayList<>();
        }
        return authMapper.listByRoleIds(roleIds);
    }

    @Override
    public List<Authority> listByRoleId(int roleId) {
        return authMapper.listByRoleId(roleId);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(int authorityId) {
        List<Authority> childs = authMapper.selectList(new QueryWrapper<Authority>().eq("parent_id", authorityId));
        if (childs != null && childs.size() > 0) {
            throw new BusinessException("请先删除子节点");
        }
        roleAuthMapper.delete(new QueryWrapper<RoleAuthority>().eq("authority_id", authorityId));
        if (authMapper.deleteById(authorityId) <= 0) {
            throw new BusinessException("删除失败，请重试");
        }
        return true;
    }


    @Override
    public boolean deleteRoleAuth(int roleId, int authId) {
        return roleAuthMapper.delete(new QueryWrapper<RoleAuthority>().eq("role_id", roleId).eq("authority_id", authId)) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRoleAuth(int roleId, List<Integer> authIds) {
        roleAuthMapper.delete(new QueryWrapper<RoleAuthority>().eq("role_id", roleId));
        if (authIds != null && authIds.size() > 0) {
            if (roleAuthMapper.insertRoleAuths(roleId, authIds) < authIds.size()) {
                throw new BusinessException("操作失败");
            }
        }
        return true;
    }
    @Override
    public boolean update(Authority Authority) {
        return authMapper.updateById(Authority) > 0;
    }
    @Override
    public boolean addRoleAuth(int roleId, int authId) {
        RoleAuthority roleAuthority = new RoleAuthority();
        roleAuthority.setRoleId(roleId);
        roleAuthority.setAuthorityId(authId);
        return roleAuthMapper.insert(roleAuthority) > 0;
    }
}
