package com.codido.nymeria.bean.resp;

import com.codido.nymeria.bean.vo.PayoffPlanVo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 22:43
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryPayoffPlanListResp extends BaseResp {

    /**
     * 还款计划列表
     */
    private List<PayoffPlanVo> payoffPlanList;
}
