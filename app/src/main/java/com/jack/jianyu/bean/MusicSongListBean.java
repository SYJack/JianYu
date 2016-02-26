package com.jack.jianyu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author:S.jack
 * data:2016-01-15 14:35
 */

public class MusicSongListBean implements Serializable {
    private int showapi_res_code;

    private String showapi_res_error;

    private Showapi_res_body showapi_res_body;

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public int getShowapi_res_code() {
        return this.showapi_res_code;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public String getShowapi_res_error() {
        return this.showapi_res_error;
    }

    public void setShowapi_res_body(Showapi_res_body showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    public Showapi_res_body getShowapi_res_body() {
        return this.showapi_res_body;
    }

    public static class Showapi_res_body {
        private Pagebean pagebean;

        private int ret_code;

        public void setPagebean(Pagebean pagebean) {
            this.pagebean = pagebean;
        }

        public Pagebean getPagebean() {
            return this.pagebean;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public int getRet_code() {
            return this.ret_code;
        }

    }

    public static class Pagebean {
        private int allNum;

        private int allPages;

        private List<MusicContentListBean> contentlist;

        private int currentPage;

        private int maxResult;

        private String notice;

        private int ret_code;

        public void setAllNum(int allNum) {
            this.allNum = allNum;
        }

        public int getAllNum() {
            return this.allNum;
        }

        public void setAllPages(int allPages) {
            this.allPages = allPages;
        }

        public int getAllPages() {
            return this.allPages;
        }

        public void setContentlist(List<MusicContentListBean> contentlist) {
            this.contentlist = contentlist;
        }

        public List<MusicContentListBean> getContentlist() {
            return this.contentlist;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getCurrentPage() {
            return this.currentPage;
        }

        public void setMaxResult(int maxResult) {
            this.maxResult = maxResult;
        }

        public int getMaxResult() {
            return this.maxResult;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        public String getNotice() {
            return this.notice;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public int getRet_code() {
            return this.ret_code;
        }

    }
}
