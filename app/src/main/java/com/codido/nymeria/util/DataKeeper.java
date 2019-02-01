package com.codido.nymeria.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.alibaba.fastjson.JSON;
import com.codido.nymeria.bean.parser.MsgParser;
import com.codido.nymeria.bean.resp.BaseResp;

/**
 * 登录数据缓存对象
 *
 * @author liu_kf
 */
public class DataKeeper {

    private static final String LOGIN_DATA_SAVE_NAME = "save_data";

    /**
     * 学校信息存储标识
     */
    private static final String PREFERENCES_SHOW_MONEY = "PREFERENCES_SHOW_MONEY";

    /**
     * 保存某个请求的响应
     *
     * @param context
     * @param baseResp
     */
    public static void saveResp(Context context, BaseResp baseResp) {
        if (context == null || baseResp == null) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(LOGIN_DATA_SAVE_NAME, 0);
        Editor editor = pref.edit();
        try {
            String datas = JSON.toJSONString(baseResp);
            editor.putString(baseResp.getKey(), datas);
            editor.commit();
        } catch (Exception e) {

        }
    }

    /**
     * 获取某个key的存储的响应对象
     *
     * @param context
     * @param key
     * @return
     */
    public static BaseResp getResp(Context context, String key) {
        if (context == null || key == null) {
            return null;
        }
        try {
            SharedPreferences pref = context.getSharedPreferences(LOGIN_DATA_SAVE_NAME, 0);
            String data = pref.getString(key, null);
            if (data == null) {
                return null;
            }

            return MsgParser.parser(key, data);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 清除某个key的存储的响应对象
     *
     * @param context
     * @param key
     */
    public static void clearResp(Context context, String key) {
        if (context == null || key == null) {
            return;
        }
        SharedPreferences pref = context.getSharedPreferences(LOGIN_DATA_SAVE_NAME, 0);
        Editor editor = pref.edit();
        try {
            editor.putString(key, null);
            editor.commit();
        } catch (Exception e) {

        }
    }

    /**
     * 保存当前状态下的登录数据
     *
     * @param context
     */
    public static void saveLoginData(Context context) {
        SharedPreferences pref = context.getSharedPreferences(LOGIN_DATA_SAVE_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();

        editor.putString("Global.userinfo", JSON.toJSONString(Global.userInfo));
        editor.putString("Global.sid", Global.sid);
        editor.putString("Global.mobile", Global.mobile);
        editor.commit();
    }


    /**
     * 清除登录数据
     *
     * @param context
     */
    public static void clearLoginData(Context context) {
        SharedPreferences pref = context.getSharedPreferences(LOGIN_DATA_SAVE_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();

        editor.putString("Global.userinfo", null);
        editor.putString("Global.sid", null);
        editor.putString("Global.mobile", null);

        editor.commit();
    }

    /**
     * 从本地恢复登录数据
     *
     * @param context
     * @return
     */
    public static boolean recoverLoginData(Context context) {
        SharedPreferences pref = context.getSharedPreferences(LOGIN_DATA_SAVE_NAME, Context.MODE_APPEND);
//
        String data = pref.getString("Global.userinfo", null);
        if (YUtils.isEmpty(data)) {
            return false;
        }
        try {
            Global.userInfo = JSON.parseObject(data, UserInfoVo.class);
            Global.sid = pref.getString("Global.sid", null);
            Global.mobile = pref.getString("Global.mobile", null);
         } catch (Exception e) {

        }
        return Global.userInfo != null;
    }

    /**
     * 保存资产显示状态
     *
     * @param context
     */
    public static void saveShowMoneySts(Context context, String sts) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_SHOW_MONEY, Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(PREFERENCES_SHOW_MONEY, sts);
        editor.commit();
    }

    /**
     * 获取资产显示状态
     *
     * @param context
     */
    public static String getShowMoneySts(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_SHOW_MONEY, Context.MODE_PRIVATE);
        String data = pref.getString(PREFERENCES_SHOW_MONEY, Global.SHOW_MONEY_STS_SHOW);//默认是显示
        return data;
    }

    /**
     * 从本地恢复显示资产状态
     *
     * @param context
     * @return
     */
    public static boolean recoverShowMoneySts(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_SHOW_MONEY, Context.MODE_PRIVATE);
        String data = pref.getString(PREFERENCES_SHOW_MONEY, null);
        if (YUtils.isEmpty(data)) {
            return false;
        }
        try {
            Global.showMoneySts = data;
        } catch (Exception e) {

        }
        return Global.showMoneySts != null;
    }


    /**
     * 清除资产显示状态数据
     *
     * @param context
     */
    public static void clearShowMoneySts(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_SHOW_MONEY, Context.MODE_PRIVATE);
        Editor editor = pref.edit();

        editor.putString(PREFERENCES_SHOW_MONEY, null);

        editor.commit();
    }

}
