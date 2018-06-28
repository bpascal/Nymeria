package com.codido.nymeria.bean.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 20:43
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class AddPayOffPlanResp extends BaseResp {

    private String needRechargeAmount;
}
