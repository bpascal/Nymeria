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
public class QueryMyCardListResp extends BaseResp {

    /**
     * 银行卡列表
     */
    private List<CardVo> cardList;
}
