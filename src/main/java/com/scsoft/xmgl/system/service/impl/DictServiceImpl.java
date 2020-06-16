package com.scsoft.xmgl.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scsoft.xmgl.common.handler.SystemCommonHandler;
import com.scsoft.xmgl.common.redis.RedisUtil;
import com.scsoft.xmgl.system.entity.Dict;
import com.scsoft.xmgl.system.entity.DictGroup;
import com.scsoft.xmgl.system.mapper.DictGroupMapper;
import com.scsoft.xmgl.system.mapper.DictMapper;
import com.scsoft.xmgl.system.service.IDictService;
import com.scsoft.scpt.common.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author zhaopengfei
 * @CreateDate 2019-04-26
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @Version: 1.0
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {
     @Resource
     private DictMapper dictMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private DictGroupMapper dictGroupMapper;

     public Dict getById(Integer dictId) {
          return dictMapper.selectById(dictId);
     }

    /**
     * 通过gid查询
     * @param gid
     * @return
     */
     @Override
     public List<Dict> getBygid(Integer gid) {
        return  dictMapper.getBygid(gid);
     }

    /**
     * 通过gid分页查询
     * @param page
     * @param gid
     * @return PageResult
     */
     @Override
     public PageResult findbyGidPage(Page page,Integer gid,String code,String condition) {
          QueryWrapper<Dict> ew = new QueryWrapper<Dict>();
         ew.eq("gid",gid);
          if (StringUtils.isNotBlank(code)){
              ew.like("label",code);
          }
         if (StringUtils.isNotBlank(condition)) {
             ew.like("code", condition);
         }
         List<Dict> dicts = dictMapper.selectPage(page, ew).getRecords();
         return new PageResult<Dict>(dicts, page.getTotal());
     }

     @Override
     public PageResult findAll(Page page, String condition) {
          QueryWrapper<Dict> ew = new QueryWrapper<Dict>();
          if (StringUtils.isNotBlank(condition)) {
               ew.like("code", condition);
          }
          IPage iPage = dictMapper.selectPage(page,ew);
          List<Dict> dicts = iPage.getRecords();
          return new PageResult<Dict>(dicts, page.getTotal());
     }

    @Override
    public List<Dict> getByGroupCode(String code) {
         QueryWrapper<Dict> qw=new QueryWrapper<Dict>();
        QueryWrapper<DictGroup> gqw=new QueryWrapper<DictGroup>();
         List<Dict> dictlist= (List<Dict>) redisUtil.get(SystemCommonHandler.SYSTEM_DICT+code);
        if(dictlist!=null&&dictlist.size()>0){
            return dictlist;
        }else {
            gqw.eq("code",code);
            DictGroup dg=dictGroupMapper.selectOne(gqw);
            if (null==dg){
                return null;
            }else {
                qw.eq("gid",dg.getId());
                return dictMapper.selectList(qw);
            }
        }
    }

}
