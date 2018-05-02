package com.codido.nymeria.bean.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取微信参数响应对象
 * 作者：Junjie.Lai on 2018/1/10 22:30
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetShareParamResp extends BaseResp {

    /**
     * 分享链接
     */
    private String shareUrl;

    /**
     * 分享二维码图片地址
     */
    private String shareImageUrl;

    /**
     * 分享标题
     */
    private String shareTitle;

    /**
     * 分享内容
     */
    private String shareContent;

}
