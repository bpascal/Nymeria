package com.codido.nymeria.bean.resp;

import com.codido.nymeria.bean.vo.CardVo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 21:39
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryWithdrawCardListResp extends BaseResp {


    /**
     * 可提现余额
     */
    private String ableBal;

    /**
     * 提示文本
     */
    private String promptText;

    /**
     * 银行卡列表
     */
    private List<CardVo> cardList;
}
