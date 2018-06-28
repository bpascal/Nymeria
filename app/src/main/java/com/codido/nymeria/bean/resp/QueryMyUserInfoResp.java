package com.codido.nymeria.bean.resp;

import com.codido.nymeria.bean.vo.UserInfoVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 23:04
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryMyUserInfoResp extends BaseResp {

    /**
     * 用户对象
     */
    private UserInfoVo userInfo;
}
