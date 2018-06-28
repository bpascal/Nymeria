package com.codido.nymeria.bean.resp;

import com.codido.nymeria.bean.vo.UserInfoVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登录响应对象
 *
 * @author liu_kf
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LoginResp extends BaseResp {

    private static final long serialVersionUID = 1L;
    /**
     * sessionId
     */
    private String sid;

    /**
     * 主账户ID
     */
    private UserInfoVo userInfo;

}