package com.codido.nymeria.bean.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户信息对象
 */
@Data
@EqualsAndHashCode
public class UserInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 总资产
     */
    private String totalBalance;

    /**
     * 可用现金余额
     */
    private String ableCashBal;

    /**
     * 可提收益
     */
    private String ableEarnBalance;

    /**
     * 累计收益
     */
    private String accuEarnBalance;

    /**
     * 用户状态
     * 0:未交邀请费
     * 1:正常
     * 2:黑名单
     */
    private String userSts;

    /**
     * 用户级别
     * 0:省代
     * 1:市代
     * 2:县代
     * 3:普通用户
     */
    private String level;

    /**
     * 用户实名标识
     * 0:未实名认证
     * 1:已实名认证
     */
    private String realFlag;

    /**
     * 身份证号码
     */
    private String idNo;

    /**
     * 用户实名
     */
    private String userName;
}
