package com.spark.szhb_master.entity;

import java.io.Serializable;
import java.util.List;

public class BaseInfo {

    /**
     * about : [{"created_at":"2020-05-22 16:45:59 +0800 CST","id":1,"name":"服务条款","synopsis":""},{"created_at":"2020-05-22 16:46:22 +0800 CST","id":2,"name":"上币申请","synopsis":""},{"created_at":"2020-05-22 16:46:45 +0800 CST","id":3,"name":"公司简介","synopsis":""}]
     * android : {"apk_url":"","force_update":0,"min_version":"","new_version":"","update_description":""}
     * email : haocoin@163.com
     * help : [{"created_at":"2020-05-22 16:47:55 +0800 CST","id":7,"name":"新手帮助","synopsis":""},{"created_at":"2020-05-22 16:48:13 +0800 CST","id":8,"name":"官方公告","synopsis":""},{"created_at":"2020-05-22 16:48:32 +0800 CST","id":9,"name":"新闻公告","synopsis":""}]
     * ios : {"apk_url":"","force_update":0,"min_version":"","new_version":"","update_description":""}
     * newcomer : [{"created_at":"2020-06-04 16:29:51 +0800 CST","id":10,"name":"如何注册","synopsis":""},{"created_at":"2020-06-04 16:29:56 +0800 CST","id":11,"name":"如何绑定邮箱","synopsis":""},{"created_at":"2020-06-04 16:30:00 +0800 CST","id":12,"name":"如何进行资金划转","synopsis":""},{"created_at":"2020-06-04 16:30:04 +0800 CST","id":13,"name":"测试动态1","synopsis":""},{"created_at":"2020-06-04 16:32:47 +0800 CST","id":14,"name":"测试动态2","synopsis":""},{"created_at":"2020-06-04 16:32:55 +0800 CST","id":15,"name":"测试动态3","synopsis":""}]
     * news : []
     * photo : [{"link":"","url":"/user/photo/15886588073979545682239.jpeg"}]
     * terms : [{"created_at":"2020-05-22 16:47:04 +0800 CST","id":4,"name":"隐私条款","synopsis":""},{"created_at":"2020-05-22 16:47:18 +0800 CST","id":5,"name":"用户协议","synopsis":""},{"created_at":"2020-05-22 16:47:37 +0800 CST","id":6,"name":"交易费率","synopsis":""}]
     * wechat : haocoin
     */

    private AndroidBean android;
    private String email;
    private String wechat;
    private List<AboutBean> about;
    private List<HelpBean> help;
    private List<NewcomerBean> newcomer;
    private List<NewsBean> news;
    private List<PhotoBean> photo;
    private List<TermsBean> terms;

    public AndroidBean getAndroid() {
        return android;
    }

    public void setAndroid(AndroidBean android) {
        this.android = android;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public List<AboutBean> getAbout() {
        return about;
    }

    public void setAbout(List<AboutBean> about) {
        this.about = about;
    }

    public List<HelpBean> getHelp() {
        return help;
    }

    public void setHelp(List<HelpBean> help) {
        this.help = help;
    }

    public List<NewcomerBean> getNewcomer() {
        return newcomer;
    }

    public void setNewcomer(List<NewcomerBean> newcomer) {
        this.newcomer = newcomer;
    }

    public List<NewsBean> getNews() {
        return news;
    }

    public void setNews(List<NewsBean> news) {
        this.news = news;
    }

    public List<PhotoBean> getPhoto() {
        return photo;
    }

    public void setPhoto(List<PhotoBean> photo) {
        this.photo = photo;
    }

    public List<TermsBean> getTerms() {
        return terms;
    }

    public void setTerms(List<TermsBean> terms) {
        this.terms = terms;
    }

    public static class AndroidBean {
        /**
         * apk_url :
         * force_update : 0
         * min_version :
         * new_version :
         * update_description :
         */

        private String apk_url;
        private int force_update;
        private String min_version;
        private String new_version;
        private String update_description;

        public String getApk_url() {
            return apk_url;
        }

        public void setApk_url(String apk_url) {
            this.apk_url = apk_url;
        }

        public int getForce_update() {
            return force_update;
        }

        public void setForce_update(int force_update) {
            this.force_update = force_update;
        }

        public String getMin_version() {
            return min_version;
        }

        public void setMin_version(String min_version) {
            this.min_version = min_version;
        }

        public String getNew_version() {
            return new_version;
        }

        public void setNew_version(String new_version) {
            this.new_version = new_version;
        }

        public String getUpdate_description() {
            return update_description;
        }

        public void setUpdate_description(String update_description) {
            this.update_description = update_description;
        }
    }

    public static class IosBean {
        /**
         * apk_url :
         * force_update : 0
         * min_version :
         * new_version :
         * update_description :
         */

        private String apk_url;
        private int force_update;
        private String min_version;
        private String new_version;
        private String update_description;

        public String getApk_url() {
            return apk_url;
        }

        public void setApk_url(String apk_url) {
            this.apk_url = apk_url;
        }

        public int getForce_update() {
            return force_update;
        }

        public void setForce_update(int force_update) {
            this.force_update = force_update;
        }

        public String getMin_version() {
            return min_version;
        }

        public void setMin_version(String min_version) {
            this.min_version = min_version;
        }

        public String getNew_version() {
            return new_version;
        }

        public void setNew_version(String new_version) {
            this.new_version = new_version;
        }

        public String getUpdate_description() {
            return update_description;
        }

        public void setUpdate_description(String update_description) {
            this.update_description = update_description;
        }
    }

    public static class AboutBean implements Serializable {
        /**
         * created_at : 2020-05-22 16:45:59 +0800 CST
         * id : 1
         * name : 服务条款
         * synopsis :
         */

        private String created_at;
        private int id;
        private String name;
        private String synopsis;

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSynopsis() {
            return synopsis;
        }

        public void setSynopsis(String synopsis) {
            this.synopsis = synopsis;
        }
    }

    public static class NewsBean {
        /**
         * created_at : 2020-05-22 16:45:59 +0800 CST
         * id : 1
         * name : 服务条款
         * synopsis :
         */

        private String created_at;
        private int id;
        private String name;
        private String synopsis;

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSynopsis() {
            return synopsis;
        }

        public void setSynopsis(String synopsis) {
            this.synopsis = synopsis;
        }
    }

    public static class HelpBean {
        /**
         * created_at : 2020-05-22 16:47:55 +0800 CST
         * id : 7
         * name : 新手帮助
         * synopsis :
         */

        private String created_at;
        private int id;
        private String name;
        private String synopsis;

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSynopsis() {
            return synopsis;
        }

        public void setSynopsis(String synopsis) {
            this.synopsis = synopsis;
        }
    }

    public static class NewcomerBean {
        /**
         * created_at : 2020-06-04 16:29:51 +0800 CST
         * id : 10
         * name : 如何注册
         * synopsis :
         */

        private String created_at;
        private int id;
        private String name;
        private String synopsis;

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSynopsis() {
            return synopsis;
        }

        public void setSynopsis(String synopsis) {
            this.synopsis = synopsis;
        }
    }

    public static class PhotoBean {
        /**
         * link :
         * url : /user/photo/15886588073979545682239.jpeg
         */

        private String link;
        private String url;

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class TermsBean {
        /**
         * created_at : 2020-05-22 16:47:04 +0800 CST
         * id : 4
         * name : 隐私条款
         * synopsis :
         */

        private String created_at;
        private int id;
        private String name;
        private String synopsis;

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSynopsis() {
            return synopsis;
        }

        public void setSynopsis(String synopsis) {
            this.synopsis = synopsis;
        }
    }
}
