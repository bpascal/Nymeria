package com.codido.nymeria.bean.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 23:53
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode
public class TxnOrderVo implements Serializable {

    /**
     * 记录ID
     */
    private String orderId;

    /**
     * 银行卡内部编号
     */
    private String cardId;

    /**
     * 卡号后4位
     */
    private String cardNo;

    /**
     * 订单类型
     */
    private String orderType;

    /**
     * 订单类型名称
     */
    private String orderTypeName;

    /**
     * 订单金额
     */
    private String orderAmount;

    /**
     * 订单时间
     */
    private String orderTime;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 卡类型 0 借记卡 1信用卡
     */
    private String cardType;

    /**
     *银行ICON
     */
    private String bankIcon;




}
