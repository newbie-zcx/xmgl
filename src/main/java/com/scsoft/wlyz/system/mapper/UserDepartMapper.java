package com.scsoft.xgsb.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.xgsb.system.entity.UserDepart;
import com.scsoft.scpt.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * @Description: 用户-部门 Mapper 接口
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@MyBatisDao
public interface UserDepartMapper extends BaseMapper<UserDepart> {
   @Select("select * from sys_user_depart where user_id=#{userId} and depart_id=#{departId}")
   UserDepart   selectByUserId(@Param("userId") int userId, @Param("departId") int departId);
}
