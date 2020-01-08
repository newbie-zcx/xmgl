package com.scsoft.wlyz.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.wlyz.system.entity.UserRole;
import com.scsoft.scpt.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Description: 用户-角色 Mapper 接口
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@MyBatisDao
public interface UserRoleMapper extends BaseMapper<UserRole> {

        List<UserRole> selectByUserIds(@Param("userIds") List<Integer> userIds);

        int insertBatch(@Param("userId") int userId, @Param("roleIds") List<Integer> roleIds);
}
