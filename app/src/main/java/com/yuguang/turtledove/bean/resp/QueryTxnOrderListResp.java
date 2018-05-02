package com.codido.nymeria.bean.resp;

import com.codido.nymeria.bean.vo.TxnOrderVo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 23:52
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryTxnOrderListResp extends BaseResp {

    /**
     * 最大id
     */
    private String maxId;

    /**
     * 还款计划
     */
    private List<TxnOrderVo> txnOrderList;

}
