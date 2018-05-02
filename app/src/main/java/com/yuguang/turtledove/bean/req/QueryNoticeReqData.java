package com.codido.nymeria.bean.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 21:22
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryNoticeReqData extends BaseReqData {

    /**
     * 公告位置 1:首页
     */
    public static final String BANNER_PLACE_INDEX = "1";
    /**
     * 公告位置 1:首页
     */
    private String noticePlace;
}
