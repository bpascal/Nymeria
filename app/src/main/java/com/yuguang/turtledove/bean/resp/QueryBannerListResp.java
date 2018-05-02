package com.codido.nymeria.bean.resp;


import com.codido.nymeria.bean.vo.BannerVo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 广告查询对象响应体
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class QueryBannerListResp extends BaseResp {
    private static final long serialVersionUID = 1L;

    /**
     * 广告列表
     */
    private List<BannerVo> bannerList;
}
