package com.codido.nymeria.bean.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 23:06
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryMyBalanceResp extends BaseResp {

    /**
     * 总资产
     */
    private String totalBalance;

    /**
     * 冻结资金
     */
    private String totalHoldBal;

    /**
     * 冻结余额
     */
    private String holdCashBal;

    /**
     * 冻结收益
     */
    private String holdEarnBal;

    /**
     * 可用金额
     */
    private String totalAbleBal;

    /**
     * 可用账户余额
     */
    private String ableCashBal;

    /**
     * 可用收益余额
     */
    private String ableEarnBal;
}
