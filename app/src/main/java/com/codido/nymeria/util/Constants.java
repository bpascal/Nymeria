package com.codido.nymeria.util;

import java.util.HashMap;

/**
 * Created by liukaifu on 2017/11/9.
 */

public class Constants {



    /**
     * level
     * inviteeList
     * 用户等级
     * 用户级别
     * 1:1星
     * 2:2星
     * 3:3星
     * 4:4星
     */
    public static final HashMap<String, String> USER_LEVEL = new HashMap<>();

    static {
        USER_LEVEL.put("0", "普通");
        USER_LEVEL.put("1", "1星");
        USER_LEVEL.put("2", "2星");
        USER_LEVEL.put("3", "3星");
        USER_LEVEL.put("4", "4星");
        USER_LEVEL.put("5", "5星");
    }


    /**

     */
    public static final HashMap<String, String> CRADS_TYPES = new HashMap<>();

    static {
        CRADS_TYPES.put("0", "借记卡");
        CRADS_TYPES.put("1", "信用卡");
    }


    /**

     */
    public static final HashMap<String, String> CRADS_STATES = new HashMap<>();

    static {
        CRADS_STATES.put("0", "未设置");
        CRADS_STATES.put("1", "已设置");
        CRADS_STATES.put("2", "还款中");
    }


    public static final String[] ORDER_TYPES_KEYS = new String[]{
            null,
            "recharge",
            "withdraw",
            "recharge",
            "payoff",
            "withhold",
            "other",
    };

    public static final HashMap<String, String> ORDER_TYPES = new HashMap<>();

    static {
        ORDER_TYPES.put("recharge", "充值");
        ORDER_TYPES.put("withdraw", "提现");
        ORDER_TYPES.put("payoff", "还款");
        ORDER_TYPES.put("withhold", "扣款");
        ORDER_TYPES.put("other", "其他");
    }

}
