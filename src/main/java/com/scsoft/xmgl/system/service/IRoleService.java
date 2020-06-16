package com.scsoft.xmgl.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.xmgl.system.entity.Role;

import java.util.List;

/**
 * @Description: 角色表 服务类
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
public interface IRoleService extends IService<Role> {
    List<Role> getByUserId(int userId);
    List<Role> list(boolean showDelete);
    Role getById(int roleId);
    boolean updateState(int roleId, int isDelete);  // 逻辑删除

}
