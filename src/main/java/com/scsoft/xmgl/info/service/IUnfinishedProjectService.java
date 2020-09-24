package com.scsoft.xmgl.info.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.scpt.exception.BusinessException;
import com.scsoft.xmgl.info.entity.Info;

import java.util.List;


public interface IUnfinishedProjectService extends IService<Info> {
    PageResult<Info> list(int pageNum, int pageSize,String proName,String proManager,String proDept);
    boolean add(Info info) throws BusinessException;
    boolean delete(int id);
    boolean update(Info info);
    List<Info> listInfo();
    PageResult<Info> listInfoByManager(int pageNum, int pageSize,String realName);
    PageResult infoList(int pageNum, int pageSize,String realName);
    PageResult infoList1(int pageNum,int pageSize,String section);
}
