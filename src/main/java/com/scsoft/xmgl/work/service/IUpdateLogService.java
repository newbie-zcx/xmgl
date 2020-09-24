package com.scsoft.xmgl.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.xmgl.work.entity.UpdateLog;

import java.util.List;

public interface IUpdateLogService extends IService<UpdateLog> {
    void add(int entityId,String beforeParams,String afterParams,String updateTable,String updateRow,String updateReason) throws ClassNotFoundException;
    List<UpdateLog> isUpdate(int entityId, String updateTable, String updateRow);
}
