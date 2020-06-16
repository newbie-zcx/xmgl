package com.scsoft.xmgl.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.xmgl.system.entity.Authority;
import com.scsoft.scpt.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Description: 数据菜单权限表 Mapper 接口
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@MyBatisDao
public interface AuthorityMapper extends BaseMapper<Authority> {
    List<Authority> listByUserId(int userId);

    List<Authority> listByRoleIds(@Param("roleIds") List<String> roleIds);

    List<Authority> listByRoleId(int roleId);

}
