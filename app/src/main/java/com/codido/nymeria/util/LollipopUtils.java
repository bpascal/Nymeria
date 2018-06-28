package com.codido.nymeria.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 萝莉棒工具类
 */
public class LollipopUtils {

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Context appContext = context.getApplicationContext();
        int result = 0;
        int resourceId =
                appContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = appContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取状态栏颜色
     * @param activity
     * @param view
     * @param color
     */
    public static void setStatusbarColor(Activity activity, View view, int color) {
        //对于Lollipop 的设备，只需要在style.xml中设置colorPrimaryDark即可
        //对于4.4的设备，如下设置padding即可，颜色同样在style.xml中配置
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = getStatusBarHeight(activity);
            if (view.getBackground() == null)
                view.setBackgroundColor(color);
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }
}
