package com.codido.nymeria.bean.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 23:32
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode
public class EarnVo implements Serializable {

    /**
     * 收益金额
     */
    private String earnAmount;

    /**
     * 来源号码
     */
    private String sourceMobile;

    /**
     * 来源姓名
     */
    private String sourceUserName;

    /**
     * 收益名称
     */
    private String shareName;

    /**
     * 分润时间
     */
    private long shareTime;
}
