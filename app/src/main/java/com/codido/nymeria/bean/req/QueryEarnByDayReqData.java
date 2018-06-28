package com.codido.nymeria.bean.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 23:29
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryEarnByDayReqData extends BaseReqData {

    /**
     * 日期 yyyyMMdd
     */
    private String day;
}
