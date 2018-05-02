package com.codido.nymeria.bean.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 22:57
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode
public class InviteeVo implements Serializable {

    /**
     * 被邀请人ID
     */
    private String inviteeId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 直推人数
     */
    private String childrenNum;


    /**
     * 其他推荐人数
     */
    private String otherNum;

    /**
     * 累计收益
     */
    private String totalEarnBalance;

    /**
     * 用户级别
     * 1:1星
     * 2:2星
     * 3:3星
     * 4:4星
     */
    private String level;
}
