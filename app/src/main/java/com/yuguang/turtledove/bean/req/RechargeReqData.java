package com.codido.nymeria.bean.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/8 00:01
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RechargeReqData extends BaseReqData {

    /**
     * 内部卡编号
     */
    private String cardId;

    /**
     * 充值金额
     */
    private String rechargeAmount;

    /**
     * 支付密码
     */
    private String payPwd;



}
