package com.codido.nymeria.bean.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 22:49
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode
public class PayoffPlanDetailVo implements Serializable {


    /**
     * 还款日期
     */
    private String payoffDate;

    /**
     * 预计还款日期
     */
    private String planPayoffDate;

    /**
     * 还款金额
     */
    private String payoffAmount;

    /**
     * 本次还款状态
     * 0:未还1:还款中 2:已还
     */
    private String status;
}
