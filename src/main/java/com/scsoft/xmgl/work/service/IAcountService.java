package com.scsoft.xmgl.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.work.entity.Acount;

import java.text.ParseException;
import java.util.List;

public interface IAcountService extends IService<Acount> {
    PageResult<Acount> list(int pageNum,int pageSize,String proId,String startDate,String endDate) throws ParseException;
    boolean add(Acount acount,Integer thisId,Integer nextId,int proId) throws ParseException;
    boolean update(Acount acount);
    boolean delete(int acountId);
}
