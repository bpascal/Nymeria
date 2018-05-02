package com.codido.nymeria.bean.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 作者：Junjie.Lai on 2017/11/7 21:26
 * 邮箱：laijj@yzmm365.cn
 */
@Data
public class NoticeVo implements Serializable {

    /**
     * 公告编号
     */
    private String noticeId;

    /**
     * 公告类型 0:URL类型
     */
    private String noticeType;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 跳转url
     */
    private String noticeUrl;
}
