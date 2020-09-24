package com.scsoft.xmgl.info.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.common.exception.BusinessException;
import com.scsoft.xmgl.info.entity.Milepost;
import com.scsoft.xmgl.info.mapper.MilepostInfoMapper;
import com.scsoft.xmgl.info.service.IMilepostInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class MilepostInfoServiceImpl extends ServiceImpl<MilepostInfoMapper,Milepost> implements IMilepostInfoService {

    @Resource
    private MilepostInfoMapper milepostInfoMapper;

    @Override
    public Milepost queryById(int id) {
        return milepostInfoMapper.selectById(id);
    }

    @Override
    public PageResult list(int pageNum, int pageSize,String proId,String proName,String milepostStartDate,String milepostEndDate,String milepostStatus) {
        QueryWrapper<Milepost> wrapper = new QueryWrapper<Milepost>();
        if (StringUtils.isNotBlank(proId)) {
            wrapper.like("pro_id", proId);
        }
        if (StringUtils.isNotBlank(proName)) {
            wrapper.like("pro_name", proName);
        }
        if (StringUtils.isNotBlank(milepostStartDate)) {
            wrapper.like("milepost_start_date", milepostStartDate);
        }
        if (StringUtils.isNotBlank(milepostEndDate)) {
            wrapper.like("milepost_end_date", milepostEndDate);
        }
        if (StringUtils.isNotBlank(milepostStatus)) {
            wrapper.like("milepost_status", milepostStatus);
        }
        Page<Milepost> milepostPage = new Page<Milepost>(pageNum, pageSize);
        List<Milepost> milepostList = milepostInfoMapper.selectPage(milepostPage,wrapper).getRecords();
        return new PageResult<>(milepostList, milepostPage.getTotal());
    }

    @Override
    public boolean add(Milepost milepost) {
        if (milepostInfoMapper.milepostIsExit(milepost.getProName(),milepost.getMilepostName())==null){
            return milepostInfoMapper.addMilepost(milepost);
        }
        return false;
    }

    @Override
    public boolean update(Milepost milepost) {
        return milepostInfoMapper.updateById(milepost) > 0;
    }

    @Override
    public boolean delete(int id) {
        return milepostInfoMapper.deleteById(id) > 0;
    }

    @Override
    public boolean addMilepost(Milepost milepost) {
        return milepostInfoMapper.insert(milepost) > 0;
    }

    @Override
    public void queryall() {
        QueryWrapper<Milepost> wrapper = new QueryWrapper<Milepost>();
        List<Milepost> milepostList = milepostInfoMapper.selectList(wrapper);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (milepostList == null) {
        } else {
            for (Milepost milepost : milepostList) {
                if (milepost.getMilepostEndDate() == null) {
                } else {
                    try {
                        Date plan = sdf.parse(milepost.getMilepostEndDate());
                        Date now = new Date(System.currentTimeMillis());
                        long day = (plan.getTime() - now.getTime()) / (24 * 60 * 60 * 1000);
                        milepost.setMilepostRemainder((int)day);
                        milepostInfoMapper.updateById(milepost);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
