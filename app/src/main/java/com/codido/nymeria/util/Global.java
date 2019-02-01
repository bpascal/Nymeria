package com.codido.nymeria.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 应用全局控制对象，包括全局参数，和一些全局的方法
 */
public class Global {

    /**
     * 资产显示状态
     */
    public static String showMoneySts;

    /**
     * 用户登录信息 -用户主账户头像地址
     */
    public static String accHeadImg;

    /**
     * 用户登录信息 -用户主账户ID
     */
    public static String accId;

    /**
     * 用户登录信息 -用户主账户昵称
     */
    public static String accNickName;

    /**
     * 登录超时
     */
    public static final String ACTION_RECEIVE_GET_SCORE = "com.codido.nymeria.ACTION_RECEIVE_GET_SCORE";

    /**
     * 登录超时
     */
    public static final String ACTION_RECEIVE_LOGIN_OUT = "com.codido.nymeria.ACTION_RECEIVE_LOGIN_OUT";

    /**
     * Banner的图片的宽高比例
     */
    public static final double ADV_SCALE = 2.78;

    /**
     * 商城页面的宽高比
     */
    public static final double OFFER_SCALE = 1.83;
    /**
     * 高德地图KEY
     */
    public static String AmapKey;


    /**
     * 用户登录信息 -客户端的Cookie信息
     */
    public static String CookieStr = null;
    /**
     * 开关标志-是否是调试版本
     */
    public static boolean DEBUG = true;

    /**
     * 设备ID，为手机的IMEI
     */
    public static String deviceId;

    private static SimpleDateFormat format_yyyy_MM_dd;

    private static SimpleDateFormat format_yyyyMM;

    private static SimpleDateFormat format_yyyyMMdd;

    /**
     * 微信appid
     */
    public static String WX_APP_ID = "wx6ed152a9338240d2";


    /**
     * 生产服务器地址
     */
    public static final String HTML_BASE_ADDRESS = "http://www.yuguang168.com/creditApp";

    /**
     * 域名地址
     */
    public static final String TOTAL_BASE_ADDRESS = "http://www.yuguang168.com";

    /**
     * 测试服务器地址
     */
    public static final String HTML_BASE_TEST_ADDRESS = "http://test.yuguang168.com/creditApp";

    /**
     * 帮助页面
     */
    public static final String HTML_HELP_ADDRESS = "https://bjjtapi.yuguang168.com/creditApp/userPage/helpView.do";

    /**
     * 服务器地址
     */
    public static String API_SERVER_ADDRESS_CONTEXT;

    /**
     * 图片缓存地址
     */
    public static final String IMG_CACHE_NAME = Environment.getExternalStorageDirectory() + "/.dada/.img/";

    /**
     * 检查更新
     */
    public static final String key_checkUpdate = "/pub/checkUpdate.do";

    /**
     * 登录
     */
    public static final String key_login = "/user/login.do";

    /**
     * 查询banner列表
     */
    public static final String key_queryBannerList = "/pub/queryBannerList.do";

    /**
     * 给预留号码下发短信验证码
     */
    public static final String key_sendSmsCode = "/user/sendSmsCode.do";

    /**
     * 用户退出
     */
    public static final String key_logout = "/user/logout.do";
    /**
     * 获取分享参数
     */

    /**
     * 打印通用日志的TAG
     */
    public static final String LOG_TAG = "dada";
    /**
     * 打印联网类日志的TAG
     */
    public static final String LOG_TAG_NET = "dada";
    /**
     * 文本日志文件
     */
    public static final String LOG_TXT = Environment.getExternalStorageDirectory() + "/dada_log.txt";

    /**
     * 用户登录信息 -用户登录密码
     */
    public static String loginPasswrod;
    /**
     * 用户登录信息 -用户登录名
     */
    public static String loginUserName;
    /**
     * 用户登录信息 -手机号码
     */
    public static String mblNo;

    /**
     * 开关标志-是否打开联网请求的开关
     */
    public static final boolean NET_REQ_DEBUG_FALG = true;

    /**
     * 显示资产状态
     */
    public static final String SHOW_MONEY_STS_SHOW = "1";
    public static final String SHOW_MONEY_STS_HIDE = "0";

    /**
     * 提现类型
     */
    public static final String WITHDRAW_TYPE_CASH = "CASH";//现金
    public static final String WITHDRAW_TYPE_SHARE = "SHARE";//收益

    /**
     *
     */
    public static final String PASSWROD_TYPE_KEY = "PASSWROD_TYPE_KEY";
    public static final String PASSWROD_TYPE_LOGIN = "LOGIN";
    public static final String PASSWROD_TYPE_PAY = "PAY";

    /**
     * 交易流水类型
     */
    public static String ORDER_TYPE_ALL = null;//全部
    public static String ORDER_TYPE_RECHARGE = "recharge";//充值
    public static String ORDER_TYPE_WITHDRAW = "withdraw";//提现
    public static String ORDER_TYPE_PAYBACK = "payoff";//还款
    public static String ORDER_TYPE_PAYOFF = "withhold";//扣款
    public static String ORDER_TYPE_OTHER = "other";//其他

    /**
     * 日期时间常量
     */
    public static final long ONE_SECONDE = 60;
    public static final long ONE_HOUR = 60 * ONE_SECONDE;
    public static final long ONE_DAY = 24 * ONE_HOUR;
    public static final long ONE_MONTH = 30 * ONE_DAY;

    /**
     * android手机操作系统的版本
     */
    public static String osVer = android.os.Build.VERSION.RELEASE;

    /**
     * 用户登录信息 -登录的用户的学校ID
     */
    public static String schoolId;
    /**
     * 用户登录信息 -登录的用户的学校名字
     */
    public static String schoolName;

    /**
     * 服务器模式 , 生产地址
     */
    public final static int SERVER_MODEL_ONLINE = 1;

    /**
     * 服务器模式 , 测试地址
     */
    public final static int SERVER_MODEL_TEST = 0;

    /**
     * 当前选择的服务器模式
     */
    public static int SERVER_MODEL = SERVER_MODEL_ONLINE;

    /**
     * 用户登录信息 -用户主账户性别
     */
    public static int sex;

    /**
     * 用户登录信息 -登录session
     */
    public static String sid;


    /**
     * 用户登录信息 -登录手机号码
     */
    public static String mobile;

    /**
     * 日期格式化对象
     */
    public static SimpleDateFormat simpleDateFormat;
    public static SimpleDateFormat simpleDateFormat_MM_point_dd;
    public static SimpleDateFormat simpleDateFormat_yyyyMMdd;
    public static SimpleDateFormat simpleDateFormat_yyyyMMdd_week;
    public static SimpleDateFormat simpleDateFormatIncludeYear;

    /**
     * 终端ID，目前取的mac地址
     */
    public static String termId;

    /**
     * 客户端类型，固定为"1"，后台识别为android
     */
    public static final String termTyp = "1";

    /**
     * 生产的上级通知主页地址
     */
    public static final String TEST_SUPERIOR_NOTICE_PAGE = "https://m.yzmm365.cn/gardenwap/school/queryNOtificationDocList.xhtml?schoolId=";

    /**
     * Umeng渠道标识
     */
    public static String umengChannel;

    /**
     * Umeng key
     */
    public static String umengKey;
    /**
     * 用户登录信息 -登录的用户ID
     */
    public static String userId;

    /**
     * 版本号
     */
    public static String version;

    /**
     * 检测是不是文件描述中VersionCode是不是合法 versionName和versionCode的关系必须满足一定算法
     * 由于android应用的版本号是根据versionCode来区分的
     * ，但是一般人来阅读versionName更加直观，为了避免出现versionName版本号增加了
     * ，但是忘记将versionCode响应变大的情况，特增加此函数；在应用启动StartActivity.onCreate()中判断。
     * 如果出现了相关的情况，应用会自动退出。
     */
    public static boolean checkVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            String[] vers = info.versionName.split("\\.");
            int versionCode = 0;
            versionCode = Integer.parseInt(vers[0]) * 10000 + Integer.parseInt(vers[1]) * 100
                    + Integer.parseInt(vers[2]);

            return versionCode == info.versionCode;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /***
     * 剪断String
     *
     * @param str
     * @param maxLength
     * @return
     */
    public static String cutString(String str, int maxLength) {
        if (str == null || maxLength <= 0) {
            return "";
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength);
    }

    public static String dateChange(String yyyyMMdd) {
        if (format_yyyyMMdd == null) {
            format_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        }
        if (format_yyyy_MM_dd == null) {
            format_yyyy_MM_dd = new SimpleDateFormat("yyyy.MM.dd");
        }
        try {
            return format_yyyy_MM_dd.format(format_yyyyMMdd.parse(yyyyMMdd));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * debug信息输出函数
     */
    public static void debug(Object info) {

        if (info != null && DEBUG) {
            Log.d(LOG_TAG, info.toString());
            File file = new File(LOG_TXT);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(LOG_TXT, true), "utf-8"));
                printWriter.println(info.toString());
                printWriter.flush();
                printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * debug信息输出函数
     */
    public static void debugNetLog(Object info) {
        if (info != null && DEBUG) {
            Log.d(LOG_TAG_NET, info.toString());
        }
    }

    public static String formatDate_MM_point_dd(Date date) {
        if (simpleDateFormat_MM_point_dd == null) {
            simpleDateFormat_MM_point_dd = new SimpleDateFormat("MM.dd");
        }
        return simpleDateFormat_MM_point_dd.format(date);
    }

    public static String formatDate_yyyyMMdd(Date date) {
        if (simpleDateFormat_yyyyMMdd == null) {
            simpleDateFormat_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        }
        return simpleDateFormat_yyyyMMdd.format(date);
    }

    public static String formatDate_yyyyMMdd_week(Date date) {
        if (simpleDateFormat_yyyyMMdd_week == null) {
            simpleDateFormat_yyyyMMdd_week = new SimpleDateFormat("yyyy.MM.dd E");
        }
        return simpleDateFormat_yyyyMMdd_week.format(date);
    }

    /**
     * 格式化时间
     */
    public static String formatTime(long time) {

        // time = time * 1000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        int year = calendar.get(Calendar.YEAR);
        calendar.setTime(new Date(System.currentTimeMillis()));
        if (calendar.get(Calendar.YEAR) == year) {
            if (simpleDateFormat == null) {
                simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
            }
            return simpleDateFormat.format(time);
        } else {
            if (simpleDateFormatIncludeYear == null) {
                simpleDateFormatIncludeYear = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            }
            return simpleDateFormatIncludeYear.format(new Date(time));
        }

    }

    public static String formatTime3(long time) {
        long now = System.currentTimeMillis();
        long t = (now - time) / 1000;
        if (t < ONE_SECONDE) {
            return "1分钟前";
        } else if (t >= ONE_SECONDE && t < ONE_HOUR) {
            return t / ONE_SECONDE + "分钟前";
        } else {
            return formatTime(time);
        }
    }

    /**
     * 通过key获取请求地址
     * @param key
     * @return
     */
    public static String getAddressByKey(String key) {
        if (key == null) {
            return null;
        }
        return getBaseUrl() + key;
    }

    /**
     * @param context
     * @return
     */
    public static String getAmapKey(Context context) {
        if (AmapKey != null) {
            return AmapKey;
        }
        PackageManager manager = context.getPackageManager();
        ApplicationInfo info;
        try {
            info = manager.getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        AmapKey = info.metaData.getString("com.amap.api.v2.apikey");

        return AmapKey;

    }

    /**
     * 获取基础主机地址
     *
     * @return
     */
    public static String getBaseAddress() {

        //Junjie.Lai 这里加了一下地址
        if (API_SERVER_ADDRESS_CONTEXT != null && !"".equals(API_SERVER_ADDRESS_CONTEXT)) {
            return API_SERVER_ADDRESS_CONTEXT;
        }

        if (SERVER_MODEL_TEST == SERVER_MODEL) {
            return HTML_BASE_TEST_ADDRESS;
        }

        if (SERVER_MODEL_ONLINE == SERVER_MODEL) {
            return HTML_BASE_ADDRESS;
        }


        return "";
    }

    /**
     * 获取基础URL
     *
     * @return
     */
    public static String getBaseUrl() {
        return getBaseAddress();
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static String getDeviceId(Context context) {
        try {
            if (deviceId == null) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                deviceId = tm.getDeviceId();
            }
            return deviceId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取校园风采中，不同栏目的html链接
     *
     * @param colId :栏目ID
     * @return
     */
    public static final String getHtml(String colId) {
        if (Global.schoolId == null) {
            return null;
        }
        String url = getBaseAddress() + "/wap/" + Global.schoolId + "/" + Global.schoolId + "_"
                + colId + ".html";

        return url;
    }

    public static String getImgUrl(String string) {
        return string;
    }

    public static String getMonth() {
        if (format_yyyyMM == null) {
            format_yyyyMM = new SimpleDateFormat("yyyyMM");
        }
        return format_yyyyMM.format(new Date());
    }


    public static String getStudyTargetStr(HashMap<String, Integer> maps) {
        if (maps == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (String key : maps.keySet()) {
            sb.append(key + "|" + maps.get(key) + ",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }


    /**
     * 获取mac地址，并将其设置为设备ID
     */
    public static String getTermId(Context context) {
        if (termId != null) {
            return termId;
        }

        termId = getDeviceId(context);
        if (termId != null) {
            return termId;
        }

        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (info == null || info.getMacAddress() == null) {
            debug("getMacAddress Failded!");
            return termId;
        }

        String macAddr = new String(info.getMacAddress().getBytes());
        if ("02:00:00:00:00:00".equals(macAddr)) {
            macAddr = getMacAddr();
        }
        termId = macAddr;
        return termId;
    }

    public static String getToday() {
        if (format_yyyyMMdd == null) {
            format_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        }
        return format_yyyyMMdd.format(new Date());
    }

    /**
     * 获取友盟的渠道参数
     *
     * @param context
     * @return
     */
    public static String getUmengChannel(Context context) {
        if (umengChannel != null) {
            return umengChannel;
        }
        PackageManager manager = context.getPackageManager();
        ApplicationInfo info;
        try {
            info = manager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        umengChannel = info.metaData.getString("UMENG_CHANNEL");
        return umengChannel;
    }


    /**
     * 获取版本号，并将其设置为version
     *
     * @param context
     * @return
     */
    public static String getVersion(Context context) {
        if (version != null) {
            return version;
        }
        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            debug("getAppVersion Failded!");
            return null;
        }
        version = info.versionName;
        return version;
    }

    /**
     * 初始化全局设置
     *
     * @param context
     */
    public static void initGlobal(Context context) {
        getUmengChannel(context);
        getAmapKey(context);
        getVersion(context);
        getDeviceId(context);
        getTermId(context);
    }


    /**
     * 获取手机的MAC地址
     *
     * @return
     */
    public String getMac() {
        String str = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (macSerial == null || "".equals(macSerial)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
        return macSerial;
    }

    /**
     * 获取文件内容
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }


    /**
     * 获取string
     *
     * @param reader
     * @return
     * @throws Exception
     */
    public static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }


    /**
     * 另外一个获取MAC的方法
     *
     * @return
     */
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }
}
