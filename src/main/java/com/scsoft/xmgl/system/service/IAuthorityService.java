package com.scsoft.xmgl.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.xmgl.system.entity.Authority;

import java.util.List;


/**
 * @Description: 数据菜单权限表 服务类
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
public interface IAuthorityService extends IService<Authority> {
    List<Authority> listByUserId(int userId);

    List<Authority> listMenu();

    List<Authority> listByRoleIds(List<String> roleId);

    List<Authority> listByRoleId(int roleId);
    boolean delete(int authorityId);
    boolean update(Authority Authority);
    boolean addRoleAuth(int roleId, int authId);

    boolean deleteRoleAuth(int roleId, int authId);

    boolean updateRoleAuth(int roleId, List<Integer> authIds);
}
