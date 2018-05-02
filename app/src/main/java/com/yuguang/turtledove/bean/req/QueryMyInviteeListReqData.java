package com.codido.nymeria.bean.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 22:54
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryMyInviteeListReqData extends BaseReqData {

    /**
     * 邀请人ID,不填默认是userId
     */
    private String inviterId;

    /**
     * 页码
     */
    private String pageNo;
}
