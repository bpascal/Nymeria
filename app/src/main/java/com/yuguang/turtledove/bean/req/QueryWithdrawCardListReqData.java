package com.codido.nymeria.bean.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 1.	提现银行卡查询
 * 描述：查询交易记录列表
 * 请求地址：txn/queryWithdrawCardList.do
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryWithdrawCardListReqData extends BaseReqData {

    /**
     * CASH:现金提现
     * SHARE:收益提现
     */
    private String withdrawType;
}
