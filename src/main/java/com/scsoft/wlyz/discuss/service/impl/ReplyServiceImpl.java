package com.scsoft.wlyz.discuss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scsoft.scpt.common.PageResult;
import com.scsoft.wlyz.common.handler.SystemCommonHandler;
import com.scsoft.wlyz.discuss.entity.ReplyFile;
import com.scsoft.wlyz.discuss.mapper.ReplyFileMapper;
import com.scsoft.wlyz.discuss.model.ReplyModel;
import com.scsoft.wlyz.system.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.scsoft.wlyz.discuss.entity.Reply;
import com.scsoft.wlyz.discuss.mapper.ReplyMapper;
import com.scsoft.wlyz.discuss.service.IReplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        ew.eq("ISSUE_ID", reply.getIssueId());
        return replyMapper.selectList(ew);
     }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveModel(ReplyModel replyModel) {
        Reply reply = new Reply();
        reply.setIssueId(replyModel.getIssueId());
        reply.setReplyDesc(replyModel.getReplyDesc());
        reply.setShowLevel(replyModel.getShowLevel());
        reply.setToReplyUserId(replyModel.getToReplyUserId());
        reply.setToReplyUserName(replyModel.getToReplyUserName());
        reply.setToReplyId(replyModel.getToReplyId());
        Integer successNum = this.replyMapper.insert(reply);

        List<Integer> fileList = replyModel.getFileList();
        if(!(null == fileList || fileList.size() == 0)){
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

    @Override
    public List<Reply> selectListByIssue(Reply reply) {
        User user = SystemCommonHandler.getLoginUser();
        String realName = user.getRealName();
        String toReplyUserName = reply.getToReplyUserName();

        /* 当登陆者为回复人，该议题下的所有信息都可看 */
        if(realName.equals(toReplyUserName)){
            QueryWrapper<Reply> ew = new QueryWrapper<>();
            ew.eq("ISSUE_ID", reply.getIssueId());
            ew.eq("TO_REPLY_USER_NAME", realName);
            ew.orderByDesc("create_date");
            return replyMapper.selectList(ew);
        }else{
            Map<String, Object> paramMap = new HashMap<>(8);
            paramMap.put("issueId", reply.getIssueId());
            paramMap.put("createName", realName);
            if(StringUtils.isNotBlank(toReplyUserName)){
                paramMap.put("toReplyUserName", toReplyUserName);
            }
//            return replyMapper.selectListByIssue(paramMap);
            return null;
        }
    }

    @Override
    public List<Reply> selectListByReplyId(Reply reply) {
        QueryWrapper<Reply> ew = new QueryWrapper<>();
        ew.eq("TO_REPLY_ID", reply.getToReplyId());
        ew.orderByDesc("create_date");
        return replyMapper.selectList(ew);
    }

    @Override
    public PageResult<Reply> selectPageListByIssue(Reply reply, Integer page, Integer limit) {
        User user = SystemCommonHandler.getLoginUser();
        String realName = user.getRealName();
        String toReplyUserName = reply.getToReplyUserName();

        /* 当登陆者为回复人，该议题下的所有信息都可看 */
        if(realName.equals(toReplyUserName)){
            QueryWrapper<Reply> ew = new QueryWrapper<>();
            ew.eq("ISSUE_ID", reply.getIssueId());
            ew.eq("TO_REPLY_USER_NAME", realName);
            ew.orderByDesc("create_date");
            Page<Reply> replyPage = new Page<>(page, limit);
            IPage iPage = replyMapper.selectPage(replyPage,ew);
            PageResult result = new PageResult<Reply>(iPage.getRecords(), iPage.getTotal());
            return result;
        }else{
            Map<String, Object> paramMap = new HashMap<>(8);
            paramMap.put("issueId", reply.getIssueId());
            paramMap.put("createName", realName);
            if(StringUtils.isNotBlank(toReplyUserName)){
                paramMap.put("toReplyUserName", toReplyUserName);
            }
            Page<Reply> replyPage = new Page<>(page, limit);
            IPage iPage = replyMapper.selectListByIssue(replyPage, paramMap);
            PageResult result = new PageResult<Reply>(iPage.getRecords(), iPage.getTotal());
            return result;
        }
    }
}
