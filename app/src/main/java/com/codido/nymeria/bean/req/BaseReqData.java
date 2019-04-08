package com.codido.nymeria.bean.req;

import com.codido.nymeria.util.Global;

import java.io.Serializable;

/**
 * 请求类基类
 */
public class BaseReqData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String plat = "1";
    private String ver = Global.version;
    private String ov = Global.osVer;
    private String imei = Global.deviceId;
    private String src = Global.umengChannel;

    private String sid = Global.sid;
    private String userId;
    private long t = System.currentTimeMillis();

    private String m = android.os.Build.MODEL;
    private String b = android.os.Build.BRAND;


    public BaseReqData() {
        if (Global.userInfo != null) {
            userId = Global.userInfo.getUserId();
        }
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getOv() {
        return ov;
    }

    public void setOv(String ov) {
        this.ov = ov;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public long getT() {
        return t;
    }

    public void setT(long t) {
        this.t = t;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    @Override
    public String toString() {
        return "BaseReqData{" +
                "plat='" + plat + '\'' +
                ", ver='" + ver + '\'' +
                ", ov='" + ov + '\'' +
                ", imei='" + imei + '\'' +
                ", src='" + src + '\'' +
                ", sid='" + sid + '\'' +
                ", userId='" + userId + '\'' +
                ", t=" + t +
                ", m='" + m + '\'' +
                ", b='" + b + '\'' +
                '}';
    }
}
