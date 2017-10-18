package com.dc.server.util;

public class PageUtil {

    public static int getPageNum(int pageNum, int totalPage) {

        if (pageNum < 1) {
            pageNum = 1;
        }
        if (pageNum > totalPage) {
            pageNum = totalPage;
        }
        return pageNum;
    }
}
