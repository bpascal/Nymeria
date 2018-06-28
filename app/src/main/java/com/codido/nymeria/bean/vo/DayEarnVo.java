package com.codido.nymeria.bean.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 23:13
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode
public class DayEarnVo implements Serializable {

    /**
     * 日期 yyyyMMdd
     */
    private String day;

    /**
     * 日收益总额
     */
    private String dayTotalEarn;

    /**
     * 进度
     */
    private float progressNum;
}
