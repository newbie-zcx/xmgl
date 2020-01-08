package com.scsoft.wlyz.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.wlyz.system.entity.Depart;

import java.util.List;

/**
 * @Description: 机构部门表 服务类
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
public interface IDepartService extends IService<Depart> {
    List<Depart> findDepartByName(String name);
    List<Depart> getByUserId(int userId);
    List<Depart> getByPId(int pid);
    List<Depart> getByPId(List<Depart> pids);
    List<Depart> selectNotInId(int departId);
}
