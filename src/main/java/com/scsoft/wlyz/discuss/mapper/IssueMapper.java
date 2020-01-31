package com.scsoft.xgsb.discuss.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scsoft.xgsb.discuss.entity.Issue;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chengshangshu
 * @since 2020-01-09
 */
public interface IssueMapper extends BaseMapper<Issue> {

    IPage<Issue> listPageByMap(IPage<Issue> page, QueryWrapper queryWrapper);

    List<Integer> hostIdList();
}
