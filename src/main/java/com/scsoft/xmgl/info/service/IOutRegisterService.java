package com.scsoft.xmgl.info.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xmgl.info.entity.OutRegister;
import com.scsoft.xmgl.system.entity.User;

import java.util.List;

public interface IOutRegisterService extends IService<OutRegister> {
    PageResult<OutRegister> list(int pageNum, int pageSize,String userName);
    PageResult<OutRegister> deptList(int pageNum, int pageSize, List<User> infoList);
    boolean add(OutRegister outRegister);
    boolean update(OutRegister outRegister);
    boolean delete(int id);
    boolean updateState(int id,Integer approval);
}
