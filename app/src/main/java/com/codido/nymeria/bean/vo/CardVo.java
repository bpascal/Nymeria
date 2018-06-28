package com.codido.nymeria.bean.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 21:40
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode
public class CardVo implements Serializable {

    /**
     * 内部编号
     */
    private String cardId;

    /**
     * 卡号
     */
    private String cardNo;

    /**
     * 卡类型
     * 0:借记卡
     * 1:信用卡
     */
    private String cardType;

    /**
     * 归属银行名称
     */
    private String bankName;

    /**
     * 归属银行图标
     */
    private String bankIcon;

    /**
     * 卡还款状态
     * 0:未设置还款计划
     * 1:已设置
     * 2:还款中
     */
    private String cardPayoffSts;

    /**
     * 账单日
     */
    private String billDay;

    /**
     * 还款日
     */
    private String payoffDay;
}
