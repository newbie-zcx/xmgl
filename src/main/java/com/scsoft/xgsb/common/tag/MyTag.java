package com.scsoft.xgsb.common.tag;

import org.beetl.core.GeneralVarTagBinding;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @title: MyTag
 * @Description:
 * @Author: zhaopengfei
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @CreateDate: 2019/5/28 11:27
 * @Version: 1.0
 */
@Service
@Scope("prototype")
@BeetlTagName("my_tag")
public class MyTag extends GeneralVarTagBinding {
    @Override
    public void render() {
        // TODO 标签处理
    }
}
