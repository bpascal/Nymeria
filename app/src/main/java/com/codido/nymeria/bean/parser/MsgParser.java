package com.codido.nymeria.bean.parser;

import com.alibaba.fastjson.JSON;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.CheckUpdateResp;
import com.codido.nymeria.bean.resp.LoginResp;
import com.codido.nymeria.bean.resp.QueryBannerListResp;
import com.codido.nymeria.bean.resp.SendSmsCodeResp;
import com.codido.nymeria.util.Global;


/**
 * 接口请求解析器
 */
public class MsgParser {

    /**
     * 解析方法
     *
     * @param key
     * @param resp
     * @return
     */
    public static BaseResp parser(String key, String resp) {
        if (key == null || resp == null) {
            return null;
        }
        if (key.equals(Global.key_login)) {
            //登录
            return JSON.parseObject(resp, LoginResp.class);
        } else if (key.equals(Global.key_checkUpdate)) {
            //检查更新
            return JSON.parseObject(resp, CheckUpdateResp.class);
        } else if (key.equals(Global.key_queryBannerList)) {
            //查询banner
            return JSON.parseObject(resp, QueryBannerListResp.class);
        } else if (key.equals(Global.key_sendSmsCode)) {
            //给预留号码下发短信验证码
            return JSON.parseObject(resp, SendSmsCodeResp.class);
        } else {
            return JSON.parseObject(resp, BaseResp.class);
        }
    }
}
