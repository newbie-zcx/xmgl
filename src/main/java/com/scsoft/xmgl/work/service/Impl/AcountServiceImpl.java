package com.scsoft.xmgl.work.service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Equivalence;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.common.exception.BusinessException;
import com.scsoft.xmgl.common.handler.SystemCommonHandler;
import com.scsoft.xmgl.common.utils.DateUtils;
import com.scsoft.xmgl.system.entity.Depart;
import com.scsoft.xmgl.system.entity.Role;
import com.scsoft.xmgl.system.entity.User;
import com.scsoft.xmgl.system.mapper.DepartMapper;
import com.scsoft.xmgl.system.mapper.RoleMapper;
import com.scsoft.xmgl.work.entity.*;
import com.scsoft.xmgl.work.mapper.*;
import com.scsoft.xmgl.work.service.IAcountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class AcountServiceImpl extends ServiceImpl<AcountMapper, Acount> implements IAcountService {

    @Resource
    private AcountMapper acountMapper;
    @Resource
    private ProAcountMapper proAcountMapper;
    @Resource
    private AcountContentMapper acountContentMapper;
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private ACMapper acMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private DepartMapper departMapper;

    @Override
    public PageResult<Acount> list(int pageNum, int pageSize,String proId,String startDate,String endDate) throws ParseException {
        QueryWrapper<Acount> wrapper = new QueryWrapper<Acount>();
        User loginUser = SystemCommonHandler.getLoginUser();
        Integer userId = loginUser.getId();
        List<Role> roles = roleMapper.selectByUserId(userId);
        String code = roles.get(0).getCode();
        if (StringUtils.isNotBlank(proId)){
            int Id = Integer.parseInt(proId);
            wrapper.eq("pro_id",Id);
        }
        if (StringUtils.isNotBlank(startDate)){
            wrapper.le("start_date",DateUtils.StringToDate(startDate));
        }else{
            if (code.equals("102")){
                wrapper.eq("start_date",DateUtils.getWeekDate().get("lastMondayDate"));
                wrapper.eq("end_date",DateUtils.getWeekDate().get("lastSundayDate"));
            }else if (code.equals("103")){
                wrapper.eq("start_date",DateUtils.getWeekDate().get("lastMondayDate"));
                wrapper.eq("end_date",DateUtils.getWeekDate().get("lastSundayDate"));
                wrapper.eq("dept",departMapper.selectByUserId(userId).get(0).getAddress());
            }else if (code.equals("104")){
                wrapper.eq("create_name",loginUser.getRealName());
            }
        }
        if (StringUtils.isNotBlank(endDate)){
            wrapper.ge("end_date",DateUtils.StringToDate(endDate));
        }else{
            if (code.equals("102")){
                wrapper.eq("start_date",DateUtils.getWeekDate().get("lastMondayDate"));
                wrapper.eq("end_date",DateUtils.getWeekDate().get("lastSundayDate"));
            }else if (code.equals("103")){
                wrapper.eq("start_date",DateUtils.getWeekDate().get("lastMondayDate"));
                wrapper.eq("end_date",DateUtils.getWeekDate().get("lastSundayDate"));
                wrapper.eq("dept",departMapper.selectByUserId(userId).get(0).getAddress());
            }else if (code.equals("104")){
                wrapper.eq("create_name",loginUser.getRealName());
            }
        }
        Page<Acount> acountPage = new Page<Acount>(pageNum,pageSize);
        List<Acount> acountList = acountMapper.selectPage(acountPage, wrapper.orderByDesc("create_date")).getRecords();
        for (int i = 0;i<acountList.size();i++){
            Acount acount = acountList.get(i);
            //绑定项目
            ProAcount proAcount = proAcountMapper.getByAcountId(acount.getId());
            Project project = projectMapper.selectById(proAcount.getProId());
            acount.setProject(project);
            //绑定台账内容
            List<AcountContent> acountContentList = acountContentMapper.getByAcountId(acount.getId());
            List<AC> acList = new ArrayList<AC>();
            for (int j = 0;j<acountContentList.size();j++){
                AC ac = acMapper.selectById(acountContentList.get(j).getContentId());
                acList.add(ac);
            }
            acount.setAcList(acList);
        }
        return new PageResult<Acount>(acountList,acountPage.getTotal());
    }

    @Override
    public boolean add(Acount acount,Integer thisId,Integer nextId, int proId) throws ParseException {
        Date startDate = DateUtils.StringToDate(acount.getStartDate());
        Date endDate = DateUtils.StringToDate((acount.getEndDate()));
        QueryWrapper<Acount> wrapper = new QueryWrapper<Acount>();
        acount.setProId(proId);
        List<Depart> departs = departMapper.selectByUserId(SystemCommonHandler.getLoginUser().getId());
        acount.setDept(departs.get(0).getAddress());
        wrapper.eq("start_date",startDate);
        wrapper.eq("end_date",endDate);
        wrapper.eq("pro_id",acount.getProId());
        if (acountMapper.selectCount(wrapper) > 0){
            acMapper.deleteById(thisId);
            acMapper.deleteById(nextId);
            throw new BusinessException("台账已经存在！");
        }
        boolean result = acountMapper.insert(acount)>0;
        if (result){
            Integer acountId = acount.getId();
            //插入数据至项目-台账表
            if (proAcountMapper.add(proId, acountId)<1){
                throw new BusinessException("添加失败，请重试");
            }
            //插入数据至台账-台账内容表中
            if (acountContentMapper.addContent(acountId,thisId)>0&&acountContentMapper.addContent(acountId,nextId)>0){
            }else {
                throw  new BusinessException("添加失败，请重试");
            }
        }
        return result;
    }

    @Override
    public boolean update(Acount acount) {
        boolean result = false;
        if (acount!=null){
            result = acountMapper.update(acount,new UpdateWrapper<Acount>())>0;
        }
        return result;
    }

    @Override
    public boolean delete(int acountId) {
        boolean result = false;
        result = acountMapper.deleteById(acountId) > 0;
        ProAcount proAcount = proAcountMapper.selectOne(new QueryWrapper<ProAcount>().eq("acount_id", acountId));
        proAcountMapper.deleteById(proAcount.getId());
        List<AcountContent> acountContents = acountContentMapper.getByAcountId(acountId);
        for (int i = 0;i<acountContents.size();i++){
            acountContentMapper.deleteById(acountContents.get(i).getId());
            acMapper.deleteById(acountContents.get(i).getContentId());
        }
        return result;
    }
}
