package com.codido.nymeria.util;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 文件管理工具类
 */
public class FileManager {

	/**
	 * 文件下载完成常量
	 */
	public static int UPDATE_FILE = 1;

	/**
	 * 是否可用缓存
	 */
	public static boolean IsCacheCanUse = false;

	/**
	 * 图片缓存路径(隐藏目录)
	 */
	public static final String IMG_CACHE_NAME = Environment.getExternalStorageDirectory() + "/.bobo/.img/";

	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	public static void clear(Context context) {
		File file = new File(IMG_CACHE_NAME);
		if (file == null || !file.exists()) {
			return;
		}
		for (File fileInFd : file.listFiles()) {
			if (fileInFd.isFile()) {
				fileInFd.delete();
				Global.debug(">>>删除文件:" + fileInFd.getAbsolutePath());
			}
		}
	}

	public static boolean initFileCacheFile(Context context) {
		try {

			IsCacheCanUse = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
			if (!IsCacheCanUse) {
				return false;
			}
			File imageCacheFile = new File(IMG_CACHE_NAME);

			if (!imageCacheFile.exists()) {
				IsCacheCanUse = imageCacheFile.mkdirs();
			}

			File file = new File(IMG_CACHE_NAME + ".nomedia");
			if (imageCacheFile.exists() && !file.exists()) {
				file.mkdirs();
			}

			return IsCacheCanUse;

		} catch (Exception e) {
			e.printStackTrace();
			Global.debug("图片缓存不能使用");

			return false;
		}

	}

	private Handler handler;

	private static FileManager instance;

	private HashSet<String> LoadingKeys;

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public static FileManager getInstance() {
		if (instance == null) {
			instance = new FileManager();
			instance.LoadingKeys = new HashSet<String>();
		}
		return instance;
	}

	public String getFile(String url) {

		if (YUtils.isEmpty(url) || !IsCacheCanUse) {
			return null;
		}

		String file = getFileName(url);

		Global.debug("待检测本地文件地址：" + file);
		if (file != null) {
			if (new File(file).exists()) {
				return file;
			}
		}

		if (!LoadingKeys.contains(url)) {
			synchronized (FileManager.class) {
				LoadingKeys.add(url);
			}
			threadPoolExecutor.execute(new FileLoader(url));
		}

		return null;

	}

	ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 5, 2, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5), new ThreadPoolExecutor.CallerRunsPolicy());

	/**
	 * 通知文件下载完成
	 * @param url
     */
	public void notifyFileDownloaded(String url) {
		if (url != null && handler != null) {
			Message msg = handler.obtainMessage();
			msg.what = UPDATE_FILE;
			msg.obj = url;
			msg.sendToTarget();
		}
	}

	private byte[] getByteArrayFromNet(String url, Context c) {
		if (url == null || YUtils.isEmpty(url)) {
			return null;
		}
		return new NetworkUtils(c).sendAndGetByteArray(url);
	}

	public void saveFileToSDacrd(String url, byte[] bytes) {
		if (!IsCacheCanUse || url == null || bytes == null || bytes.length == 0) {
			return;
		}
		String filename = getFileName(url);
		File file = new File(filename);
		try {

			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(bytes);
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getFileName(String url) {
		String extendsName = getExtensionName(url);
		return IMG_CACHE_NAME + Md5.MD5(url.getBytes()) + "." + extendsName;
	}

	class FileLoader implements Runnable
	{
		String url;

		public FileLoader(String url) {
			this.url = url;
		}

		@Override
		public void run() {

			byte[] bytesFormNet;
			bytesFormNet = getByteArrayFromNet(url, null);
			if (bytesFormNet != null && bytesFormNet.length > 0) {
				saveFileToSDacrd(url, bytesFormNet);
			}

			notifyFileDownloaded(url);

			// 线程执行结束，不管图片加载成功与否，都要将该图片正在加载的标志位移除
			// 即：改图片的加载过程已经结束
			synchronized (FileManager.class) {
				LoadingKeys.remove(url);
			}
		}
	}

}
