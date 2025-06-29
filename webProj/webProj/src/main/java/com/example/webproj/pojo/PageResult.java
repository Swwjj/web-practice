package com.example.webproj.pojo;

import java.util.List;

    public class PageResult<T> {
        private Integer pageNum;     // 当前页码
        private Integer pageSize;    // 每页记录数
        private Integer totalRecord; // 总记录数
        private Integer totalPage;   // 总页数
        private Integer startIndex;  // 起始索引
        private List<T> data;        // 当前页数据
        private Integer prePage;     // 上一页
        private Integer nextPage;    // 下一页

        // 构造方法
        public PageResult() {
        }

        public PageResult(Integer pageNum, Integer pageSize, Integer totalRecord, List<T> data) {
            this.pageNum = pageNum;
            this.pageSize = pageSize;
            this.totalRecord = totalRecord;
            this.data = data;

            // 计算其他分页属性
            this.totalPage = (totalRecord + pageSize - 1) / pageSize;
            this.startIndex = (pageNum - 1) * pageSize;
            this.prePage = pageNum > 1 ? pageNum - 1 : 1;
            this.nextPage = pageNum < totalPage ? pageNum + 1 : totalPage;
        }

        // Getter和Setter方法
        public Integer getPageNum() {
            return pageNum;
        }

        public void setPageNum(Integer pageNum) {
            this.pageNum = pageNum;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public Integer getTotalRecord() {
            return totalRecord;
        }

        public void setTotalRecord(Integer totalRecord) {
            this.totalRecord = totalRecord;
        }

        public Integer getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(Integer totalPage) {
            this.totalPage = totalPage;
        }

        public Integer getStartIndex() {
            return startIndex;
        }

        public void setStartIndex(Integer startIndex) {
            this.startIndex = startIndex;
        }

        public List<T> getData() {
            return data;
        }

        public void setData(List<T> data) {
            this.data = data;
        }

        public Integer getPrePage() {
            return prePage;
        }

        public void setPrePage(Integer prePage) {
            this.prePage = prePage;
        }

        public Integer getNextPage() {
            return nextPage;
        }

        public void setNextPage(Integer nextPage) {
            this.nextPage = nextPage;
        }
    }

