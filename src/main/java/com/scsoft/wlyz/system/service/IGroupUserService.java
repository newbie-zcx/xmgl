package com.scsoft.xgsb.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.xgsb.system.entity.GroupUser;
import com.scsoft.xgsb.system.entity.User;
import com.scsoft.scpt.common.PageResult;


/**
 * 用户组与用户
 */
public interface IGroupUserService  extends IService<GroupUser> {
    PageResult<User> list(int pageNum, int pageSize, String userName, Integer gid);
   // 查询没有的用户
   PageResult<User> findnotlist(int pageNum, int pageSize, String searchKey, String searchValue, Integer gid, String departId);
   Boolean add(Integer uid, Integer gid);
}
