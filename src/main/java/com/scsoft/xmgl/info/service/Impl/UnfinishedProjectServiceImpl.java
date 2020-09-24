package com.scsoft.xmgl.info.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.scpt.exception.BusinessException;
import com.scsoft.xmgl.info.entity.Info;
import com.scsoft.xmgl.info.mapper.UnfinishedProjectMapper;
import com.scsoft.xmgl.info.service.IUnfinishedProjectService;
import com.scsoft.xmgl.system.entity.User;
import com.scsoft.xmgl.system.mapper.UserMapper;
import com.scsoft.xmgl.work.entity.ProUser;
import com.scsoft.xmgl.work.mapper.ProUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UnfinishedProjectServiceImpl extends ServiceImpl<UnfinishedProjectMapper, Info> implements IUnfinishedProjectService {
    @Resource
    private UnfinishedProjectMapper unfinishedProjectMapper;
    @Resource
    private ProUserMapper proUserMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public PageResult<Info>  list(int pageNum, int pageSize,String proName,String proManager,String proDept) {
        QueryWrapper<Info> wrapper = new QueryWrapper<Info>();
        if (StringUtils.isNotBlank(proName)) {
            wrapper.like("pro_name", proName);
        }
        if (StringUtils.isNotBlank(proManager)) {
            wrapper.like("pro_manager", proManager);
        }
        if (StringUtils.isNotBlank(proDept)) {
            wrapper.like("pro_dept", proDept);
        }
        wrapper.eq("pro_completion", "未完成");
        wrapper.ne("pro_type", "内部研发");
        Page<Info> infoPage = new Page<Info>(pageNum, pageSize);
        List<Info>  infoList = unfinishedProjectMapper.selectPage(infoPage,wrapper).getRecords();
        return new PageResult<>(infoList, infoPage.getTotal());
    }

    @Override
    public boolean add(Info info) {
        if (unfinishedProjectMapper.selectByProName(info.getProName())==null){
            return unfinishedProjectMapper.insert(info) > 0;
        }else {
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        boolean rs = unfinishedProjectMapper.deleteById(id) > 0;
        return rs;
    }

    @Override
    public boolean update(Info info) {
        boolean rs = unfinishedProjectMapper.updateById(info) > 0;
        return rs;
    }

    @Override
    public List<Info> listInfo() {
        return unfinishedProjectMapper.selectList(new QueryWrapper<Info>());
    }

    @Override
    public PageResult<Info> listInfoByManager(int pageNum, int pageSize,String realName) {
        Page<Info> infoPage = new Page<Info>(pageNum,pageSize);
        QueryWrapper<Info> wrapper = new QueryWrapper<Info>();
        if (StringUtils.isNotBlank(realName)){
            wrapper.eq("pro_manager", realName);
        }
        List<Info> infoList = unfinishedProjectMapper.selectPage(infoPage, wrapper.orderByDesc("create_date")).getRecords();
        if (infoList!=null&&infoList.size()>0){
            //获取项目成员
            List<Integer> proIds = getProIds(infoList);
            List<ProUser> proUsers = proUserMapper.selectByProIds(proIds);
            for (Info info:infoList){
                List<User> tempPUs = new ArrayList<>();
                for (ProUser proUser:proUsers){
                    if (info.getId().equals(proUser.getProId())){
                        tempPUs.add(new User(proUser.getUserId(),proUser.getRealName()));
                    }
                }
                info.setUserList(tempPUs);
            }
        }

        return new PageResult<Info>(infoList,infoPage.getTotal());
    }

    @Override
    public PageResult<Info> infoList(int pageNum, int pageSize,String realName) {
        QueryWrapper<Info> wrapper = new QueryWrapper<Info>();
        if(StringUtils.isNotBlank(realName)){
            wrapper.eq("pro_manager",realName);
        }
        Page<Info> infoPage = new Page<Info>(pageNum,pageSize);
        List<Info> infoList = unfinishedProjectMapper.selectPage(infoPage, wrapper).getRecords();
        return new PageResult<Info>(infoList,infoPage.getTotal());
    }

    @Override
    public PageResult infoList1(int pageNum, int pageSize, String section) {
        QueryWrapper<Info> wrapper = new QueryWrapper<Info>();
        if(StringUtils.isNotBlank(section)){
            wrapper.eq("pro_dept",section);
        }
        Page<Info> infoPage = new Page<Info>(pageNum,pageSize);
        List<Info> infoList = unfinishedProjectMapper.selectPage(infoPage, wrapper).getRecords();
        return new PageResult<Info>(infoList,infoPage.getTotal());
    }

    private List<Integer> getProIds(List<Info> infoList) {
        List<Integer> proIds = new ArrayList<>();
        for (Info info: infoList) {
            proIds.add(info.getId());
        }
        return proIds;
    }


}

