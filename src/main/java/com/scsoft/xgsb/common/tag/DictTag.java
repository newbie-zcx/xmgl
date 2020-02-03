package com.scsoft.xgsb.common.tag;

import com.scsoft.xgsb.system.entity.Dict;
import com.scsoft.xgsb.system.service.IDictService;
import org.apache.commons.lang3.StringUtils;
import org.beetl.core.GeneralVarTagBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @title: DictTag
 * @Description:
 * @Author: zhaopengfei
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @CreateDate: 2019/5/27 19:20
 * @Version: 1.0
 */
@Service
@Scope("prototype")
@BeetlTagName("dict_tag")
public class DictTag extends GeneralVarTagBinding {
    private  String name;
    private  String id;
    private String cssclass;
    private String dictCode ;
    private String layverify;
    private String defaultVal;
    private String value;
    private String readonly ;
    private String layfilter;

    private List<Dict> dictList = new ArrayList<Dict>();
    private static final String A = "a";
    @Autowired
    private IDictService dictService;

    private void init(){
         name = (String) getAttributeValue("name");
         id = (String) getAttributeValue("id");
         cssclass = (String) getAttributeValue("class");
         dictCode = (String) getAttributeValue("dictCode");
         defaultVal = (String) getAttributeValue("defaultVal");
         layverify = (String) getAttributeValue("layVerify");
         layfilter = (String) getAttributeValue("layFilter");
         readonly = (String) getAttributeValue("readonly");
         value =  getAttributeValue("value").toString();
         dictList =dictService.getByGroupCode(dictCode);
    }
    @Override
    public void render(){
        init();
        StringBuilder sb = new StringBuilder("");
        sb.append("<select id=\""+id+"\" name=\""+name+"\" ");
        if (StringUtils.isNotBlank(cssclass)){
            sb.append(" class=\""+cssclass+"\"");
        }
        if (StringUtils.isNotBlank(layverify)){
            sb.append(" layverify=\""+layverify+"\"");
        }
        if (StringUtils.isNotBlank(readonly)){
            sb.append(" readonly ");
        }
        if (StringUtils.isNotBlank(dictCode)){
            sb.append(" dictCode=\""+dictCode+"\"");
        }
        if (StringUtils.isNotBlank(layfilter)){
            sb.append(" lay-filter=\""+layfilter+"\"");
        }
        sb.append(" >");
        for (Dict dict:dictList){
            if (StringUtils.isNotBlank(value)){
                if (dict.getValue().equals(value)){
                    sb.append("<option value=\""+dict.getValue()+"\" selected=\"selected\">");
                }else {
                    sb.append("<option value=\""+dict.getValue()+"\">");
                }
            }else{
                if (dict.getValue().equals(defaultVal)){
                    sb.append("<option value=\""+dict.getValue()+"\" selected=\"selected\">");
                }else {
                    sb.append("<option value=\""+dict.getValue()+"\">");
                }
            }

            sb.append(dict.getLabel());
            sb.append("</option>");
        }
        sb.append("</select>");
        try{
//            this.doBodyRender();
//            ctx.byteWriter.writeString(sb.toString());
            ctx.byteWriter.writeString(sb.toString());
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }



}

