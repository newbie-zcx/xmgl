package com.scsoft.xgsb.yqsb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.scpt.utils.DateUtil;
import com.scsoft.xgsb.yqsb.entity.Detail;
import com.scsoft.xgsb.yqsb.mapper.DetailMapper;
import com.scsoft.xgsb.yqsb.service.IDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 疫情上报详情 服务实现类
 * </p>
 *
 * @author 赵鹏飞
 * @since 2020-01-31
 */
@Service
public class DetailServiceImpl extends ServiceImpl<DetailMapper, Detail> implements IDetailService {
     @Resource
    private DetailMapper detailMapper;
    /**
     * 通过分页查询
     * @param pageNum
     * @return PageResult
     */
    @Override
    public PageResult<Detail> listPage(int pageNum, int pageSize, Detail detail) {
       Page<Detail> page = new Page<Detail>(pageNum, pageSize);
         QueryWrapper<Detail> ew = new QueryWrapper<Detail>();
        if (StringUtils.isNotBlank(detail.getUserName())) {//获取姓名
            ew.like("user_name", detail.getUserName());
        }
        if (!StringUtils.isBlank(detail.getUserDepart())) {//获取部门
            ew.like("user_depart", detail.getUserDepart());
        }
        if (StringUtils.isNotBlank(detail.getOutState())) {//获取外出状态
            ew.eq("out_state", detail.getOutState());
        }
        if (!StringUtils.isBlank(detail.getHdXznsz())) {//获取重点活动区域
            ew.eq("hd_xznsz", detail.getHdXznsz());
        }
        if (StringUtils.isNotBlank(detail.getFsgm())) {//是否感冒发烧
            ew.eq("fsgm", detail.getFsgm());
        }
        if (!StringUtils.isBlank(detail.getJtyx())) {//获取交通影响
            ew.eq("jtyx", detail.getJtyx());
        }
        if (null!=detail.getCreateDate()) {//时间排序
            ew.gt("create_date", DateUtil.getDay(detail.getCreateDate())+" 00:00:00");
            ew.lt("create_date", DateUtil.getDay(detail.getCreateDate())+" 23:59:00");
        }
        ew.orderByDesc("create_Date");
         IPage iPage = detailMapper.selectPage(page,ew);
          return new PageResult<Detail>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public PageResult<Detail> oneselfListPage(int pageNum, int pageSize, Detail detail) {
        Page<Detail> page = new Page<Detail>(pageNum, pageSize);
        QueryWrapper<Detail> ew = new QueryWrapper<Detail>();
       //获取姓名ID
        ew.eq("create_id", detail.getCreateId());
        ew.orderByDesc("create_Date");
        IPage iPage = detailMapper.selectPage(page,ew);
        return new PageResult<Detail>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public PageResult<Detail> departmentListPage(int pageNum, int pageSize, Detail detail,List<String> list) {
        Page<Detail> page = new Page<Detail>(pageNum, pageSize);
        QueryWrapper<Detail> ew = new QueryWrapper<Detail>();
        //获取部门名称
        ew.in("user_depart", list);
        if (StringUtils.isNotBlank(detail.getUserName())) {//获取姓名
            ew.like("user_name", detail.getUserName());
        }
        if (StringUtils.isNotBlank(detail.getOutState())) {//获取外出状态
            ew.eq("out_state", detail.getOutState());
        }
        if (!StringUtils.isBlank(detail.getHdXznsz())) {//获取重点活动区域
            ew.eq("hd_xznsz", detail.getHdXznsz());
        }
        if (StringUtils.isNotBlank(detail.getFsgm())) {//是否感冒发烧
            ew.eq("fsgm", detail.getFsgm());
        }
        if (!StringUtils.isBlank(detail.getJtyx())) {//获取交通影响
            ew.eq("jtyx", detail.getJtyx());
        }
        if (null!=detail.getCreateDate()) {//时间排序
            ew.gt("create_date", DateUtil.getDay(detail.getCreateDate())+" 00:00:00");
            ew.lt("create_date", DateUtil.getDay(detail.getCreateDate())+" 23:59:00");
        }
        ew.orderByDesc("create_Date");
        IPage iPage = detailMapper.selectPage(page,ew);
        return new PageResult<Detail>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public PageResult<Detail> listPage(int pageNum, int pageSize, Detail detail,String type) {
        Page<Detail> page = new Page<Detail>(pageNum, pageSize);
        QueryWrapper<Detail> ew = new QueryWrapper<Detail>();
        if (type.equals("0")){
            if (StringUtils.isNotBlank(detail.getUserName())) {//获取姓名
                ew.like("user_name", detail.getUserName());
            }
            if (!StringUtils.isBlank(detail.getUserDepart())) {//获取部门
                ew.like("user_depart", detail.getUserDepart());
            }
            if (StringUtils.isNotBlank(detail.getOutState())) {//获取外出状态
                ew.eq("out_state", detail.getOutState());
            }
            if (!StringUtils.isBlank(detail.getHdXznsz())) {//获取重点活动区域
                ew.eq("hd_xznsz", detail.getHdXznsz());
            }
            if (StringUtils.isNotBlank(detail.getFsgm())) {//是否感冒发烧
                ew.eq("fsgm", detail.getFsgm());
            }
            if (!StringUtils.isBlank(detail.getJtyx())) {//获取交通影响
                ew.eq("jtyx", detail.getJtyx());
            }
            if (null!=detail.getCreateDate()) {//时间排序
                ew.gt("create_date", DateUtil.getDay(detail.getCreateDate())+" 00:00:00");
                ew.lt("create_date", DateUtil.getDay(detail.getCreateDate())+" 23:59:00");
            }
            ew.gt("create_date", DateUtil.getNowDate()+" 00:00:00");
            ew.orderByDesc("create_Date");
            IPage iPage = detailMapper.selectPage(page,ew);
            return new PageResult<Detail>(iPage.getRecords(), iPage.getTotal());
        }else{
            if (StringUtils.isNotBlank(detail.getUserName())) {//获取姓名
                ew.like("a.user_name", detail.getUserName());
            }
            if (!StringUtils.isBlank(detail.getUserDepart())) {//获取部门
                ew.like("a.user_depart", detail.getUserDepart());
            }
            IPage iPage = detailMapper.todayNosb(page,ew);
            return new PageResult<Detail>(iPage.getRecords(), iPage.getTotal());
        }

    }



    @Override
    public List<Detail> selectList(Detail detail) {
        QueryWrapper<Detail> ew = new QueryWrapper<Detail>();
        ew.orderByDesc("create_Date");
        return detailMapper.selectList(ew);

     }
    @Override
    public Detail tijiao(Detail detail){
        QueryWrapper<Detail> ew = new QueryWrapper<Detail>();
        ew.eq("user_name",detail.getUserName());
        ew.gt("create_date", DateUtil.getNowDate()+" 00:00:00");
        List<Detail> list=detailMapper.selectList(ew);
        if (list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }


    /**
         *  逻辑删除
         */
        @Override
        public Boolean logicDelete(Detail detail){
            detail.setIsDel(1);
            return this.updateById(detail);
        }
}
