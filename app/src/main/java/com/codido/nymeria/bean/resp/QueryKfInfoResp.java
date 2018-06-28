package com.codido.nymeria.bean.resp;

import com.codido.nymeria.bean.vo.KfPhoneVo;

import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 1.	查询客服信息
 * 描述：查询客服信息
 * 请求地址：pub/queryKfInfo.do
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryKfInfoResp extends BaseResp {

    private ArrayList<KfPhoneVo> kfPhoneList;
    private String kfWeixin;
    private String kfEmail;
    private String serviceTime;

}
