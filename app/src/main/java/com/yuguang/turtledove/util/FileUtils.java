package com.codido.nymeria.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * 文件操作类
 */
public class FileUtils {

	private static final String TAG = "FileUtils";
	public static final String FILE_DIR = Environment.getExternalStorageDirectory().getPath() + "/mingcloud";

	public static final String LIST_DIR = FILE_DIR + Urls.ROOTPATH;

	public static String getCameraPath() {
		return LIST_DIR + "/cameraimage/";
	}

	public static String getHeadImagePath() {
		return LIST_DIR + "/headimage/";
	}

	public static String getCoverImage() {
		return LIST_DIR + "/converimage/";
	}

	public static String getSendImagePath() {
		return LIST_DIR + "/sendimage/";
	}

	public static String getDownloadPath() {
		return LIST_DIR + "/download/";
	}

	public static String getPhotoPath() {
		return LIST_DIR + "/photo/";
	}

	public static String getNewApkPath() {
		return LIST_DIR + "/apk/";
	}

	public static String CarshPath() {
		return LIST_DIR + "/crash/";
	}

	/**
	 * 获取DevicedInfo
	 *
	 * @return
	 */
	@SuppressWarnings("resource")
	public static String getDeviceInfo() {
		// 配置文件放到Android系统目录，命名为deviceinfo.txt 2016.11.18
		String fileName = Environment.getExternalStorageDirectory().getPath() + "/deviceinfo.txt";
		String str = "";
		File file = new File(fileName);
		InputStreamReader is = null;
		try {
			is = new InputStreamReader(new FileInputStream(file), "UTF-8");
			BufferedReader br = new BufferedReader(is);

			String MineTypeLine = null;
			while ((MineTypeLine = br.readLine()) != null) {
				str = str + MineTypeLine;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					// 关闭读取流
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str;

	}

	/**
	 * 转码方式获取DevicedInfo
	 *
	 * @param str_filepath
	 *            配置文件路径
	 * @return
	 */
	public static String convertCodeAndGetText(String str_filepath) {

		File file = new File(str_filepath);
		BufferedReader reader;
		String text = "";
		try {
			// FileReader f_reader = new FileReader(file);
			// BufferedReader reader = new BufferedReader(f_reader);
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream in = new BufferedInputStream(fis);
			in.mark(4);
			byte[] first3bytes = new byte[3];
			in.read(first3bytes);// 找到文档的前三个字节并自动判断文档类型。
			in.reset();
			if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB && first3bytes[2] == (byte) 0xBF) {// utf-8

				reader = new BufferedReader(new InputStreamReader(in, "utf-8"));

			} else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFE) {

				reader = new BufferedReader(new InputStreamReader(in, "unicode"));
			} else if (first3bytes[0] == (byte) 0xFE && first3bytes[1] == (byte) 0xFF) {

				reader = new BufferedReader(new InputStreamReader(in, "utf-16be"));
			} else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFF) {

				reader = new BufferedReader(new InputStreamReader(in, "utf-16le"));
			} else {

				reader = new BufferedReader(new InputStreamReader(in, "GBK"));
			}
			String str = reader.readLine();

			while (str != null) {
				text = text + str + "/n";
				str = reader.readLine();

			}
			reader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

	/**
	 * 保存Bitmap到sdcard(这里是图片路径保存)
	 *
	 * @param b
	 * @throws IOException
	 */
	public static String saveBitmap(Bitmap b) throws IOException {
		String path = getCameraPath();
		long dataTake = System.currentTimeMillis();
		// 这里是图片的存储路径，可以通过EventBus传递
		String jpegAbsPath = path + "/" + dataTake + ".jpg";
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		FileOutputStream fout = null;
		BufferedOutputStream bos = null;
		File file = new File(dir, dataTake + ".jpg");
		try {
			file.createNewFile();
			fout = new FileOutputStream(jpegAbsPath);
			bos = new BufferedOutputStream(fout);
			// 处理图片
			b.compress(Bitmap.CompressFormat.JPEG, 50, bos);
			Log.i(TAG, "saveBitmap成功");
		} catch (IOException e) {
			Log.i(TAG, "saveBitmap:失败");
		} finally {
			if (bos != null) {
				bos.flush();
				bos.close();
			}
			if (fout != null) {
				fout.close();
			}
		}
		return jpegAbsPath;
	}

	/**
	 * 保存截图
	 *
	 * @param root
	 * @param name
	 * @param bitmap
	 */
	public static void saveCapture(String root, String name, Bitmap bitmap) {
		FileOutputStream fos = null;
		try {
			File dir = new File(root);
			File file = new File(dir, name);
			if (!dir.exists()) {
				dir.mkdirs();
				file.createNewFile();
			}
			if (bitmap != null) {
				fos = new FileOutputStream(root + name);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
				fos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存截图并刷新本地相册
	 *
	 * @param context
	 * @param root
	 * @param name
	 * @param bitmap
	 * @return
	 */
	public static boolean saveTakePhoto(Context context, String root, String name, Bitmap bitmap) {
		try {
			File dir = new File(root);
			File file = new File(dir, name);
			if (!dir.exists()) {
				dir.mkdirs();
				file.createNewFile();
			}
			if (bitmap != null) {
				FileOutputStream saveFile = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, saveFile);
				// 把文件插入系统图库
				MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), name, null);
				// 最后通知图库更新
				context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
						Uri.parse("file://" + file.getAbsolutePath())));
				return true;
			}
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 是否含有SDcard
	 *
	 * @return
	 */
	public static boolean isSdcard() {
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/**
	 * 保存裁剪后的封皮图片
	 *
	 * @param data
	 * @param fileName
	 */
	public static void saveCoverBitmap(Intent data, String fileName) {
		// 取到裁剪图片
		Bundle bundle = data.getExtras();
		Bitmap mBitmap = null;
		if (null != bundle) {
			mBitmap = bundle.getParcelable("data");
			// 将裁剪图片保存到sd卡
			FileUtils.saveCapture(FileUtils.getCoverImage(), fileName, mBitmap);
		}
	}

	/**
	 * 通过路径 递归删除文件和文件夹
	 */
	public static String deleteFilebyPath(String filePath) {
		String retStr = "未清除文件";
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				retStr = "目标目录下无文件" ;
			} else {
				if (file.isFile()) {
					file.delete();
					retStr = "清除1个文件成功";
				}
				if (file.isDirectory()) {
					File[] childFile = file.listFiles();
					if (childFile == null || childFile.length == 0) {
						file.delete();
						retStr = "目标是目录为空";
					}
					for (File f : childFile) {
						DeleteFile(f);
					}
					file.delete();
					retStr = "清除"+childFile.length+"个文件成功" ;
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "deleteFilebyPath，删除文件时出现错误：" + e.getMessage());
			retStr = "清除文件发生异常";
		}
		return retStr;
	}

	/**
	 * 递归删除文件
	 *
	 * @param file
	 */
	public static void DeleteFile(File file) {
		if (!file.exists()) {
			return;
		} else {
			if (file.isFile()) {
				file.delete();
				return;
			}
			if (file.isDirectory()) {
				File[] childFile = file.listFiles();
				if (childFile == null || childFile.length == 0) {
					file.delete();
					return;
				}
				for (File f : childFile) {
					DeleteFile(f);
				}
				file.delete();
			}

		}
	}
}
