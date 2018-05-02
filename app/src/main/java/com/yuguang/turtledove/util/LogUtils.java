package com.codido.nymeria.util;

import android.util.Log;

/**
 * 信息打印工具，通过全局控制来完全关闭或者打开信息打印
 *
 * @author liu_kf
 */
public class LogUtils {

    private static final String TAG = "Traveller";

    public static void debug(Object tag, Object msg) {
        String classTag = TAG;
        if (tag != null) {
            if (tag instanceof String) {
                classTag = String.valueOf(tag);
            } else {
                classTag = tag.getClass().getSimpleName();
            }
        }
        // 全局信息打印标志位关闭，则不输入任何信息
        if (!CommonConfig.isdeBug()) {
            return;
        }
        if (msg == null) {
            Log.d(classTag, "null object");
            return;
        }

        Log.d(classTag, msg.toString());
    }

    public static void debug(Object obj) {
        debug(TAG, obj);
    }
}
