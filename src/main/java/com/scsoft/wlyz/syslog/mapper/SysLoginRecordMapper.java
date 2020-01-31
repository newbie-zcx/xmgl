package com.scsoft.xgsb.syslog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scsoft.xgsb.syslog.entity.SysLoginRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 登陆日志记录表 Mapper 接口
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
public interface SysLoginRecordMapper extends BaseMapper<SysLoginRecord> {
    List<SysLoginRecord> listFull(Page<SysLoginRecord> page, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("account") String account);

}
