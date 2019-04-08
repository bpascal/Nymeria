package com.codido.nymeria.bean.req;


/**
 * 登录请求
 */
public class LoginReqData extends BaseReqData {

    /**
     * 序列号
     */
    private static final long serialVersionUID = 1L;

    private String mobile;
    private String pushId;
    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    @Override
    public String toString() {
        return "LoginReqData{" +
                "mobile='" + mobile + '\'' +
                ", pushId='" + pushId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
