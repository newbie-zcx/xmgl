package com.scsoft.xmgl.info.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.scpt.exception.BusinessException;
import com.scsoft.xmgl.info.entity.Info;
import com.scsoft.xmgl.info.mapper.MaintainAndWarrantyProjectMapper;
import com.scsoft.xmgl.info.mapper.UnfinishedProjectMapper;
import com.scsoft.xmgl.info.service.IMaintainAndWarrantyProjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

@Service
public class MaintainAndWarrantyProjectServiceImpl extends ServiceImpl<MaintainAndWarrantyProjectMapper, Info> implements IMaintainAndWarrantyProjectService {
    @Resource
    private MaintainAndWarrantyProjectMapper maintainAndWarrantyProjectMapper;
    @Resource
    private UnfinishedProjectMapper unfinishedProjectMapper;

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
        wrapper.eq("pro_completion", "维护期");
        wrapper.or();
        wrapper.eq("pro_completion", "质保期");
        Page<Info> infoPage = new Page<Info>(pageNum, pageSize);
        List<Info>  infoList = maintainAndWarrantyProjectMapper.selectPage(infoPage,wrapper).getRecords();
        /**
        *质保、维修期限字段中数字转换为时间并计算到期日期
         **/
//        for (Info info:infoList){
//            char[] ZBPeriod = {'0','0','年','0','0','月','0','0','天'};
//            char[] YWPeriod = {'0','0','年','0','0','月','0','0','天'};
//
//            if(info.getProZBPeriod().length()==0){
//            }else {
//                String proZBPeriod = info.getProZBPeriod();
//                System.out.println(proZBPeriod);
//                ZBPeriod = proZBPeriod.toCharArray();
//            }
//            char day2='\0';
//            char day1='\0';
//            char month1='\0';
//            char month2='\0';
//            char year2='\0';
//            char year1='\0';
//            if(ZBPeriod.length==0){
//            }else {
//                for (int i=ZBPeriod.length-1;i>=0;i--){
//                    if (ZBPeriod[i]=='天'&&i>1){
//                        day2 = ZBPeriod[i-1];
//                        day1 = ZBPeriod[i-2];
//                    }else if (ZBPeriod[i]=='月'&&i>1){
//                        month2 = ZBPeriod[i-1];
//                        month1 = ZBPeriod[i-2];
//                    }else if (ZBPeriod[i]=='年'&&i>1){
//                        year2 = ZBPeriod[i-1];
//                        year1 = ZBPeriod[i-2];
//                    }
//                }
//                StringBuffer daybuffer = new StringBuffer();
//                if (day1=='0'){
//                    daybuffer.append(day2);
//                }else {
//                    daybuffer.append(day1);
//                    daybuffer.append(day2);
//                }
//                String day = daybuffer.toString();
//                int DAY = Integer.parseInt(day);
//
//                StringBuffer monthbuffer = new StringBuffer();
//                if (month1=='0'){
//                    monthbuffer.append(month2);
//                }else {
//                    monthbuffer.append(month1);
//                    monthbuffer.append(month2);
//                }
//                String month = monthbuffer.toString();
//                int MONTH = Integer.parseInt(month);
//
//                StringBuffer yearbuffer = new StringBuffer();
//                if (year1=='0'){
//                    yearbuffer.append(year2);
//                }else {
//                    yearbuffer.append(year1);
//                    yearbuffer.append(year2);
//                }
//                String year = yearbuffer.toString();
//                int YEAR = Integer.parseInt(year);
//
//                Calendar now = Calendar.getInstance();
//                int y = now.get(Calendar.YEAR) + YEAR;
//                int m = now.get(Calendar.MONTH) + MONTH + 1;
//                int d = now.get(Calendar.DAY_OF_MONTH) + DAY;
//                if (m>12){
//                    m=m-12;
//                    y=y+1;
//                }
//                if (y%4==0&&y%100!=0||y%400==0){
//                    if (m==2&&d>29){
//                        d=d-29;
//                        m=m+1;
//                    }
//                    if (m==1||m==3||m==5||m==7||m==8||m==10||m==12){
//                        if (d>31){
//                            d=d-31;
//                            m=m+1;
//                            if(m>12){
//                                m=m-12;
//                                y=y+1;
//                            }
//                        }
//                    }
//                    if (m==4||m==6||m==9||m==11){
//                        if (d>30){
//                            d=d-30;
//                            m=m+1;
//                        }
//                    }
//                }else {
//                    if (m==2&&d>28){
//                        d=d-28;
//                        m=m+1;
//                    }
//                    if (m==1||m==3||m==5||m==7||m==8||m==10||m==12){
//                        if (d>31){
//                            d=d-31;
//                            m=m+1;
//                            if(m>12){
//                                m=m-12;
//                                y=y+1;
//                            }
//                        }
//                    }
//                    if (m==4||m==6||m==9||m==11){
//                        if (d>30){
//                            d=d-30;
//                            m=m+1;
//                        }
//                    }
//                }
//                String ZBend = y + "-" + m + "-" + d;
//                info.setProZBEnd(ZBend);
//            }
//
//            if(info.getProYWPeriod().length()==0){
//            }else{
//                String proYWPeriod = info.getProYWPeriod();
//                System.out.println(proYWPeriod);
//                YWPeriod = proYWPeriod.toCharArray();
//            }
//            char day3='\0';
//            char day4='\0';
//            char month3='\0';
//            char month4='\0';
//            char year3='\0';
//            char year4='\0';
//            if (YWPeriod.length==0){
//            }else {
//                for (int i=YWPeriod.length-1;i>=0;i--){
//                    if (YWPeriod[i]=='天'&&i>1){
//                        day4 = YWPeriod[i-1];
//                        day3 = YWPeriod[i-2];
//                    }
//                    if (YWPeriod[i]=='月'&&i>1){
//                        month4 = YWPeriod[i-1];
//                        month3 = YWPeriod[i-2];
//                    }
//                    if (YWPeriod[i]=='年'&&i>1){
//                        year4 = YWPeriod[i-1];
//                        year3 = ZBPeriod[i-2];
//                    }
//                }
//                StringBuffer daybuffer1 = new StringBuffer();
//                if (day3=='0'){
//                    daybuffer1.append(day4);
//                }else {
//                    daybuffer1.append(day3);
//                    daybuffer1.append(day4);
//                }
//                String day5 = daybuffer1.toString();
//                int DAY1 = Integer.parseInt(day5);
//
//                StringBuffer monthbuffer1 = new StringBuffer();
//                if (month3=='0'){
//                    monthbuffer1.append(month4);
//                }else {
//                    monthbuffer1.append(month3);
//                    monthbuffer1.append(month4);
//                }
//                String month5 = monthbuffer1.toString();
//                int MONTH1 = Integer.parseInt(month5);
//
//                StringBuffer yearbuffer1 = new StringBuffer();
//                if (year3=='0'){
//                    yearbuffer1.append(year4);
//                }else {
//                    yearbuffer1.append(year3);
//                    yearbuffer1.append(year4);
//                }
//                String year5 = yearbuffer1.toString();
//                int YEAR1 = Integer.parseInt(year5);
//
//                Calendar now1 = Calendar.getInstance();
//                int y1 = now1.get(Calendar.YEAR) + YEAR1;
//                int m1 = now1.get(Calendar.MONTH) + MONTH1 + 1;
//                int d1 = now1.get(Calendar.DAY_OF_MONTH) + DAY1;
//                if (m1>12){
//                    m1=m1-12;
//                    y1=y1+1;
//                }
//                if (y1%4==0&&y1%100!=0||y1%400==0){
//                    if (m1==2&&d1>29){
//                        d1=d1-29;
//                        m1=m1+1;
//                    }
//                    if (m1==1||m1==3||m1==5||m1==7||m1==8||m1==10||m1==12){
//                        if (d1>31){
//                            d1=d1-31;
//                            m1=m1+1;
//                            if(m1>12){
//                                m1=m1-12;
//                                y1=y1+1;
//                            }
//                        }
//                    }
//                    if (m1==4||m1==6||m1==9||m1==11){
//                        if (d1>30){
//                            d1=d1-30;
//                            m1=m1+1;
//                        }
//                    }
//                }else {
//                    if (m1==2&&d1>28){
//                        d1=d1-28;
//                        m1=m1+1;
//                    }
//                    if (m1==1||m1==3||m1==5||m1==7||m1==8||m1==10||m1==12){
//                        if (d1>31){
//                            d1=d1-31;
//                            m1=m1+1;
//                            if(m1>12){
//                                m1=m1-12;
//                                y1=y1+1;
//                            }
//                        }
//                    }
//                    if (m1==4||m1==6||m1==9||m1==11){
//                        if (d1>30){
//                            d1=d1-30;
//                            m1=m1+1;
//                        }
//                    }
//                }
//                String YWend = y1 + "-" + m1 + "-" + d1;
//                info.setProYWEnd(YWend);
//            }
//        }
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
        boolean rs = maintainAndWarrantyProjectMapper.deleteById(id) > 0;
        return rs;
    }

    @Override
    public boolean update(Info info) {
        boolean rs = maintainAndWarrantyProjectMapper.updateById(info) > 0;
        return rs;
    }

    @Override
    public List<Info> listInfo() {
        return maintainAndWarrantyProjectMapper.selectList(new QueryWrapper<Info>());
    }

}

