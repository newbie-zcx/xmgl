package com.scsoft.xgsb.syslog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.xgsb.syslog.entity.SysLoginRecord;
import com.scsoft.xgsb.syslog.mapper.SysLoginRecordMapper;
import com.scsoft.xgsb.syslog.service.ISysLoginRecordService;
import com.scsoft.scpt.common.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 登陆日志记录表 服务实现类
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@Service
public class SysLoginRecordServiceImpl extends ServiceImpl<SysLoginRecordMapper, SysLoginRecord> implements ISysLoginRecordService {
    @Resource
    private SysLoginRecordMapper loginRecordMapper;

    @Override
    public PageResult<SysLoginRecord> list(Integer pageNum, Integer pageSize, String startDate, String endDate, String account) {
        QueryWrapper<SysLoginRecord> wrapper = new QueryWrapper<SysLoginRecord>();
        if (StringUtils.isNotBlank(account)) {
            wrapper.eq("user_name", account);
        }
        if (StringUtils.isNotBlank(startDate)) {
            wrapper.ge("create_time", startDate);
        }
        if (StringUtils.isNotBlank(endDate)) {
            wrapper.le("create_time", endDate);
        }
        Page<SysLoginRecord> loginRecordPage = new Page<SysLoginRecord>(pageNum, pageSize);
        List<SysLoginRecord> loginRecordList  = loginRecordMapper.selectPage(loginRecordPage, wrapper.orderByDesc("create_time")).getRecords();
        return new PageResult<SysLoginRecord>(loginRecordList,loginRecordPage.getTotal() );
    }
}
