package com.codido.nymeria.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 应用程序基础配置,该类中只记录应用程序通用配置信息
 *
 * @author liu_kf
 */
public class CommonConfig {
    /** 加密标识 */
    public static boolean encodeFlag = false;
    /** 是否初始化 */
    private static boolean isInited = false;
    /** 正式发布版本的签名证书MD5值 */
    private static String ONLINE_SIGN_MD5;
    /** 包名 */
    private static String PACKAGE_NAME;
    /** 在SD中建立的该应用程序的缓存目录,默认为 "." + 应用程序包名 */
    public static String CACHE_PATH_NAME = null;
    /** MAC地址 */
    private static String MAC_ADDRESS = null;
    /** 应用程序版本号，从配置文件读取 */
    private static String VERSION = null;
    /** 应用程序运行环境的IMEI号，初始化时，从应用环境读取 */
    private static String IMEI = null;
    /** 应用程序平台标志，该值为固定值 */
    public static final String PLAT = "ANDROID";
    /** 正式版本UMENG_KEY值，,该字段可以为空 */
    private static String ONLINE_UMENG_KEY;
    /** 正式版本JPUSH_KEY值 */
    private static String ONLINE_JPUSH_KEY;
    /** 推送ID */
    public static String PUSH_ID;
    /** 正式包名 */
    private static String ONLINE_PACKAGE_NAME;
    /** 应用程序渠道(友盟)标志，从配置文件读取 */
    private static String CHANNEL = null;
    /** 服务器地址 */
    private static String BASE_URL_DEBUG, BASE_URL_ONLINE;
    /** 客户端授权编号 */
    private static String CLIENT_ID;
    /** 加密编号 */
    private static String REQ_KEY;
    /** 分享地址 */
    private static String SHARE_URL;
    /** SESSION URL 用来标示改地址返回的SESSION 需要保存 */
    private static Set<String> SESSION_URL;
    /** debug模式标示 */
    private static boolean isdeBug = true;


    /**
     * 初始化
     *
     * @param context 调用此方法请确认在AndroidManifest.xml写有如下数据
     *                <!--正式版的包名-->
     *                <meta-data android:name="CLOUNDFIN_PACKAGENAME" android:value="XXXXX"/>
     *                <!--签名MD码-->
     *                <meta-data android:name="CLOUNDFIN_SIGN_MD5" android:value="XXXXX"/>
     *                <!--正式版的极光KEY-->
     *                <meta-data android:name="CLOUNDFIN_ONLINE_JPUSH_KEY" android:value="XXXXX"/>
     *                <!--正式版友盟KEY-->
     *                <meta-data android:name="CLOUNDFIN_ONLINE_UMENG_KEY" android:value="XXXXX"/>
     *                <!--测试地址-->
     *                <meta-data android:name="CLOUNDFIN_BASE_URL_TEST" android:value="XXXXX"/>
     *                <!--正式地址-->
     *                <meta-data android:name="CLOUNDFIN_BASE_URL_ONLINE" android:value="XXXXX"/>
     *                <!--客户端授权编号（可选）-->
     *                <meta-data android:name="CLOUNDFIN_CLIEDTID" android:value="XXXXX"/>
     *                <!--加密编号（可选）-->
     *                <meta-data android:name="CLOUNDFIN_REQKEY" android:value="XXXXX"/>
     *                <!--分享地址产品连接（可选）-->
     *                <meta-data android:name="SHARE_URL" android:value="XXXXX"/>
     */
    public static void init(@NonNull Context context) {
        //获取系统信息
        ApplicationInfo info;
        try {
            info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle metaData = info.metaData;
            //获取包名
            CommonConfig.ONLINE_PACKAGE_NAME = metaData.getString("CLOUNDFIN_PACKAGENAME");
            //获取证书MD值
            CommonConfig.ONLINE_SIGN_MD5 = metaData.getString("CLOUNDFIN_SIGN_MD5");
            //获取UMENG的KEY
            CommonConfig.ONLINE_UMENG_KEY = metaData.getString("CLOUNDFIN_ONLINE_UMENG_KEY");
            //获取极光的KEY
            CommonConfig.ONLINE_JPUSH_KEY = metaData.getString("CLOUNDFIN_ONLINE_JPUSH_KEY");

            CommonConfig.getIMEI(context);
            CommonConfig.getAppVersion(context);
            CommonConfig.getUmengChannel(context);
            CommonConfig.getMacAddress(context);


            //获取服务器地址
            BASE_URL_DEBUG = metaData.getString("CLOUNDFIN_BASE_URL_TEST");
            BASE_URL_ONLINE = metaData.getString("CLOUNDFIN_BASE_URL_ONLINE");

            //获取分享地址
            SHARE_URL = metaData.getString("SHARE_URL");

            //获取客户端编号
            CLIENT_ID = metaData.getString("CLOUNDFIN_CLIEDTID");
            CLIENT_ID = TextUtils.isEmpty(CLIENT_ID) ? "AA89D6D64FB94F79B4A8060165A41A51" : CLIENT_ID;
            //设置加密编号
            REQ_KEY = metaData.getString("CLOUNDFIN_REQKEY");
            REQ_KEY = TextUtils.isEmpty(REQ_KEY) ? "J58l2qudG8uCeae4JcWBl2en9zv7xvFa" : REQ_KEY;

            isInited = true;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
            LogUtils.debug(context, "getSource Failded!");
        }
    }


    /***
     * umeng 测试设备提交数据
     * <p/>
     * 将答应的信息添加到umeng的测试设置中
     * http://www.umeng.com/test_devices/new
     *
     * @param context
     *
     * @return
     */
    public static void UmengDeviceInfo(@NonNull Context context) {
        if (!isdeBug) return;
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }

            if (TextUtils.isEmpty(device_id)) {
                device_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            LogUtils.debug("Umeng test devices", json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 是否是同一个路径下
     * <p/>
     * 这里认可的地址：
     * 正式：juyouqian.cn，juyouqian.com
     * 测试：115.29.139.173
     *
     * @param url
     */
    public static boolean isSameHost(String url) {
        try {
            //末尾随机标示符
            int random = (int) (Math.random() * 100);
            String urlHost = new URL(url).getHost().toLowerCase() + random;
            // 创建 Pattern 对象
            String patternStr = String.format("(juyouqian.com|juyouqian.cn|115.29.139.173)%d", random);
            Pattern pattern = Pattern.compile(patternStr);
            // 现在创建 matcher 对象
            Matcher matcher = pattern.matcher(urlHost);
            return matcher.find();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isEncodeFlag() {
        return encodeFlag;
    }

    public static void encodeFlag(boolean encodeFlag) {
        CommonConfig.encodeFlag = encodeFlag;
    }

    /** 设置debug模式 */
    public static void setDebug(Boolean deBug) {
        isdeBug = deBug;
    }

    /**
     * 根据规则返回该版本中的配置参数是否是一个生产版本的配置参数
     *
     * @param context
     *
     * @return
     */
    public static boolean isOnlineVer(Context context) {

        if (!isInited) {
            return false;
        }
        if (isdeBug) {
            return false;
        }

        return false;
    }

    /**
     * 配置参数是否初始化成功
     *
     * @return
     */
    public static boolean isInitSuccess() {

        return isInited && CommonConfig.CACHE_PATH_NAME != null;
    }


    /**
     * 获取mac地址
     */
    public static String getMacAddress(Context context) {
        if (MAC_ADDRESS != null) {
            return MAC_ADDRESS;
        }
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (info == null || info.getMacAddress() == null) {
            return null;
        }

        MAC_ADDRESS = new String(info.getMacAddress().getBytes());

        return MAC_ADDRESS;
    }

    /**
     * 获取版本号，并将其设置为AppVersion
     *
     * @param context
     *
     * @return
     */
    public static String getAppVersion(Context context) {
        if (VERSION != null) {
            return VERSION;
        }
        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            LogUtils.debug(context, "getAppVersion Failded!");
            return null;
        }
        VERSION = info.versionName;
        return VERSION;
    }

    /**
     * 获取包名
     *
     * @param context
     *
     * @return
     */
    public static String getAppPackageName(Context context) {
        if (PACKAGE_NAME != null) {
            return PACKAGE_NAME;
        }
        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            LogUtils.debug(context, "getAppVersion Failded!");
            return null;
        }
        PACKAGE_NAME = info.packageName;
        return PACKAGE_NAME;
    }

    /**
     * 从应用环境中获取IMEI号码
     *
     * @param context
     *
     * @return
     */
    public static String getIMEI(Context context) {
        if (IMEI != null) {
            return IMEI;
        }
//		try {
//			TelephonyManager tm = (TelephonyManager) context
//					.getSystemService(Context.TELEPHONY_SERVICE);
//			return IMEI = tm.getDeviceId();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

        try {
            TelephonyManager mTelephony = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (!TextUtils.isEmpty(mTelephony.getDeviceId())) {
                IMEI = mTelephony.getDeviceId();
            } else {
                IMEI = Secure.getString(
                        context.getContentResolver(), Secure.ANDROID_ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return IMEI;
    }

    /**
     * 从配置中读取友盟的Key
     *
     * @param context
     *
     * @return
     */
    public static String getUMengKey(Context context) {
        String code = getMetaData(context, "UMENG_APPKEY");
        if (code != null) {
            return code;
        }
        return "null";
    }

    /**
     * 从配置中读取推送Key
     *
     * @param context
     *
     * @return
     */
    public static String getJPushCode(Context context) {
        String code = getMetaData(context, "JPUSH_APPKEY");
        if (code != null) {
            return code;
        }
        return "null";
    }

    /**
     * 从配置中读取友盟的渠道信息
     *
     * @param context
     *
     * @return
     */
    public static String getUmengChannel(Context context) {
        if (CHANNEL != null) {
            return CHANNEL;
        }
        PackageManager manager = context.getPackageManager();
        ApplicationInfo info;
        try {
            info = manager.getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            LogUtils.debug(context, "getSource Failded!");
            return null;
        }
        return CHANNEL = info.metaData.getString("UMENG_CHANNEL");

    }

    private static String getMetaData(Context context, String key) {
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
            //
        }
        return null;
    }

    /**
     * 检测应用程序是否经过混淆
     *
     * @return boolean
     */
    public static boolean isMutil() {

        boolean isM;
        try {
            Class<?> cls = Class.forName("com.cloudfin.common.utils.CommonConfig");
            isM = cls == null;
            if (cls == null) {
                isM = true;
            } else {
                isM = !cls.getName().endsWith("CommonConfig");
            }

            return isM;

        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 判断是不是SessionUrl
     *
     * @param url
     *
     * @return
     */
    public static boolean isSessionUrl(String url) {
        if (SESSION_URL == null) {
            return true;
        } else {
            return SESSION_URL.contains(url);
        }
    }


    public static String getBASE_URL() {
        return isdeBug ? BASE_URL_DEBUG : BASE_URL_ONLINE;
    }

    public static String getCLIENT_ID() {
        return CLIENT_ID;
    }

    public static String getREQ_KEY() {
        return REQ_KEY;
    }

    public static String getONLINE_SIGN_MD5() {
        return ONLINE_SIGN_MD5;
    }

    public static void setONLINE_SIGN_MD5(String ONLINE_SIGN_MD5) {
        CommonConfig.ONLINE_SIGN_MD5 = ONLINE_SIGN_MD5;
    }

    public static String getPACKAGE_NAME() {
        return PACKAGE_NAME;
    }

    public static void setPACKAGE_NAME(String PACKAGE_NAME) {
        CommonConfig.PACKAGE_NAME = PACKAGE_NAME;
    }

    public static String getMAC_ADDRESS() {
        return MAC_ADDRESS;
    }

    public static void setMAC_ADDRESS(String MAC_ADDRESS) {
        CommonConfig.MAC_ADDRESS = MAC_ADDRESS;
    }

    public static String getVERSION() {
        return VERSION;
    }

    public static void setVERSION(String VERSION) {
        CommonConfig.VERSION = VERSION;
    }

    public static String getIMEI() {
        return IMEI;
    }

    public static void setIMEI(String IMEI) {
        CommonConfig.IMEI = IMEI;
    }

    public static String getONLINE_UMENG_KEY() {
        return ONLINE_UMENG_KEY;
    }

    public static void setONLINE_UMENG_KEY(String ONLINE_UMENG_KEY) {
        CommonConfig.ONLINE_UMENG_KEY = ONLINE_UMENG_KEY;
    }

    public static String getONLINE_JPUSH_KEY() {
        return ONLINE_JPUSH_KEY;
    }

    public static void setONLINE_JPUSH_KEY(String ONLINE_JPUSH_KEY) {
        CommonConfig.ONLINE_JPUSH_KEY = ONLINE_JPUSH_KEY;
    }

    public static String getONLINE_PACKAGE_NAME() {
        return ONLINE_PACKAGE_NAME;
    }

    public static void setONLINE_PACKAGE_NAME(String ONLINE_PACKAGE_NAME) {
        CommonConfig.ONLINE_PACKAGE_NAME = ONLINE_PACKAGE_NAME;
    }

    public static String getCHANNEL() {
        return CHANNEL;
    }

    public static void setCHANNEL(String CHANNEL) {
        CommonConfig.CHANNEL = CHANNEL;
    }

    public static void setCLIENT_ID(String CLIENT_ID) {
        CommonConfig.CLIENT_ID = CLIENT_ID;
    }

    public static void setREQ_KEY(String REQ_KEY) {
        CommonConfig.REQ_KEY = REQ_KEY;
    }

    public static String getShareUrl() {
        return SHARE_URL;
    }

    public static void setShareUrl(String shareUrl) {
        SHARE_URL = shareUrl;
    }

    public static Set<String> getSessionUrl() {
        return SESSION_URL;
    }

    public static void addSessionUrl(String sessionUrl) {
        if (SESSION_URL == null) {
            SESSION_URL = new HashSet<String>();
        }
        SESSION_URL.add(sessionUrl);
    }

    public static boolean isdeBug() {
        return isdeBug;
    }
}
