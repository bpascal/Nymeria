package com.codido.nymeria.bean.vo;

import java.io.Serializable;

/**
 * 作者：Junjie.Lai on 2017/2/3 17:22
 * 邮箱：laijj@yzmm365.cn
 */

public class ModuleVo implements Serializable {

    /**
     * 模块类型
     */
    public static String MODULE_TYPE_FUNCTION = "1";
    public static String MODULE_TYPE_URL = "2";

    /**
     * 彩票类型标识str
     */
    public static String MODULE_LOTTERY_TYPE = "MODULE_LOTTERY_TYPE";

    /**
     * 双色球
     */
    public static String MODULE_LOTTERY_TYPE_SSQ = "1";
    public static String MODULE_LOTTERY_NAME_SSQ = "双色球";

    /**
     * 功能id
     */
    private String id;

    /**
     * 功能模块名称
     */
    private String name;

    /**
     * 功能图标url
     */
    private String iconUrl;

    /**
     * 功能模块类型
     */
    private String type;

    /**
     * 功能url
     */
    private String url;

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ModuleVo{" +
                "iconUrl='" + iconUrl + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                '}';
    }


    /**
     * 返回该MODULE是否标志一个内部功能点
     */
    public boolean isFunction() {
        return MODULE_TYPE_FUNCTION.equals(type) && id != null;
    }

    /**
     * 返回该MODULE是否标志一个URL功能点
     */
    public boolean isUrl() {
        return MODULE_TYPE_URL.equals(type) && url != null;
    }

    /**
     * 重写equals方法
     */
    @Override
    public boolean equals(Object o) {

        if (o == null || (!(o instanceof ModuleVo))) {
            return false;
        }

        if (this == o) {
            return true;
        }

        ModuleVo module = (ModuleVo) o;
        if (MODULE_TYPE_FUNCTION.equals(type) && id != null && id.equals(module.id)) {
            return true;
        }
        if (MODULE_TYPE_URL.equals(type) && url != null && url.equals(module.url)) {
            return true;
        }
        return false;
    }
}
