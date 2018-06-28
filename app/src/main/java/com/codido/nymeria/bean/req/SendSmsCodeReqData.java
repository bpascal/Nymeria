package com.codido.nymeria.bean.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 22:20
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SendSmsCodeReqData extends BaseReqData {

    /**
     * 银行卡号
     */
    private String cardNo;

    /**
     * 银行预留手机号码
     */
    private String mobile;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 身份证号
     */
    private String idNo;
}
