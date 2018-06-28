package com.codido.nymeria.bean.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 20:43
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class CheckUpdateResp extends BaseResp {

    /**
     * 版本更新类型 0：不需要更新
     */
    public static final String VER_TYPE_NO_UPDATE = "0";
    /**
     * 版本更新类型 1，非强制更新
     */
    public static final String VER_TYPE_NOT_MUST_UPDATE = "1";
    /**
     * 版本更新类型 2，强制更新
     */
    public static final String VER_TYPE_MUST_UPDATE = "2";
    /**
     * 版本更新类型 3 微小更新
     */
    public static final String VER_TYPE_IGNORE_UPDATE = "3";

    /**
     * 更新类型
     * 0：不需要更新，1，非强制更新2，强制更新, 3 微小更新
     */
    private String verType;

    /**
     * 更新地址
     */
    private String verUrl;

    /**
     * 版本名称
     */
    private String verName;

    /**
     * 更新描述
     */
    private String verDesc;

    /**
     * 更新时间
     */
    private String verTime;
}
