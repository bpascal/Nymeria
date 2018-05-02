package com.codido.nymeria.bean.resp;

import com.codido.nymeria.bean.vo.ModuleVo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：Junjie.Lai on 2017/2/3 17:21
 * 邮箱：laijj@yzmm365.cn
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class QueryHomeModuleListResp extends BaseResp {

    /**
     * 模块列表
     */
    private List<ModuleVo> moduleList;

    public List<ModuleVo> getModuleList() {
        return moduleList;
    }

    public void setModuleList(List<ModuleVo> moduleList) {
        this.moduleList = moduleList;
    }

    @Override
    public String toString() {
        return "QueryHomeModuleListResp{" +
                "moduleList=" + moduleList +
                '}';
    }
}
