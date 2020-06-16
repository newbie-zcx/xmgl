package com.scsoft.xmgl.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.xmgl.system.entity.DictGroup;
import com.scsoft.scpt.common.PageResult;

/**
 * <p>
 * 字典分组 服务类
 * </p>
 * @author zhaopengfei
 * @CreateDate 2019-04-26
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @Version: 1.0
 */
public interface IDictGroupService extends IService<DictGroup> {

    // 通过组id 查找
  DictGroup  findById(Integer dictGroupId);
    PageResult findAll(Page page, String condition);
    boolean  logicDelete(DictGroup dictGroup);
}
