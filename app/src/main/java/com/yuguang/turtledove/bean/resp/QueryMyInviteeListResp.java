package com.codido.nymeria.bean.resp;

import com.codido.nymeria.bean.vo.InviteeVo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 22:56
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryMyInviteeListResp extends BaseResp {

    /**
     * 直推人数
     */
    private String childrenNum;

    /**
     * 其他推荐人数
     */
    private String otherNum;

    /**
     * 被邀请人列表
     */
    private List<InviteeVo> inviteeList;
}
