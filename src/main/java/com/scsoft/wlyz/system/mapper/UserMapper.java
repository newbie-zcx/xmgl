package com.scsoft.wlyz.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.wlyz.system.entity.User;
import com.scsoft.scpt.annotation.MyBatisDao;


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
}
