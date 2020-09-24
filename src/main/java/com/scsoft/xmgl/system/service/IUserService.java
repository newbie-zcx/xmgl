package com.scsoft.xmgl.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.xmgl.system.entity.Role;
import com.scsoft.xmgl.system.entity.User;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.scpt.exception.BusinessException;
import com.scsoft.scpt.exception.ParameterException;
import org.apache.ibatis.annotations.Param;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 系统用户表 服务类
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
public interface IUserService extends IService<User> {

    /**
     * 根据条件查询用户列表
     */
    List<Map<String, Object>> selectUsers(@Param("name") String name, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("deptid") int deptid);

    User getByUsername(String username);
    User getByRealName(String realName);
    PageResult<User> list(int pageNum, int pageSize, boolean showDelete, String searchKey, String searchValue, String departId);

    User getById(int userId);
    /**
     * 修改用户状态
     */
    boolean updateState(int userId, int state) throws ParameterException;
    /**
     * 修改密码
     */
    boolean updatePsw(int userId, String username, String newPsw);
    boolean add(User user, String departId) throws BusinessException;

    boolean update(User user);
    boolean updateUser(User user);
    boolean delete(int userId);
    User getRealNameByLoginName(String LoginName);
    List<User> getRealNameBySection(String section);
    List<Role> getRoleByid(int id);

    /**
    * 用户工时统计
    **/
    PageResult<User> countList(int pageSize,int pageNum,Integer proId,String realName,String startDate,String endDate) throws ParseException;
}
