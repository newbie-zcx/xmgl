package com.scsoft.xmgl.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.xmgl.system.entity.Depart;
import com.scsoft.xmgl.system.entity.Role;
import com.scsoft.xmgl.system.entity.User;
import com.scsoft.scpt.annotation.MyBatisDao;

import java.util.List;


/**
 * @Description: 系统用户表 Mapper 接口
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@MyBatisDao
public interface UserMapper extends BaseMapper<User> {
    User getByUsername(String username);
    User getByRealName(String realName);
    List<User> getBySection(String section);
    List<Role> getRoleByid(int id);
    List<User> getByDepart(Integer departId);
}
