package com.codido.nymeria.bean.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 22:23
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AddMyCardReqData extends BaseReqData {

    /**
     * 银行卡号
     */
    private String cardNo;

    /**
     * 用户真实姓名
     */
    private String userName;

    /**
     * 用户身份证号
     */
    private String idNo;

    /**
     * 银行预留手机号码
     */
    private String mobile;

    /**
     * 短信请求流水号
     */
    private String serialCode;

    /**
     * 短信验证码
     */
    private String smsCode;

    /**
     * 有效期MMYY
     * 信用卡必须
     */
    private String expireDate;

    /**
     * 信用卡必须
     */
    private String cvv2;
}
