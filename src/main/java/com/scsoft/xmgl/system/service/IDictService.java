package com.scsoft.xmgl.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scsoft.xmgl.system.entity.Dict;
import com.scsoft.scpt.common.PageResult;

import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 * @author zhaopengfei
 * @CreateDate 2019-04-26
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @Version: 1.0
 */
public interface IDictService extends IService<Dict> {
    //通过dictId查询
    List<Dict> getBygid(Integer gid);
    //添加一个数据
    boolean save(Dict dict);
    PageResult findbyGidPage(Page page, Integer gid, String code, String condition);
    PageResult findAll(Page page, String condition);
    //通过字典组查询
    List<Dict> getByGroupCode(String code);
}
