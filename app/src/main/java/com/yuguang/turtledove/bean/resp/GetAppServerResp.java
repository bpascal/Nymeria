package com.codido.nymeria.bean.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取服务器地址参数响应对象
 * 作者：Junjie.Lai on 2018/1/6 00:32
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class GetAppServerResp extends BaseResp {

    /**
     * 服务器上下文地址
     */
    private String url;
}
