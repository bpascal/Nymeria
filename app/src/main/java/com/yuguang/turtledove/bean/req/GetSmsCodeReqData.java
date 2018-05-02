package com.codido.nymeria.bean.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取短信验证码请求对象
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class GetSmsCodeReqData extends BaseReqData {

    private static final long serialVersionUID = 1L;

    /**
     * 获取短信验证码用于登录或者注册
     */
    public static final String TYPE_FOR_REGISTER_OR_LOGINS = "1";

    /**
     * 获取短信验证码用于重置密码
     */
    public static final String TYPE_RESET_PASSWORD = "2";

    /**
     * 短信验证码类型
     * 0:用于修改用户密码
     * 1:注册
     * 2:登录
     */
    private String smsType;

    /**
     * 手机号码
     */
    private String mobile;
}
