package com.codido.nymeria.bean.vo;

import java.io.Serializable;

/**
 * 作者：Junjie.Lai on 2017/3/23 16:16
 * 邮箱：laijj@yzmm365.cn
 */

public class ParentInfo implements Serializable {

    private String userid;              //家长ID（非空）
    private String username;            //手机号
    private String realname;            //真实姓名
    private String imageUrl;            //照片地址
    private String relation;            //（学生家长关系）

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Override
    public String toString() {
        return "ParentInfo [userid=" + userid + ", username=" + username + ", realname=" + realname + ", imageUrl="
                + imageUrl + ", relation=" + relation + "]";
    }
}
