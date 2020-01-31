package com.scsoft.xgsb.common.utils;/*
package com.scsoft.xgsb.common.utils;

import org.beetl.core.GeneralVarTagBinding;

import java.io.IOException;

*/
/**
 * @title: DictTag
 * @Description:
 * @Author: zhaopengfei
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @CreateDate: 2019/5/27 19:20
 * @Version: 1.0
 *//*

public class DictTag extends GeneralVarTagBinding {
    private  long total = 0;
    private  int perSize = 0;
    private int curNo = 0;
    private int pageNum = 0;
    private static final String SPAN = "span";
    private static final String A = "a";


    private void init(){
        Page page = (Page)getAttributeValue("val");
        total = page.getTotalNum();
        perSize = page.getPerSize();
        curNo = page.getCurNo();
        pageNum = (int)(total / perSize);
        if(pageNum * perSize < total){
            pageNum++;
        }
    }
    @Override
    public void render(){
        init();
        StringBuilder paging = new StringBuilder("");
        paging.append("<div class=\"pagination\">");
        if(curNo > 1){
            paging.append("<a class=\"firstPage\"> </a>")
                    .append("<a class=\"previousPage\"> </a>");
            if(curNo > 4){
                paging.append("<span class=\"pageBreak\">...</span>");
            }
            if(curNo - 2 > 0){
                paging.append("<a href=\"javascript:;\">").append(curNo - 2).append("</a>");
            }
            if(curNo - 1 > 0){
                paging.append("<a href=\"javascript:;\">").append(curNo - 1).append("</a>");
            }
        }else if(curNo == 1){
            paging.append("<span class=\"firstPage\"> </span>")
                    .append("<span class=\"previousPage\"> </span>");
        }
        paging.append("<span class=\"currentPage\">").append(curNo).append("</span>");
        if(curNo + 1 <= pageNum){
            paging.append("<a href=\"javascript:;\">").append(curNo + 1).append("</a>");
        }
        if(curNo + 2 <= pageNum){
            paging.append("<a href=\"javascript:;\">").append(curNo + 2).append("</a>");
        }
        if(pageNum - curNo > 2){
            paging.append("<span class=\"pageBreak\">...</span>");
        }
        if(curNo >= pageNum){
            paging.append("<span class=\"nextPage\" href=\"javascript:;\"> </span>")
                    .append("<span class=\"lastPage\" href=\"javascript:;\"> </span>");
        } else{
            paging.append("<a class=\"nextPage\" href=\"javascript:;\"> </a>")
                    .append("<a class=\"lastPage\" href=\"javascript:;\"> </a>");
        }
        paging.append("<span class=\"pageSkip\">")
                .append("共").append(pageNum).append("页").append(total).append("条记录 到第<input id=\"pageNumber\" name=\"pageNumber\" value=\"1\" maxlength=\"9\" onpaste=\"return false;\" />")
                .append("页<button type=\"submit\"> </button>")
                .append("</span></div>");
        try{
            ctx.byteWriter.writeString(paging.toString());
            this.doBodyRender();
            ctx.byteWriter.writeString(paging.toString());
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }



}
*/
