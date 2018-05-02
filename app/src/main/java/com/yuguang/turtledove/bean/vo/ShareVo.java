package com.codido.nymeria.bean.vo;

import java.io.Serializable;

/**
 * Created by bpascal on 2017/1/3.
 */
public class ShareVo implements Serializable {

    /**
     * 订单状态常量
     */
    public static final String ORDER_STS_INIT = "U";//初始状态
    public static final String ORDER_STS_ON = "D";//已匹配
    public static final String ORDER_STS_PAYED = "W";//已确认打款
    public static final String ORDER_STS_ENSURE = "S";//交易成功，对方已确认
    public static final String ORDER_STS_FAILED = "F";//交易失败

    /**
     * 订单号
     */
    private String ordNo;

    /**
     * 订单日期
     */
    private String ordDt;

    /**
     * 订单金额
     */
    private String ordAmt;

    /**
     * 订单状态
     */
    private String ordSts;

    public String getOrdNo() {
        return ordNo;
    }

    public void setOrdNo(String ordNo) {
        this.ordNo = ordNo;
    }

    public String getOrdDt() {
        return ordDt;
    }

    public void setOrdDt(String ordDt) {
        this.ordDt = ordDt;
    }

    public String getOrdAmt() {
        return ordAmt;
    }

    public void setOrdAmt(String ordAmt) {
        this.ordAmt = ordAmt;
    }

    public String getOrdSts() {
        return ordSts;
    }

    public void setOrdSts(String ordSts) {
        this.ordSts = ordSts;
    }

    @Override
    public String toString() {
        return "ShareVo{" +
                "ordNo='" + ordNo + '\'' +
                ", ordDt='" + ordDt + '\'' +
                ", ordAmt='" + ordAmt + '\'' +
                ", ordSts='" + ordSts + '\'' +
                '}';
    }
}
