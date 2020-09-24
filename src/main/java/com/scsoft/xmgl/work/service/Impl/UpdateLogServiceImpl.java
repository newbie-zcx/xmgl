package com.scsoft.xmgl.work.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.scpt.utils.WebUtil;
import com.scsoft.xmgl.common.handler.SystemCommonHandler;
import com.scsoft.xmgl.work.entity.UpdateLog;
import com.scsoft.xmgl.work.mapper.UpdateLogMapper;
import com.scsoft.xmgl.work.service.IUpdateLogService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Service
public class UpdateLogServiceImpl extends ServiceImpl<UpdateLogMapper, UpdateLog> implements IUpdateLogService {

    @Resource
    private UpdateLogMapper updateLogMapper;

    @Override
    public void add(int entityId,String beforeParams,String afterParams,String updateTable,String updateRow,String updateReason) throws ClassNotFoundException {
        UpdateLog updateLog = new UpdateLog();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //获取IP
        String remoteAddr = WebUtil.getRemoteAddr(request);
        updateLog.setRemoteAddr(remoteAddr);
        updateLog.setUserName(SystemCommonHandler.getLoginUser().getRealName());
        updateLog.setUpdateReason(updateReason);
        updateLog.setUpdateTable(updateTable);//通过updateTable获得修改的是哪个表的数据
        updateLog.setEntityId(entityId);//通过entityId获取修改的是这张表哪条数据
        updateLog.setUpdateRow(updateRow);//通过updateRow获取修改的是这条数据的哪一字段
        updateLog.setBeforeParams(beforeParams);
        updateLog.setAfterParams(afterParams);
        updateLogMapper.insert(updateLog);
    }

    @Override
    public List<UpdateLog> isUpdate(int entityId, String updateTable, String updateRow) {
        QueryWrapper<UpdateLog> wrapper = new QueryWrapper<UpdateLog>();
        wrapper.eq("update_table",updateTable);
        wrapper.eq("entity_id",entityId);
        wrapper.eq("update_row",updateRow);
        return updateLogMapper.selectList(wrapper.orderByDesc("create_date"));
    }
}
