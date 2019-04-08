package com.codido.nymeria.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class YImageUtils
{
	public static final int RPOCESS_QUALITY = 80;

	public static final int minSideLength = 500;

	public static final int maxNumOfPixels = 1280 * 720;

	// 用来测试图片上传，很小的尺寸
	// public static final int maxNumOfPixels = 300 * 200;

	public static final String IMG_CACHE_NAME = Environment.getExternalStorageDirectory()
			+ "/.yzmm/.img/";

	public static final String IMAGE_SAVE_NAME = Environment.getExternalStorageDirectory()
			+ "/yzmm/";

	public static boolean copy(File oldfile, String newPath) {
		try {
			int byteread = 0;
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldfile);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.flush();
				fs.close();

				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}

	public static String saveImage(File file) {
		if (file == null || (!file.exists())) {
			return null;
		}
		File fileSaveDir = new File(IMAGE_SAVE_NAME);
		if (!fileSaveDir.exists()) {
			boolean bCreateDirSuccess = fileSaveDir.mkdirs();
			if (!bCreateDirSuccess) {
				return null;
			}
		}

		String strMd5Name = Md5.MD5(file.getAbsolutePath().getBytes( ));
		
		String strSaveFileName = IMAGE_SAVE_NAME + strMd5Name + ".jpg";
		File fileSave = new File(strSaveFileName);
		if (!fileSave.exists()) {
			try {
				fileSave.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}else {
			Global.debug("图片" + strSaveFileName + "已存在");
			return strSaveFileName;
		}

		boolean bRenameSuccess = copy(file, strSaveFileName);

		if (bRenameSuccess) {
			return fileSave.getAbsolutePath();
		}
		return null;
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

	public static String getImageExtendsName(String fullName) {

		if (fullName == null) {
			return null;
		}
		String urlLow = fullName.toLowerCase();
		if ((!urlLow.equals("jpg")) && (!urlLow.equals("png"))) {
			return ".jpg";
		}
		int extendsIndex = -1;
		for (int i = fullName.length() - 1; i >= 0; i--) {
			if (fullName.charAt(i) == '.') {
				extendsIndex = i;
				break;
			}
		}
		if (extendsIndex == -1) {
			return null;
		}
		else {
			return fullName.substring(extendsIndex);
		}
	}

	public static int computeSampleSize(Options options, int minSideLength,
			int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		}
		else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	public static int computeInitialSampleSize(Options options, int minSideLength,
			int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h
				/ maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		}
		else if (minSideLength == -1) {
			return lowerBound;
		}
		else {
			return upperBound;
		}
	}

	public static Bitmap cutAndZoomBitmap(Bitmap bitmap, float width, float height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		float f1 = (float) w / h;
		float f2 = (float) width / height;
		int d = 0;
		int cut = 0;
		if (f1 > f2) {
			d = (int) (w - width * h / height);
			w = w - d;
			cut = 1;
		}
		else if (f2 > f1) {
			d = (int) (h - w * height / width);
			cut = 2;
			h = h - d;
		}
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = (float) height / h;
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = null;
		if (cut == 0) {
			newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, false);
		}
		else if (cut == 1) {
			newbmp = Bitmap.createBitmap(bitmap, d / 2, 0, w, h, matrix, false);
		}
		else if (cut == 2) {
			newbmp = Bitmap.createBitmap(bitmap, 0, d / 2, w, h, matrix, false);
		}
		return newbmp;
	}

	public static Bitmap zoomBitmap(Bitmap bitmap, float width, float height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = (float) height / h;
		float scale = scaleHeight < scaleWidth ? scaleHeight : scaleWidth;

		if (scale >= 1) {
			return bitmap;
		}

		matrix.postScale(scale, scale);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, false);

		return newbmp;
	}

	public static Bitmap getSimpleBitmapFromFile(File file, int width, int height) {
		if (file == null || !file.exists()) {
			return null;
		}

		try {

			Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(file.getAbsolutePath(), options);
			int widthScale = ((options.outWidth - 1) / width);
			int heightScale = ((options.outHeight - 1) / height);

			int scale = widthScale < heightScale ? widthScale : heightScale;
			options.inSampleSize = scale;
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		} catch (OutOfMemoryError e) {
			return null;
		}

	}

	public static File compressImage(File file) {
		return compressImage(file, true);
	}

	public static File compressImage(File file, boolean deleteSrc) {
		String filename = file.getAbsolutePath();
		String fileExtendsName = getImageExtendsName(filename).toLowerCase();

		try {
			Options options = new Options();
			options.inJustDecodeBounds = true;
			options.inSampleSize = 1;

			Bitmap srcBitmap = BitmapFactory.decodeFile(filename, options);
			options.inSampleSize = computeSampleSize(options, -1, 640 * 480);
			options.inJustDecodeBounds = false;

			options.inJustDecodeBounds = false;
			srcBitmap = BitmapFactory.decodeFile(filename, options);

			File tagFile = new File(IMG_CACHE_NAME + System.currentTimeMillis() + "m"
					+ fileExtendsName);

			saveBitmapToFile(tagFile, srcBitmap);
			if (deleteSrc) {
				file.delete();
			}
			return tagFile;

		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Bitmap getBitmapFromRootView(View view) {
		view.setDrawingCacheEnabled(true);
		Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		if (bmp != null) {
			return bmp;
		}
		else {
			return null;
		}
	}

	public static String saveBitmapToFile(Bitmap bitmap) {
		String path = IMG_CACHE_NAME + System.currentTimeMillis() + ".jpg";
		String extendsName = getImageExtendsName(path);
		Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.PNG;
		if (extendsName.toLowerCase().equals(".jpg")) {
			compressFormat = Bitmap.CompressFormat.JPEG;
		}
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(path);
			if (bitmap.compress(compressFormat, RPOCESS_QUALITY, fileOutputStream)) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
			return path;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Bitmap getBitmapFromFileProccessed(String path) {
		if (path == null) {
			return null;
		}
		try {
			Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			Global.debug("图片处理，处理前，图片大小=" + options.outWidth + "x" + options.outHeight);

			int i = computeSampleSize(options, minSideLength, maxNumOfPixels);

			options.inJustDecodeBounds = false;
			options.inSampleSize = i;

			Bitmap tag = BitmapFactory.decodeFile(path, options);
			Global.debug("图片处理，处理后，图片大小=" + tag.getWidth() + "x" + tag.getHeight());
			return tag;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Bitmap getBitmapFromBytesProccessed(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		try {
			Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

			Global.debug("图片处理，处理前，图片大小=" + options.outWidth + "x" + options.outHeight);
			int i = computeSampleSize(options, minSideLength, maxNumOfPixels);
			options.inJustDecodeBounds = false;
			options.inSampleSize = i;

			Bitmap tag = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
			Global.debug("图片处理，处理后，图片大小=" + tag.getWidth() + "x" + tag.getHeight());

			return tag;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Bitmap getBitmapFromFile(String path) {
		if (path == null) {
			return null;
		}
		try {
			return BitmapFactory.decodeFile(path);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void saveBitmapToFile(File file, Bitmap bitmap) {
		String path = file.getAbsolutePath();
		String extendsName = getImageExtendsName(path);
		Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.PNG;
		if (extendsName.toLowerCase().equals(".jpg")) {
			compressFormat = Bitmap.CompressFormat.JPEG;
		}
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			if (bitmap.compress(compressFormat, RPOCESS_QUALITY, fileOutputStream)) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		}
		else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xFFFFFFFF;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst_left, dst_top, dst_right, dst_bottom);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);

		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

}
