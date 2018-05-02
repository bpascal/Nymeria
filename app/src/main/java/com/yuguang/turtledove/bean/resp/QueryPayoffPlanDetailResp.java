package com.codido.nymeria.bean.resp;

import com.codido.nymeria.bean.vo.PayoffPlanVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 22:52
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryPayoffPlanDetailResp extends BaseResp {

    /**
     * 还款计划
     */
    private PayoffPlanVo payoffPlan;
}
