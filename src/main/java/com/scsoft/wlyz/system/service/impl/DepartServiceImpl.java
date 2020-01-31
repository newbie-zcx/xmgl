package com.scsoft.xgsb.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.xgsb.system.entity.Depart;
import com.scsoft.xgsb.system.mapper.DepartMapper;
import com.scsoft.xgsb.system.service.IDepartService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 部门 服务类
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@Service
public class DepartServiceImpl extends ServiceImpl<DepartMapper, Depart> implements IDepartService {
    @Resource
    private DepartMapper departMapper;
    @Override
    public List<Depart> getByUserId(int userId) {
        return departMapper.selectByUserId(userId);
    }

    @Override
    public List<Depart> findDepartByName(String name) {
        return departMapper.selectList(new LambdaQueryWrapper<Depart>().eq(Depart::getDepartName,name));
    }
    @Override
    public List<Depart> getByPId(int pid) {
        return departMapper.selectList(new QueryWrapper<Depart>().eq("parent_id",pid));
    }

    @Override
    public List<Depart> getByPId(List<Depart> pids) {
        List<Integer> depIds = new ArrayList<Integer>();
        for (Depart one : pids) {
            depIds.add(one.getId());
        }
        QueryWrapper<Depart> query = new QueryWrapper<Depart>().in("parent_id", depIds).or().in("id",depIds);
        return departMapper.selectList(query);
    }

    @Override
    public List<Depart> selectNotInId(int departId) {
        return departMapper.selectNotInId(departId);
    }
}
