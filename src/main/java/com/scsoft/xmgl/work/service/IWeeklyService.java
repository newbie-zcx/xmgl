package com.scsoft.xmgl.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.work.entity.ProWeekly;
import com.scsoft.xmgl.work.entity.Weekly;

import java.text.ParseException;
import java.util.List;

public interface IWeeklyService extends IService<Weekly> {
    PageResult<Weekly> list(int pageNum,int PageSize,Integer proId,String startdate,String enddate) throws ParseException;
    public int returnMonthHour(List<Weekly> weeklyList) throws ParseException;
    boolean add(Weekly weekly,int scId,int proId) throws ParseException;
    boolean update(Weekly weekly, int scId,int proId) throws ParseException;
    boolean delete(int weeklyId);
    List<Weekly> getListById(List<ProWeekly> proWeeklyList);
    boolean updateHour(Weekly weekly);
}
