package com.codido.nymeria.bean.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/11/7 21:26
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode
public class KfPhoneVo implements Serializable {

    /**
     * 公告编号
     */
    private String name;


    /**
     * 客服姓名
     */
    private String  phone;


}
