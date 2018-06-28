package com.codido.nymeria.bean.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 22:21
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SendSmsCodeResp extends BaseResp {

    /**
     * 请求流水号
     */
    private String serialCode;
}
