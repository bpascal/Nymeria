package com.codido.nymeria.bean.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 22:44
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode
public class PayoffPlanVo implements Serializable {

    /**
     * 还款计划ID
     */
    private String planId;

    /**
     * 银行卡内部编号
     */
    private String cardId;

    /**
     * 还款月份
     */
    private String payoffMonth;

    /**
     * 还款金额
     */
    private String payoffAmount;

    /**
     * 当前已还金额
     */
    private String payedAmount;

    /**
     * 还款状态
     * 0:未开始1:已开始2:已结束 3：已取消
     */
    private String planSts;

    /**
     * 还款计划详情列表
     */
    private List<PayoffPlanDetailVo> payoffPlanDetailList;
}
