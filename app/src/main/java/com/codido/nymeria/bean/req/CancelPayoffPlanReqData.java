package com.codido.nymeria.bean.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/11 23:15
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CancelPayoffPlanReqData extends BaseReqData {

    /**
     * 计划ID
     */
    private String planId;
}
