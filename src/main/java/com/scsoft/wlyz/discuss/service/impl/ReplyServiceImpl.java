package com.scsoft.wlyz.discuss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.wlyz.discuss.entity.ReplyFile;
import com.scsoft.wlyz.discuss.mapper.ReplyFileMapper;
import com.scsoft.wlyz.discuss.model.ReplyModel;
import org.springframework.stereotype.Service;
import com.scsoft.wlyz.discuss.entity.Reply;
import com.scsoft.wlyz.discuss.mapper.ReplyMapper;
import com.scsoft.wlyz.discuss.service.IReplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 回复表 服务实现类
 * </p>
 *
 * @author wangchao
 * @since 2020-01-08
 */
@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements IReplyService {

    @Resource
    private ReplyMapper replyMapper;

    @Resource
    private ReplyFileMapper replyFileMapper;

    /**
     * 通过分页查询
     * @param page
     * @return PageResult
     */
    @Override
    public PageResult<Reply> listPage(Integer page, Integer limit, Reply reply) {
       Page<Reply> replyPage = new Page<Reply>(page, limit);
       QueryWrapper<Reply> ew = new QueryWrapper<Reply>();
       IPage iPage = replyMapper.selectPage(replyPage,ew);
       PageResult result = new PageResult<Reply>(iPage.getRecords(), iPage.getTotal());
       return result;
    }

    @Override
    public List<Reply> selectList(Reply reply) {
        QueryWrapper<Reply> ew = new QueryWrapper<Reply>();
        return replyMapper.selectList(ew);

     }

    @Override
    @Transactional
    public boolean saveModel(ReplyModel replyModel) {
        Reply reply = new Reply();
        reply.setIssueId(replyModel.getIssueId());
        reply.setReplyDesc(replyModel.getReplyDesc());
        reply.setShowLevel(replyModel.getShowLevel());
        Integer successNum = this.replyMapper.insert(reply);

        List<Integer> fileList = replyModel.getFileList();
        if(null == fileList || fileList.size() == 0){

        }else{
            for(Integer file : fileList){
                ReplyFile replyFile = new ReplyFile();
                replyFile.setFileId(file);
                replyFile.setReplyId(reply.getId());
                replyFileMapper.insert(replyFile);
            }
        }
        if(successNum >= 1){
            return true;
        }else {
            return false;
        }
    }

}
