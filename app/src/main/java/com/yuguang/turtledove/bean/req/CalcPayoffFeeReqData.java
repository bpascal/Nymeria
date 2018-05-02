package com.codido.nymeria.bean.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 22:35
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CalcPayoffFeeReqData extends BaseReqData {

    /**
     * 用户银行卡内部编号
     */
    private String cardId;

    /**
     * 账单日
     */
    private String billDay;

    /**
     * 还款日
     */
    private String payoffDay;

    /**
     * 还款金额
     */
    private String payoffAmount;

    /**
     * 开始月份
     */
    private String startMonth;

    /**
     * 结束月份
     */
    private String endMonth;
}
