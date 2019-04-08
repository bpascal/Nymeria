/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.codido.nymeria.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class NetworkUtils
{

	private int connectTimeout = 30 * 1000;
	private int readTimeout = 30 * 1000;
	Proxy mProxy = null;
	Context mContext;
	public String encoding = "UTF-8";

	private boolean isCanceled = false;
	HttpURLConnection httpURLConnection;

	public void cancel() {

		isCanceled = true;
		try {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public NetworkUtils(Context context, String encoding) {
		this.mContext = context;
		this.encoding = encoding;
		setDefaultHostnameVerifier();
	}

	public NetworkUtils(Context context) {
		this.mContext = context;
		setDefaultHostnameVerifier();
	}

	public NetworkUtils(Context context, DownloadListener downloadListener) {
		this.mContext = context;
		this.downloadListener = downloadListener;
	}

	/**
	 * 设置代理
	 */
	public void detectProxy() {
		// ConnectivityManager cm = (ConnectivityManager)
		// mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		// NetworkInfo ni = cm.getActiveNetworkInfo();
		// if (ni != null && ni.isAvailable() && ni.getType() ==
		// ConnectivityManager.TYPE_WIFI) {
		//
		// String proxyHost = android.net.Proxy.getDefaultHost();
		// int port = android.net.Proxy.getDefaultPort();
		// if (proxyHost != null) {
		// final InetSocketAddress sa = new InetSocketAddress(proxyHost, port);
		// mProxy = new Proxy(Proxy.Type.HTTP, sa);
		// }
		// }
	}

	private void setDefaultHostnameVerifier() {
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		HttpsURLConnection.setDefaultHostnameVerifier(hv);
	}

	public byte[] sendAndGetByteArray(String strUrl) {
		if (strUrl == null || isCanceled) {
			return null;
		}
		detectProxy();

		try {
			URL url = new URL(strUrl);

			if (mProxy != null) {
				httpURLConnection = (HttpURLConnection) url.openConnection(mProxy);
			}
			else {
				httpURLConnection = (HttpURLConnection) url.openConnection();
			}
			httpURLConnection.setConnectTimeout(connectTimeout);
			httpURLConnection.setReadTimeout(readTimeout);
			httpURLConnection.setDoInput(true);

			if (!isCanceled) {
				httpURLConnection.connect();
			}

			InputStream content = httpURLConnection.getInputStream();
			try {
				return YUtils.readByteArrayFromStream(content);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cancel();
		}

		return null;
	}

	public String SendAndWaitResponse(String strReqData, String strUrl, String _encoding,
			boolean isLog) {
		HashMap<String, String> m = new HashMap<String, String>();
		if (_encoding == null) {
			_encoding = encoding;
		}
		m.put("Content-type", "application/json;charset=" + _encoding);
//		m.put("yzmm_file_name", "test.jpg");
//		m.put("yzmm_file_type", "jpg");
		
		return SendAndWaitResponse(strReqData, strUrl, _encoding, m, isLog);
	}

	// public String send(String strReqData, String strUrl, String _encoding,
	// Map<String, String> httpHeads, boolean isLog)
	// {
	// // HttpClient client = new
	// }

	public String SendAndWaitResponse(String strReqData, String strUrl, String _encoding,
			Map<String, String> httpHeads, boolean isLog) {

		if (_encoding == null) {
			_encoding = this.encoding;
		}
		if (isLog) {
			Global.debugNetLog("\r\n请求地址:" + strUrl);
			Global.debugNetLog("\r\nReqData:" + strReqData);
		}
		detectProxy();

		String strResponse = null;

		try {
			URL url = new URL(strUrl);

			if (mProxy != null) {
				httpURLConnection = (HttpURLConnection) url.openConnection(mProxy);
			}
			else {
				httpURLConnection = (HttpURLConnection) url.openConnection();
			}
			httpURLConnection.setConnectTimeout(connectTimeout);
			httpURLConnection.setReadTimeout(readTimeout);
			httpURLConnection.setDoInput(true);

			if (httpHeads != null) {
				for (String key : httpHeads.keySet()) {
					httpURLConnection.addRequestProperty(key, httpHeads.get(key));
				}
			}

			if (Global.CookieStr != null) {
 				httpURLConnection.addRequestProperty("Cookie", Global.CookieStr);
			}

			if (!isCanceled && strReqData != null) {
				httpURLConnection.setDoOutput(true);
				OutputStream os = httpURLConnection.getOutputStream();
				os.write(strReqData.getBytes(_encoding));
				os.flush();
			}

			if (Global.CookieStr == null) {
				
 				try {
					String key, cookieVal, sessionId = "";
					for (int i = 1; (key = httpURLConnection.getHeaderFieldKey(i)) != null; i++) {
						if (key.equalsIgnoreCase("set-cookie")) {
							cookieVal = httpURLConnection.getHeaderField(i);
							cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
							sessionId = sessionId + cookieVal + ";";
						}

					}

					if (!"".equals(sessionId)  ) {
						Global.CookieStr = sessionId;
					}
 				} catch (Exception e) {
					Global.debug("url can't httpURLConnection");
					return null;
				}
			}
			
			Global.debug("Global.CookieStr=" + Global.CookieStr); 

			if (!isCanceled) {
				InputStream content = httpURLConnection.getInputStream();
				strResponse = new String(YUtils.readByteArrayFromStream(content), _encoding);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cancel();
		}

		if (isLog) {
			Global.debugNetLog("回应数据:["+strUrl+"]" + strResponse + "\r\n");
		}

		return strResponse;
	}

	public boolean urlDownloadToFile(Context context, String strurl, String path) {
		boolean bRet = false;

		detectProxy();

		try {
			URL url = new URL(strurl);
			if (mProxy != null) {
				httpURLConnection = (HttpURLConnection) url.openConnection(mProxy);
			}
			else {
				httpURLConnection = (HttpURLConnection) url.openConnection();
			}
			httpURLConnection.setConnectTimeout(connectTimeout);
			httpURLConnection.setReadTimeout(readTimeout);
			httpURLConnection.setDoInput(true);
			if (!isCanceled) {

				File file = new File(path);
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);

				byte[] temp = new byte[1024];
				int i = 0;

				InputStream is = httpURLConnection.getInputStream();
				while ((i = is.read(temp)) > 0) {
					if (isCanceled) {
						break;
					}
					fos.write(temp, 0, i);
				}

				fos.close();
				is.close();

				bRet = true;

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cancel();
		}

		return bRet;
	}

	public NetworkUtils downloadFiles(String urlStr) {

		detectProxy();

		try {
			URL url = new URL(urlStr);
			if (mProxy != null) {
				httpURLConnection = (HttpURLConnection) url.openConnection(mProxy);
			}
			else {
				httpURLConnection = (HttpURLConnection) url.openConnection();
			}
			if (downloadListener != null && !isCanceled) {
				downloadListener.setMaxDownloadCount(httpURLConnection.getContentLength());
			}

			httpURLConnection.setConnectTimeout(connectTimeout);
			httpURLConnection.setReadTimeout(readTimeout);

			InputStream is = httpURLConnection.getInputStream();

			if (downloadListener != null && !isCanceled) {
				downloadListener.downloadStarted();
			}

			byte[] temp = new byte[1024 * 8];
			int i = 0;
			while ((i = is.read(temp)) > 0 && !isCanceled) {

				if (downloadListener != null && !isCanceled) {
					downloadListener.download(temp, 0, i);
				}
			}

			if (downloadListener != null && !isCanceled) {
				downloadListener.downloadfinished();
			}
			is.close();
			httpURLConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();

			if (downloadListener != null && !isCanceled) {
				downloadListener.downloadError(e);
			}
		} finally {
			cancel();
		}

		return this;
	}

	DownloadListener downloadListener;

	public static class DownloadAdapter implements DownloadListener
	{
		public void download(int ch) {}

		public void download(byte[] buffer, int start, int length) {}

		public void downloadError(Exception e) {}

		public void downloadStarted() {}

		public void downloadfinished() {}

		public void setMaxDownloadCount(int maxCount) {}
	}

	public static interface DownloadListener
	{
		/**
		 * 下载到一个字节
		 * 
		 * @param ch
		 */
		public void download(int ch);

		/**
		 * 下载到一个字节数
		 * 
		 * @param buffer
		 * @param start
		 * @param length
		 */
		public void download(byte[] buffer, int start, int length);

		/**
		 * 设置本次下载的字节数
		 * 
		 * @param maxCount
		 */
		public void setMaxDownloadCount(int maxCount);

		/**
		 * 通知现在完成
		 */
		public void downloadfinished();

		/**
		 * 下载开始
		 */
		public void downloadStarted();

		/**
		 * 下载发生错误
		 */
		public void downloadError(Exception e);
	}

	/**
	 * 判断是否有网络
	 *
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
}
