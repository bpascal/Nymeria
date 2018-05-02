package com.codido.nymeria.bean.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 23:46
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryTxnOrderListReqData extends BaseReqData {

    /**
     * 内部卡编号
     */
    private String cardId;

    /**
     * 交易类型
     */
    private String orderType;

    /**
     * 最大ID
     */
    private String maxId;
}
