package com.scsoft.xmgl.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scsoft.xmgl.system.entity.Depart;
import com.scsoft.scpt.annotation.MyBatisDao;

import java.util.List;


/**
 * @Description: 机构部门表 Mapper 接口
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@MyBatisDao
public interface DepartMapper extends BaseMapper<Depart> {


    /**
     * 根据用户ID获取该用户的机构
     */
    List<Depart> selectByUserId(int userId);
    List<Depart> selectNotInId(int departId);
}
