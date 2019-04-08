package com.codido.nymeria.bean.vo;

import lombok.Data;

/**
 * banner广告位对象
 *
 * @author liu_kf
 */
@Data
public class BannerVo {

    /**
     * 主键id
     */
    private String bannerId;

    /**
     * banner图片
     */
    private String bannerPic;

    /**
     * banner类型 0:URL
     */
    private String bannerType;

    /**
     * 标题名称
     */
    private String bannerTitle;

    /**
     * 跳转url
     */
    private String bannerUrl;
}
