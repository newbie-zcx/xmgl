package com.scsoft.xmgl.common.utils;

import java.util.ArrayList;
import java.util.List;

public class PageResultUtils {
    public static List paginAtion(Integer page, Integer limit,List basicDataHeadList){
        //分页
        List currentPageList = new ArrayList();
        if (basicDataHeadList != null && basicDataHeadList.size() > 0) {
            int currIdx = (page > 1 ? (page - 1) * limit : 0);
            for (int i = 0; i < limit && i < basicDataHeadList.size() - currIdx; i++) {
                currentPageList.add(basicDataHeadList.get(currIdx + i));
            }
        }
        return currentPageList;
    }
}
