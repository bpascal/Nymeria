package com.codido.nymeria.bean.vo;

import java.io.Serializable;

/**
 * 操作项
 * 作者：Junjie.Lai on 2017/1/18 11:57
 * 邮箱：laijj@yzmm365.cn
 */
public class Operate implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 操作方式key 各详情接口回传
     */
    private String operateKey;

    /**
     * 操作方式名
     */
    private String operateName;

    /**
     * 操作对象ID
     */
    private String objId;

    public String getOperateKey() {
        return operateKey;
    }

    public String getOperateName() {
        return operateName;
    }

    public String getObjId() {
        return objId;
    }

    public void setOperateKey(String operateKey) {
        this.operateKey = operateKey;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    @Override
    public String toString() {
        return "Operate{" +
                "objId='" + objId + '\'' +
                ", operateKey='" + operateKey + '\'' +
                ", operateName='" + operateName + '\'' +
                '}';
    }
}
