package com.spark.szhb_master.entity;

import java.util.List;

public class BannerInfo {

    private String email;
    private String wechat;
    private List<AboutBean> about;
    private List<HelpBean> help;
    private List<PhotoBean> photo;
    private List<TermsBean> terms;

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

    public static class AboutBean {
        /**
         * created_at : 2020-05-22 16:45:59 +0800 CST
         * id : 1
         * name : 服务条款
         */

        private String created_at;
        private int id;
        private String name;

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
    }

    public static class HelpBean {
        /**
         * created_at : 2020-05-22 16:47:55 +0800 CST
         * id : 7
         * name : 新手帮助
         */

        private String created_at;
        private int id;
        private String name;

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
         */

        private String created_at;
        private int id;
        private String name;

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
    }
}
