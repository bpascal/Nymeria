package com.codido.nymeria.bean.parser;

import com.alibaba.fastjson.JSON;
import com.codido.nymeria.bean.resp.AddPayOffPlanResp;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.CalcPayoffFeeResp;
import com.codido.nymeria.bean.resp.CheckUpdateResp;
import com.codido.nymeria.bean.resp.GetAppServerResp;
import com.codido.nymeria.bean.resp.GetShareParamResp;
import com.codido.nymeria.bean.resp.GetSmsCodeResp;
import com.codido.nymeria.bean.resp.LoginResp;
import com.codido.nymeria.bean.resp.QueryBankBybinResp;
import com.codido.nymeria.bean.resp.QueryBannerListResp;
import com.codido.nymeria.bean.resp.QueryEarnByDayResp;
import com.codido.nymeria.bean.resp.QueryHomeModuleListResp;
import com.codido.nymeria.bean.resp.QueryKfInfoResp;
import com.codido.nymeria.bean.resp.QueryMyBalanceResp;
import com.codido.nymeria.bean.resp.QueryMyCardListResp;
import com.codido.nymeria.bean.resp.QueryMyEarnResp;
import com.codido.nymeria.bean.resp.QueryMyInviteeListResp;
import com.codido.nymeria.bean.resp.QueryMyUserInfoResp;
import com.codido.nymeria.bean.resp.QueryNoticeResp;
import com.codido.nymeria.bean.resp.QueryParameterResp;
import com.codido.nymeria.bean.resp.QueryPayoffPlanDetailResp;
import com.codido.nymeria.bean.resp.QueryPayoffPlanListResp;
import com.codido.nymeria.bean.resp.QueryTxnOrderListResp;
import com.codido.nymeria.bean.resp.QueryWithdrawCardListResp;
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
        } else if (key.equals(Global.key_getSmsCode)) {
            //获取短信验证码
            return JSON.parseObject(resp, GetSmsCodeResp.class);
        } else if (key.equals(Global.key_checkUpdate)) {
            //检查更新
            return JSON.parseObject(resp, CheckUpdateResp.class);
        } else if (key.equals(Global.key_queryBannerList)) {
            //查询banner
            return JSON.parseObject(resp, QueryBannerListResp.class);
        } else if (key.equals(Global.key_queryNotice)) {
            //查询公告
            return JSON.parseObject(resp, QueryNoticeResp.class);
        } else if (key.equals(Global.key_queryHomeModuleList)) {
            //查询首页模块
            return JSON.parseObject(resp, QueryHomeModuleListResp.class);
        } else if (key.equals(Global.key_queryMyCardList)) {
            //查询用户银行卡列表
            return JSON.parseObject(resp, QueryMyCardListResp.class);
        } else if (key.equals(Global.key_queryBankBybin)) {
            //查询卡bin
            return JSON.parseObject(resp, QueryBankBybinResp.class);
        } else if (key.equals(Global.key_sendSmsCode)) {
            //给预留号码下发短信验证码
            return JSON.parseObject(resp, SendSmsCodeResp.class);
        } else if (key.equals(Global.key_calcPayoffFee)) {
            //还款费率计算
            return JSON.parseObject(resp, CalcPayoffFeeResp.class);
        } else if (key.equals(Global.key_queryPayoffPlanList)) {
            //查询还款计划列表
            return JSON.parseObject(resp, QueryPayoffPlanListResp.class);
        } else if (key.equals(Global.key_queryPayoffPlanDetail)) {
            //查询还款计划详情
            return JSON.parseObject(resp, QueryPayoffPlanDetailResp.class);
        } else if (key.equals(Global.key_queryMyInviteeList)) {
            //查询我的邀请
            return JSON.parseObject(resp, QueryMyInviteeListResp.class);
        } else if (key.equals(Global.key_queryMyUserInfo)) {
            //查询我的用户信息
            return JSON.parseObject(resp, QueryMyUserInfoResp.class);
        } else if (key.equals(Global.key_queryMyBalance)) {
            //查询我的资产结构
            return JSON.parseObject(resp, QueryMyBalanceResp.class);
        } else if (key.equals(Global.key_queryMyEarn)) {
            //查询我的收益
            return JSON.parseObject(resp, QueryMyEarnResp.class);
        } else if (key.equals(Global.key_queryEarnByDay)) {
            //按日期查询收益
            return JSON.parseObject(resp, QueryEarnByDayResp.class);
        } else if (key.equals(Global.key_queryTxnOrderList)) {
            //查询账单交易记录列表
            return JSON.parseObject(resp, QueryTxnOrderListResp.class);
        } else if (key.equals(Global.key_queryWithdrawCardList)) {
            //提现银行卡查询
            return JSON.parseObject(resp, QueryWithdrawCardListResp.class);
        } else if (key.equals(Global.key_queryKfInfo)) {
            //查询客服信息
            return JSON.parseObject(resp, QueryKfInfoResp.class);
        } else if (key.equals(Global.key_queryParameter)) {
            return JSON.parseObject(resp, QueryParameterResp.class);
        } else if (key.equals(Global.key_addPayoffPlan)) {
            return JSON.parseObject(resp, AddPayOffPlanResp.class);
        } else if (key.equals(Global.key_getBjjtAppServer)) {
            //获取服务器地址
            return JSON.parseObject(resp, GetAppServerResp.class);
        } else if (key.equals(Global.key_getShareParam)) {
            //微信分享参数
            return JSON.parseObject(resp, GetShareParamResp.class);
        } else {
            return JSON.parseObject(resp, BaseResp.class);
        }
    }
}
