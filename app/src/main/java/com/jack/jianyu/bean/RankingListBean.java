package com.jack.jianyu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author:S.jack
 * data:2016-01-13 17:13
 */


public class RankingListBean implements Serializable{

    private int showapi_res_code;

    private String showapi_res_error;

    private Showapi_res_body showapi_res_body;

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public Showapi_res_body getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(Showapi_res_body showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    public static class Showapi_res_body {
        private RankingPagebean pagebean;

        private int ret_code;

        public void setPagebean(RankingPagebean pagebean) {
            this.pagebean = pagebean;
        }

        public RankingPagebean getPagebean() {
            return this.pagebean;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public int getRet_code() {
            return this.ret_code;
        }
    }

    public static class RankingPagebean {
        private int cur_song_num;

        private int currentPage;

        private int ret_code;

        private List<MusicContentListBean> songlist;

        public void setCur_song_num(int cur_song_num) {
            this.cur_song_num = cur_song_num;
        }

        public int getCur_song_num() {
            return this.cur_song_num;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getCurrentPage() {
            return this.currentPage;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public int getRet_code() {
            return this.ret_code;
        }

        public void setSonglist(List<MusicContentListBean> songlist) {
            this.songlist = songlist;
        }

        public List<MusicContentListBean> getSonglist() {
            return this.songlist;
        }

    }

}
