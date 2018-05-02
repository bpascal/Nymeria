package com.codido.nymeria.bean.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/12 17:25
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RemoveMyCardReqData extends BaseReqData {

    /**
     * 卡ID
     */
    private String cardId;
}
