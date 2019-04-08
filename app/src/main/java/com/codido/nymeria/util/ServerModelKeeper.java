package com.codido.nymeria.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 保存服务器模式
 * 
 * @author liu_kf
 * 
 */
public class ServerModelKeeper {
	private static final String PREFERENCES_NAME = "SERVER_MODEL";

	/**
	 * 保存服务器模式
	 * @param context
	 * @param SERVER_MODEL
	 */
	public static void saveServerModel(Context context, int SERVER_MODEL) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putInt("SERVER_MODEL", SERVER_MODEL);
		editor.commit();
	}

	/**
	 * 获取缓存的服务器模式
	 * @param context
	 * @return 默认返回-1，如返回-1，说明用户未自主选择服务器模式
	 */
	public static int getServerModel(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		return pref.getInt("SERVER_MODEL", -1);
	}

}
