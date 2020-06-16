package com.scsoft.xmgl.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.xmgl.system.entity.Role;
import com.scsoft.scpt.annotation.MyBatisDao;

import java.util.List;


/**
 * @Description: 角色表 Mapper 接口
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@MyBatisDao
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户ID获取该用户所在组的角色权限
     */
    List<Role> selectByUserId(int userId);
}
