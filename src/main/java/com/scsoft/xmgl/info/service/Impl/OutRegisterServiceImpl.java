package com.scsoft.xmgl.info.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.info.entity.Info;
import com.scsoft.xmgl.info.entity.OutRegister;
import com.scsoft.xmgl.info.mapper.OutRegisterMapper;
import com.scsoft.xmgl.info.service.IOutRegisterService;
import com.scsoft.xmgl.system.entity.User;
import org.apache.velocity.runtime.directive.contrib.For;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OutRegisterServiceImpl extends ServiceImpl<OutRegisterMapper,OutRegister> implements IOutRegisterService {

    @Resource
    private OutRegisterMapper outRegisterMapper;

    @Override
    public PageResult<OutRegister> list(int pageNum, int pageSize,String userName) {
        Page<OutRegister> outRegisterPage = new Page<OutRegister>(pageNum,pageSize);
        List<OutRegister> outRegisterList = outRegisterMapper.selectPage(outRegisterPage, new QueryWrapper<OutRegister>().eq("user_name",userName)).getRecords();
        return new PageResult<>(outRegisterList,outRegisterPage.getTotal());
    }

    @Override
    public PageResult<OutRegister> deptList(int pageNum, int pageSize, List<User> userList) {
        Page<OutRegister> outRegisterPage = new Page<OutRegister>(pageNum,pageSize);
        QueryWrapper<OutRegister> wrapper =  new QueryWrapper<OutRegister>();
        List realNameList = new ArrayList();
        for (User user:userList){
            String realName = user.getRealName();
            realNameList.add(realName);
        }
        wrapper.in("user_name",realNameList);
        List<OutRegister> outRegisterList = outRegisterMapper.selectPage(outRegisterPage, wrapper).getRecords();
        return new PageResult<>(outRegisterList,outRegisterPage.getTotal());
    }

    @Override
    public boolean add(OutRegister outRegister) {
        return outRegisterMapper.insert(outRegister)>0;
    }

    @Override
    public boolean update(OutRegister outRegister) {
        return outRegisterMapper.updateById(outRegister)>0;
    }

    @Override
    public boolean delete(int id) {
        return outRegisterMapper.deleteById(id)>0;
    }

    @Override
    public boolean updateState(int id, Integer approval) {
        Map<String,Object> map = new HashMap();
        map.put("id",id);
        map.put("approval",approval);
        return outRegisterMapper.updateState(map);
    }
}
