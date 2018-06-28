package com.codido.nymeria.bean.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 22:17
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryBankBybinResp extends BaseResp {

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
     * 归属银行编码
     */
    private String bankCode;
}
