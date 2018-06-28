package com.codido.nymeria.bean.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 23:11
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode
public class MonthEarnVo implements Serializable {

    /**
     * 月份yyyyMM
     */
    private String month;

    /**
     * 月份总收益
     */
    private String monthTotalEarn;

    /**
     * 日收益总额
     */
    private List<DayEarnVo> dayEarnList;
}
