package com.codido.nymeria.bean.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取短信验证码接口响应对象
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class GetSmsCodeResp extends BaseResp {

	private static final long serialVersionUID = 1L;

    /**
     * 请求流水号
     */
	private String serialCode;
}
