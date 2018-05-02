package com.codido.nymeria.bean.resp;

import com.codido.nymeria.bean.vo.NoticeVo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 21:23
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryNoticeResp extends BaseResp {

    /**
     * 公告列表
     */
    private List<NoticeVo> noticeList;
}
