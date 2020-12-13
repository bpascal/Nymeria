package com.codido.nymeria.bean.resp;

import com.codido.nymeria.util.Global;

import java.io.Serializable;
import java.util.List;

/**
 * 联网请求回应对象
 * <p>
 * 修改于：2015年7月15日23:07:56
 *
 * @author 刘凯夫
 */
public class BaseResp implements Serializable {

    /**
     * 特定返回码：返回成功
     */
    public static final String RSP_CODE_OK = "0000";
    /**
     * 特定返回码：喜欢一个文章时，返回文章已经被指定用户喜欢
     */
    public static final String RSP_CODE_AGREE_ARTICLE_AGREED = "1521";

    /**
     * 特定返回码：SessionId异常，如：账号在其他设备登录，本机sid失效
     */
    public static final String RSP_CODE_SID_ERR = "2000";//用户不存在
    public static final String RSP_CODE_SESSION_TIMEOUT = "2054";//session超时
    public static final String RSP_CODE_SESSION_FAILD = "2055";//在其他地方登陆

    /**
     * 特定返回错误码:竞拍的时候出价太低
     */
    public static final String RSP_CODE_ACUT_ERR = "8315";

    /**
     * 网络异常
     */
    public static final String RESP_NET_ERROR = "NetErr";


    private static final long serialVersionUID = 1L;

    /**
     * 积分值
     */
    private int singleCredit;

    /**
     * 积分描述
     */
    private String creditDesc;

    /**
     * 返回码
     */
    private String respCode;
    /**
     * 返回消息
     */
    private String respMsg;
    /**
     * 请求key
     */
    private String key;

    /**
     * sessionId
     */
    private String sid;

    /**
     * session是否无效
     *
     * @return
     */
    public boolean isSidError() {
        //respCode.equals(RSP_CODE_SID_ERR)
        if(Global.key_login.equals(key)){
            return respCode != null && (respCode.equals(RSP_CODE_SESSION_TIMEOUT) || respCode.equals(RSP_CODE_SESSION_FAILD));
        }else{
            return respCode != null && (respCode.equals(RSP_CODE_SID_ERR)|| respCode.equals(RSP_CODE_SESSION_TIMEOUT) || respCode.equals(RSP_CODE_SESSION_FAILD));
        }

    }

    /**
     * 返回是否是成功的
     *
     * @return
     */
    public boolean isOk() {
        return respCode != null && respCode.equals(RSP_CODE_OK);
    }

    public String getRespCode() {
        return respCode;
    }

    public String getRespMsg() {
        if (respMsg == null) {
            respMsg = "请求失败，请检查网络设置后重试";
        }
        return respMsg;
    }

    public String getKey() {
        return key;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getSingleCredit() {
        return singleCredit;
    }

    public String getCreditDesc() {
        return creditDesc;
    }

    public void setSingleCredit(int singleCredit) {
        this.singleCredit = singleCredit;
    }

    public void setCreditDesc(String creditDesc) {
        this.creditDesc = creditDesc;
    }

    @Override
    public String toString() {
        return "BaseResp{" +
                "singleCredit=" + singleCredit +
                ", creditDesc='" + creditDesc + '\'' +
                ", respCode='" + respCode + '\'' +
                ", respMsg='" + respMsg + '\'' +
                ", key='" + key + '\'' +
                ", sid='" + sid + '\'' +
                '}';
    }
}
