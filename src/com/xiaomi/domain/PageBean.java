package com.xiaomi.domain;

import java.util.List;

public class PageBean<T> {
    private int pageNum;
    private int pageSize;
    private long totalSize;
    private int pageCount;
    private List<T> data;
    private int startPage;
    private int endPage;

    public PageBean() {
    }

    public PageBean(int pageNum, int pageSize, long totalSize, List<T> data) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
        this.data = data;
        this.pageCount = (int)((this.totalSize % this.pageSize) == 0 ? (this.totalSize / this.pageSize) : (this.totalSize / this.pageSize + 1));
        //计算导航的开始页和结束页
        //正常情况，当前页-4  +5
        this.startPage = pageNum - 4;
        this.endPage = pageNum + 5;
        //第一种情况：当前页码小于5
        if (pageNum < 5) {
            this.startPage = 1;
            this.endPage = 10;
        }
        //第二种情况：当前页大于总页数-5
        if (pageNum > (pageCount - 5)) {
            this.startPage = pageCount - 9;
            this.endPage = pageCount;
        }
        //第三种情况：总页数小于10
        if (pageCount < 10) {
            this.startPage = 1;
            this.endPage = pageCount;
        }
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }
}
