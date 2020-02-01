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
         IPage iPage = detailMapper.selectPage(page,ew);
          return new PageResult<Detail>(iPage.getRecords(), iPage.getTotal());
    }
    @Override
    public PageResult<Detail> listPage(int pageNum, int pageSize, Detail detail,String type) {
        Page<Detail> page = new Page<Detail>(pageNum, pageSize);
        QueryWrapper<Detail> ew = new QueryWrapper<Detail>();
        if (type.equals("0")){
            ew.gt("create_date", DateUtil.getNowDate()+" 00:00:00");
            IPage iPage = detailMapper.selectPage(page,ew);
            return new PageResult<Detail>(iPage.getRecords(), iPage.getTotal());
        }else{
            ew.select("user_name","user_depart");
            IPage iPage = detailMapper.todayNosb(page,ew);
            return new PageResult<Detail>(iPage.getRecords(), iPage.getTotal());
        }

    }



    @Override
    public List<Detail> selectList(Detail detail) {
        QueryWrapper<Detail> ew = new QueryWrapper<Detail>();
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
