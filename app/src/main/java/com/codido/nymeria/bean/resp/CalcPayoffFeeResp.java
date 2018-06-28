package com.codido.nymeria.bean.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 22:37
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CalcPayoffFeeResp extends BaseResp {

    /**
     * 提示文本
     */
    private String promptText;

    /**
     * 保证金
     */
    private String depositCash;

    /**
     * 手续费
     */
    private String feeCash;

    /**
     * 用户可用现金余额
     */
    private String cashBalance;
}
