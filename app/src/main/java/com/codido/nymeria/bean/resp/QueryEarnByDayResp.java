package com.codido.nymeria.bean.resp;

import com.codido.nymeria.bean.vo.EarnVo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 23:30
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryEarnByDayResp extends BaseResp {

    /**
     * 收益列表
     */
    private List<EarnVo> earnList;
}
