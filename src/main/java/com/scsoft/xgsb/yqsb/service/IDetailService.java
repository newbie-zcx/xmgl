package com.scsoft.xgsb.yqsb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.xgsb.yqsb.entity.Detail;

import java.util.List;

/**
 * <p>
 * 疫情上报详情 服务类
 * </p>
 *
 * @author 赵鹏飞
 * @since 2020-01-31
 */

public interface IDetailService extends IService<Detail> {
    /**
     *  分页查询结果集
     * @param detail
     */
    PageResult<Detail> listPage(int pageNum, int pageSize,Detail detail,String createDate);
    //本人分页查询
    PageResult<Detail> oneselfListPage(int pageNum, int pageSize,Detail detail);
    //部门分页查询
    PageResult<Detail> departmentListPage(int pageNum, int pageSize,Detail detail,List<String> list,String createDate);
    /**
     *  分页查询结果集
     * @param detail
     */
    PageResult<Detail> listTodayPage(int pageNum, int pageSize,Detail detail,String type);
    /**
      *  list查询
      */
    List<Detail> selectList(Detail detail);

    /**
      *  逻辑删除
      */
    Boolean logicDelete(Detail detail);

    /**
     *  是否重复提交
     */
    Detail tijiao(Detail detail);
}

