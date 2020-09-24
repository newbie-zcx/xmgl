package com.scsoft.xmgl.work.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.common.exception.BusinessException;
import com.scsoft.xmgl.common.utils.DateUtils;
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


    @Override
    public PageResult<Acount> list(int pageNum, int pageSize,String proId,String startDate,String endDate) throws ParseException {
        QueryWrapper<Acount> wrapper = new QueryWrapper<Acount>();
        if (StringUtils.isNotBlank(proId)){
            wrapper.eq("pro_id",proId);
        }
        if (StringUtils.isNotBlank(startDate)){
            wrapper.eq("start_date",DateUtils.StringToDate(startDate));
        }
        if (StringUtils.isNotBlank(endDate)){
            wrapper.eq("end_date",DateUtils.StringToDate(endDate));
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
                AcountContent acountContent = acountContentList.get(j);
                AC ac = acMapper.selectById(acountContent.getContentId());
                acList.add(ac);
            }
            acount.setAcList(acList);
        }
        return new PageResult<Acount>(acountList,acountPage.getTotal());
    }

    @Override
    public boolean add(Acount acount, List<Integer> acThisIdList, List<Integer> acNextIdList, int proId) throws ParseException {

        Date startDate = DateUtils.StringToDate(acount.getStartDate());
        Date endDate = DateUtils.StringToDate((acount.getEndDate()));
        QueryWrapper<Acount> wrapper = new QueryWrapper<Acount>();
        acount.setProId(proId);
        wrapper.eq("start_date",startDate);
        wrapper.eq("end_date",endDate);
        wrapper.eq("pro_id",acount.getProId());
        if (acountMapper.selectCount(wrapper) > 0){
            for (int i= 0;i<acThisIdList.size();i++){
                acMapper.deleteById(acThisIdList.get(i));
            }
            for (int j= 0;j<acNextIdList.size();j++){
                acMapper.deleteById(acNextIdList.get(j));
            }
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
            if (acountContentMapper.addContent(acountId,acThisIdList)>0&&acountContentMapper.addContent(acountId,acNextIdList)>0){
            }else {
                throw  new BusinessException("添加失败，请重试");
            }

        }
        return result;
    }
}
