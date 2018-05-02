package com.codido.nymeria.bean.resp;

import com.codido.nymeria.bean.vo.MonthEarnVo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 23:15
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryMyEarnResp extends BaseResp {

    /**
     * 累计收益
     */
    private String accuEarnBalance;

    /**
     * 冻结收益
     */
    private String holdEarnBal;

    /**
     * 可用收益
     */
    private String ableEarnBal;

    /**
     * 已提现收益
     */
    private String withdrawedEarnBal;

    /**
     * 积分
     */
    private String pointBal;

    /**
     * 月收益列表
     */
    private List<MonthEarnVo> monthEarnList;
}
