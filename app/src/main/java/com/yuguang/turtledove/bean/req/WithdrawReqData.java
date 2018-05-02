package com.codido.nymeria.bean.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/8 00:03
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WithdrawReqData extends BaseReqData {

    /**
     * 内部卡编号
     */
    private String cardId;

    /**
     * 提现金额
     */
    private String withdrawAmount;

    /**
     * 支付密码
     */
    private String payPwd;

    /**
     * CASH:现金提现
     * SHARE:收益提现
     */
    private String withdrawType;
}
