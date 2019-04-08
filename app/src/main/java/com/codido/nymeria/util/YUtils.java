package com.codido.nymeria.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 全局工具
 *
 * @author 刘凯夫
 */
public final class YUtils {
    /**
     *
     */
    public static final String URL_PATTERN = "(http[s]{0,1}://)([a-zA-Z0-9\\-]+\\.)+[a-zA-Z0-9\\-]+(/[a-zA-Z0-9\\-./?%_&=#]*)?";
    public static final Pattern pattern = Pattern.compile(URL_PATTERN);


    public static String trim(String str) {
        if (str == null) {
            return "";
        }
        return str.trim();
    }

    public static String repalceEmptySting(String src, String desc) {

        if (isEmpty(src)) {
            return desc;
        }

        return src;

    }

    public static String addMoney(String... money) {
        long result = 0;
        for (int i = 0; i < money.length; i++) {
            result += getMoneyFromStr(money[i]);
        }
        return formatMoney(result);
    }

    /**
     * 从输入框中写入的以元为单位的金额转化成以分为单位的数字；
     *
     * @param money
     * @return
     */
    public static long getMoneyFromStr(String money) {
        BigDecimal bigDecimal = new BigDecimal(money);
        return bigDecimal.multiply(new BigDecimal(100)).longValue();
    }

    public static boolean isUrlFromYZMM365(String url) {
        if (isEmpty(url)) {
            return false;
        }
        Pattern pattern = Pattern
                .compile("(http://|https://){0,1}[a-zA-Z0-9]+\\.yzmm365.cn($|/.*)");
        // 空格结束
        Matcher matcher = pattern.matcher(url);

        return matcher.matches();
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getViewMeasuredHeight(View child) {
        measureView(child);
        return child.getMeasuredHeight();
    }

    private static void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static String FormetFileSize(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获取利率<br/>
     * 保留两位小数
     *
     * @param att   实际数字
     * @param total 总数字
     * @return
     */
    public static String getRate(long att, long total) {
        if (att == 0 || total == 0) {
            return "0%";
        }
        return new BigDecimal((double) (att * 100)).divide(new BigDecimal((double) total), 2,
                BigDecimal.ROUND_HALF_UP).doubleValue()
                + "%";
    }

    /**
     * 获取利率<br/>
     * 保留两位小数
     *
     * @param att   实际数字
     * @param total 总数字
     * @return
     */
    public static String getRate(int att, int total) {
        if (att == 0) {
            return "0";
        }
        if (total == 0) {
            return "100";
        }
        return new BigDecimal((double) (att * 100)).divide(new BigDecimal((double) total), 2,
                BigDecimal.ROUND_HALF_UP).intValue()
                + "";
    }

    /**
     * 获取本应用的MD5签名信息
     */
    public static String getSignInfo(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];

            return Md5.MD5(sign.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 直接对对象的某个属�?赋�?
     *
     * @param obj
     * @param property
     * @param value
     * @throws Exception
     */
    public static void setValue(Object obj, String property, Object value) throws Exception {
        Class<?> c = obj.getClass();
        Field f = c.getDeclaredField(property);
        f.set(obj, value);
    }

    /**
     * 获取�?��属�?对应的Get函数返回值的类型
     *
     * @param c
     * @param property
     * @return
     */
    public static Class<?> getClassByPropertyName(Class<?> c, String property) {

        try {
            char ch = (char) property.charAt(0);
            String methodName = new StringBuffer(property).delete(0, 1)
                    .insert(0, Character.toUpperCase(ch)).insert(0, "get").toString();

            return c.getMethod(methodName, new Class<?>[0]).getReturnType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过Get方法获取某个指定字段的�?（String类型�?
     *
     * @param obj
     * @param property
     * @return
     */
    public static String getValueStrByGetMethod(Object obj, String property) {
        try {
            Class<?> c = obj.getClass();

            char ch = (char) property.charAt(0);
            String methodName = new StringBuffer(property).delete(0, 1)
                    .insert(0, Character.toUpperCase(ch)).insert(0, "get").toString();

            Method method = c.getMethod(methodName);

            Object result = method.invoke(obj);
            if (result == null) {
                return null;
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 通过Object的set方法设置属�?�?
     *
     * @param obj
     * @param property
     * @param value
     */
    public static void setValueBySetMehtod(Object obj, String property, Object value) {
        try {
            Class<?> c = obj.getClass();

            char ch = (char) property.charAt(0);
            String methodName = new StringBuffer(property).delete(0, 1)
                    .insert(0, Character.toUpperCase(ch)).insert(0, "set").toString();

            Method method = c.getMethod(methodName, getClassByPropertyName(c, property));

            method.invoke(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据Get方法获取请求参数列表
     *
     * @param obj
     * @return
     */
    public static String getReqStrFromGetMethods(Object obj) {
        try {
            Class<?> c = obj.getClass();

            Method[] ms = c.getMethods();
            if (ms == null || ms.length == 0) {
                return null;
            }
            boolean isFirst = true;
            StringBuffer stringBuffer = new StringBuffer();

            for (int i = 0; i < ms.length; i++) {
                if (ms[i].getName().startsWith("get") && !ms[i].getName().equals("getClass")) {
                    String res = getValFromObj(ms[i], obj);
                    if (res != null) {
                        if (isFirst) {
                            isFirst = false;
                        } else {
                            stringBuffer.append("&");
                        }
                        stringBuffer.append(getPropertyNameFromGetMethod(ms[i].getName()));
                        stringBuffer.append("=");
                        stringBuffer.append(res);
                    }
                }
            }
            return stringBuffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取特定的所有Get函数返回值的描述�?
     *
     * @param method
     * @param obj
     * @return
     */
    public static String getValFromObj(Method method, Object obj) {
        try {
            Object result = method.invoke(obj);

            if (result == null) {
                return null;
            }
            if (result instanceof List<?>) {
                List<?> c = (List<?>) result;
                if (c.size() == 0) {
                    return "|";
                }
                StringBuffer stringBuffer = new StringBuffer();
                // boolean isFirst = true;
                for (int i = 0; i < c.size(); i++) {
                    Object o = c.get(i);
                    if (o != null) {
                        // if (isFirst) {
                        // isFirst = false;
                        // }
                        // else {
                        // stringBuffer.append("|");
                        // }
                        stringBuffer.append(o.toString());
                        stringBuffer.append("|");
                    }
                }
                return stringBuffer.toString();
            } else {
                return result.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定属�?对应的标准Get方法
     *
     * @param id
     * @return
     */
    public static String getPropertyNameFromGetMethod(String id) {
        String str = id.substring(3, id.length());
        char ch = (char) str.charAt(0);
        return new StringBuffer(str).delete(0, 1).insert(0, Character.toLowerCase(ch)).toString();
    }

    // public static void writeByteArrayToFile(byte[] bytes, File file)
    // {
    // if (!file.exists()){
    // file.createNewFile();
    // }
    // FileOutputStream fileOutputStream = new FileOutputStream(file);
    // fileOutputStream.write(bytes);
    // }

    /**
     * 从一个文件中读取内容，转化成byte[],注意必须是小文件
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static byte[] readByteArrayFromFile(File file) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int length = -1;
        while ((length = fileInputStream.read(buffer)) > 0) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        byteArrayOutputStream.flush();
        fileInputStream.close();

        byte[] result = byteArrayOutputStream.toByteArray();

        byteArrayOutputStream.close();

        return result;
    }

    /**
     * 从一个输入流读出byte[],以输入流有EOF标志为准
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static byte[] readByteArrayFromStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = -1;
        while ((length = inputStream.read(buffer)) > 0) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        byteArrayOutputStream.flush();
        inputStream.close();

        byte[] result = byteArrayOutputStream.toByteArray();

        byteArrayOutputStream.close();

        return result;
    }

    /**
     * 替换一个字符串，如果它是空串的话，包含空格
     *
     * @param str
     * @return
     */
    public static String replaceIfEmpty(String str, String replace) {
        if (isEmpty(str)) {
            return replace;
        }

        return str.trim();
    }


    /**
     * 判断一个字符串是不是空串
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }

        return str.trim().equals("");
    }

    /**
     * 隐藏手机号中间部分
     *
     * @param str
     * @return
     */
    public static String ChangeMobileNum(String str) {
        String mobileNum = null;

        if (str != null && str.length() == 11) {
            String num = str.substring(0, 3);
            String num1 = str.substring(7, 11);
            mobileNum = num + "****" + num1;
        }
        return mobileNum;
    }

    /**
     * 格式化金额
     *
     * @param money
     * @return
     */
    public static String formatMoney(String money) {

        long m = 0;
        try {
            m = Long.parseLong(money);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return formatMoney(m);

    }

    /**
     * 格式化金额
     *
     * @param money
     * @return
     */
    public static String formatMoney(long money) {
        if (money == 0) {
            return "0";
        }
        boolean postive = true;
        if (money < 0) {
            postive = false;
            money = Math.abs(money);
        }
        long yuan = money / 100;
        long jiaofen = money % 100;
        String result = (yuan + ".");

        if (jiaofen != 0) {
            if (jiaofen >= 10) {
                result += (jiaofen + "");
            } else {
                result += ("0" + jiaofen);
            }
        } else {
            result += ("00");
        }

        if (!postive) {
            result = "-" + result;
        }
        return result;

    }

    /**
     * 版本号高低判断
     *
     * @param newVer 新版本号
     * @param oldVer 旧版本号
     * @return 如果新版本号大于旧版本号，则返回值>0, 如果新版本号<旧版本号，则返回值<0, 版本号相同，返回0
     */
    public static int veriosnCompare(String newVer, String oldVer) {
        if (isEmpty(oldVer)) {
            return 1;
        }
        if (isEmpty(newVer)) {
            return -1;
        }

        try {
            String[] oldvers = oldVer.split("\\.");
            int versionCodeOld = Integer.parseInt(oldvers[0]) * 10000
                    + Integer.parseInt(oldvers[1]) * 100 + Integer.parseInt(oldvers[2]);

            String[] newvers = newVer.split("\\.");
            int versionCodeNew = Integer.parseInt(newvers[0]) * 10000
                    + Integer.parseInt(newvers[1]) * 100 + Integer.parseInt(newvers[2]);

            return versionCodeNew - versionCodeOld;
        } catch (Exception e) {
            return -1;
        }

    }

    /**
     * 根据秒钟数获取时间描述，例如61秒转化为1:01
     *
     * @param seconds
     * @return
     */
    public static String getTimeDescBySeconds(int seconds) {
        int val = seconds % 60;
        return seconds / 60 + ":" + (val < 10 ? ("0" + val) : val);
    }

    /**
     * 根据秒钟数获取时间描述，例如61秒转化为1:01
     *
     * @param seconds
     * @return
     */
    public static String getTimeDescBySeconds2(int seconds) {
        int val = seconds % 60;
        int val2 = seconds / 60;
        String result = "";
        if (val2 >= 0) {
            result += (val2 + "分");
        }
        if (val > 0) {
            result += (val + "秒");
        }

        return result.equals("") ? "0秒" : result;
    }

    /**
     * 获取文件的大小
     *
     * @param kbs KB数
     * @return 多少M
     */
    public static String getFileSizeDescByKB(int kbs) {
        BigDecimal bigDecimal = new BigDecimal(kbs);
        return bigDecimal.divide(new BigDecimal(1024), 2, RoundingMode.HALF_DOWN).doubleValue()
                + "";
    }

    /**
     * 获取文件的大小
     *
     * @param bs B数
     * @return 多少M
     */
    public static String getFileSizeDescByB(long bs) {
        BigDecimal bigDecimal = new BigDecimal(bs);
        return bigDecimal.divide(new BigDecimal(1024 * 1024), 2, RoundingMode.HALF_DOWN)
                .doubleValue() + "";
    }

    /**
     * 删除指定文件夹下的所有文件
     *
     * @param floderName
     */
    public static void deleteFilesInFloder(String floderName) {
        deleteFilesInFloder(floderName, false, false);
    }

    /**
     * 删除指定文件夹下的所有文件
     *
     * @param floderName
     * @param deleteFolder
     */
    public static void deleteFilesInFloder(String floderName, boolean deleteFolder,
                                           boolean deleteMp4) {
        File file = new File(floderName);
        if (file == null || !file.exists()) {
            return;
        }
        for (File fileInFd : file.listFiles()) {
            if (fileInFd.isFile()) {
                if (!deleteMp4 && fileInFd.getAbsolutePath().endsWith("mp4")) {
                    continue;
                }
                fileInFd.delete();
                Global.debug("删除文件:" + fileInFd.getAbsolutePath());
            } else if (fileInFd.isDirectory()) {
                deleteFilesInFloder(fileInFd.getAbsolutePath(), deleteFolder, deleteMp4);
            }
        }
        if (deleteFolder) {
            file.delete();
        }
        Global.debug(floderName + "文件夹下的文件已经删除");
    }

    public static String[] toArray(ArrayList<String> items) {
        if (items == null) {
            return null;
        }
        String[] results = new String[items.size()];
        for (int i = 0; i < results.length; i++) {
            results[i] = items.get(i);
        }
        return results;
    }

    /**
     * 判断是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * 计算双色球的注数方法
     *
     * @param N-红球的数量
     * @param B-蓝球的数量
     * @return
     */
    public static BigDecimal betCalculate(int N, int B) {
        BigDecimal K = new BigDecimal(B);
        BigDecimal c = factorial(N);
        BigDecimal d = factorial(N - 6);
        BigDecimal f = factorial(6);
        BigDecimal bet = c.divide(d).divide(f).multiply(K);
        return bet;
    }

    /**
     * 计算n的阶乘
     *
     * @param n
     * @return
     */
    public static BigDecimal factorial(int n) {
        BigDecimal result = new BigDecimal(1);
        BigDecimal a;
        for (int i = 2; i <= n; i++) {
            a = new BigDecimal(i);//将i转换为BigDecimal类型
            result = result.multiply(a);
        }
        return result;
    }

}
