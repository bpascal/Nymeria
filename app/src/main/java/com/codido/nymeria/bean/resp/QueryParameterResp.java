package com.codido.nymeria.bean.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 22:37
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryParameterResp extends BaseResp {

    /**
     * 保证金比例
     */
    private String depositPer;
    /**
     * 还款手续费费率
     */
    private String payoffFeePer;

    /**
     * 保证金模式:
     * 1:预收保证金
     * 0:预留最小还款额度
     */
    private String depositMode;

    /**
     * 分享链接
     */
    private String shareUrl;

    /**
     * 分享二维码图片地址
     */
    private String shareQrcodeUrl;
}
