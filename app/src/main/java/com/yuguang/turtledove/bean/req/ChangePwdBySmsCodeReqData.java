package com.codido.nymeria.bean.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 21:34
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChangePwdBySmsCodeReqData extends BaseReqData {

    /**
     * 请求流水号
     */
    private String serialCode;

    /**
     * 短信验证码
     */
    private String smsCode;

    /**
     * 用户设置的新密码
     * (MD5加密)
     */
    private String newPwd;

    /**
     * 密码类型
     * “LOGIN”:登录密码
     * “PAY”:支付密码
     */
    private String pwdType;
    /**
     *
     */
    private String mobile;
}
