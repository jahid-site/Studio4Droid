package com.studio4droid.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.io.*;
import java.text.*;
import android.webkit.JavascriptInterface;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;

public class FileUtils {
	
	private static Context context;
	
	@JavascriptInterface
	public static void init(Context context){
		FileUtils.context = context;
	}
	
	@JavascriptInterface
	public static void createNewFile(String path) {
		int lastSep = path.lastIndexOf(File.separator);
		if (lastSep > 0) {
			String dirPath = path.substring(0, lastSep);
			makeDir(dirPath);
		}
		File file = new File(path);
		try {
			if (!file.exists())
				file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@JavascriptInterface
	public static String readFile(String path) {
		createNewFile(path);
		StringBuilder sb = new StringBuilder();
		FileReader fr = null;
		try {
			fr = new FileReader(new File(path));
			char[] buff = new char[1024];
			int length = 0;
			while ((length = fr.read(buff)) > 0) {
				sb.append(new String(buff, 0, length));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
	@JavascriptInterface
	public static void writeFile(String path, String str) {
		createNewFile(path);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(new File(path), false);
			fileWriter.write(str);
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileWriter != null)
					fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@JavascriptInterface
	public static void copyFile(String sourcePath, String destPath) {
		if (!isExistFile(sourcePath)) return;
		createNewFile(destPath);
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(sourcePath);
			fos = new FileOutputStream(destPath, false);
			byte[] buff = new byte[1024];
			int length = 0;
			while ((length = fis.read(buff)) > 0) {
				fos.write(buff, 0, length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@JavascriptInterface
	public static boolean copyAssetFolder(String srcName, String dstName) {
		try {
			boolean result = true;
			String fileList[] = context.getAssets().list(srcName);
			if (fileList == null) return false;

			if (fileList.length == 0) {
				result = copyAssetFile(srcName, dstName);
			} else {
				File file = new File(dstName);
				result = file.mkdirs();
				for (String filename : fileList) {
					result &= copyAssetFolder(srcName + File.separator + filename, dstName + File.separator + filename);
				}
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@JavascriptInterface
	public static boolean copyAssetFile(String srcName, String dstName) {
		try {
			InputStream in = context.getAssets().open(srcName);
			File outFile = new File(dstName);
			OutputStream out = new FileOutputStream(outFile);
			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			out.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@JavascriptInterface
	public static void moveFile(String sourcePath, String destPath) {
		copyFile(sourcePath, destPath);
		deleteFile(sourcePath);
	}
	
	@JavascriptInterface
	public static void copyFolder(String _source, String _destination)
	{
		File source = new File(_source);
		File destination = new File(_destination);
		if (source.isDirectory())
		{
			if (!destination.exists())
			{
				destination.mkdirs();
			}
			String files[] = source.list();

			for (String file : files)
			{
				copyFolder(source + "/"+ file, destination + "/" + file);
			}
		}
		else
		{
			InputStream in = null;
			OutputStream out = null;

			try
			{
				in = new FileInputStream(source);
				out = new FileOutputStream(destination);

				byte[] buffer = new byte[1024];

				int length;
				while ((length = in.read(buffer)) > 0)
				{
					out.write(buffer, 0, length);
				}
			}
			catch (Exception e)
			{
				try
				{
					in.close();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}

				try
				{
					out.close();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
		}
	}
	
	@JavascriptInterface
	public static void moveFolder(String source, String dest) {
		copyFolder(source, dest);
		deleteFile(source);
	}
	
	@JavascriptInterface
	public static void deleteFile(String path) {
		File file = new File(path);
		if (!file.exists()) return;
		if (file.isFile()) {
			file.delete();
			return;
		}
		File[] fileArr = file.listFiles();
		if (fileArr != null) {
			for (File subFile : fileArr) {
				if (subFile.isDirectory()) {
					deleteFile(subFile.getAbsolutePath());
				}
				if (subFile.isFile()) {
					subFile.delete();
				}
			}
		}
		file.delete();
	}
	
	@JavascriptInterface
	public static boolean isExistFile(String path) {
		File file = new File(path);
		return file.exists();
	}
	
	@JavascriptInterface
	public static void makeDir(String path) {
		if (!isExistFile(path)) {
			File file = new File(path);
			file.mkdirs();
		}
	}
	
	@JavascriptInterface
	public static String listDir(String json) {
		try{
			final JSONObject options = new JSONObject(json);
			File dir = new File(options.getString("path"));
			if (!dir.exists() || dir.isFile()){
				return "[]";
			}
			String list = "[";
			File[] listFiles = dir.listFiles();
			if(listFiles == null || listFiles.length <= 0){
				return "[]";
			}
			if(!options.has("sort")){
				options.put("sort","name");
			}
			if(options.getString("sort").equalsIgnoreCase("name")){
				if (listFiles != null && listFiles.length > 1) {
					Arrays.sort(listFiles, new Comparator<File>() {
							@Override
							public int compare(File object1, File object2) {
								return object1.getName().compareTo(object2.getName());
							}
						});
				}
			} else if(options.getString("sort").equalsIgnoreCase("date")){
				if (listFiles != null && listFiles.length > 1) {
					Arrays.sort(listFiles, new Comparator<File>() {
							@Override
							public int compare(File a, File b) {
								if(a.lastModified() < b.lastModified() )
									return 1;
								if(a.lastModified() > b.lastModified() )
									return -1;
								return 0;
							}});
				}
			} else if(options.getString("sort").equalsIgnoreCase("size")){
				if (listFiles != null && listFiles.length > 1) {
					Arrays.sort(listFiles, new Comparator<File>() {
							@Override
							public int compare(File a, File b) {
								if(a.length() < b.length())
									return 1;
								if(a.length() > b.length())
									return -1;
								return 0;
							}});
				}
			}
			if(!options.has("format")){
				options.put("format","dd/MM/yyyy a hh:mm:ss");
			}
			if(!options.has("filter")){
				options.put("filter","all");
			}
			if(options.has("isReverse")&&options.getBoolean("isReverse")){
				listFiles = FileUtils.reverse(listFiles);
			}
			if(listFiles.length > 0){
				for(File file : listFiles){
					if(file.isDirectory() && options.getString("filter").equalsIgnoreCase("file")){
						continue;
					}
					if(file.isFile() && options.getString("filter").equalsIgnoreCase("folder")){
						continue;
					}
					list += "{name:'"+file.getName()+"',";
					list += "size:'"+FileUtils.getFileSize(file.length())+"',";
					list += "date:'"+ new SimpleDateFormat(options.getString("format")).format(file.lastModified()) + "',";
					list += "length:"+file.length()+",";
					list += "lastModified:"+file.lastModified()+",";
					list += "isHidden:"+file.isHidden()+",";
					list += "isFile:"+file.isFile()+",";
					list += "isFolder:"+file.isDirectory()+",";
					list += "totalSpace:"+file.getTotalSpace()+",";
					list += "freeSpace:"+file.getFreeSpace()+",";
					list += "path:'"+file.getAbsolutePath()+"'},";
				}
			}
			return list + "]";
		} catch (JSONException ex){
			ToastUtils.showLong(ex.toString());
			return "[]";
		}
	}
	
	@JavascriptInterface
	public static File[] reverse(File[] arr) {
        List<File> list = Arrays.asList(arr);
        Collections.reverse(list);
        return (File[]) list.toArray();
    }
	
	@JavascriptInterface
	public static boolean isDirectory(String path) {
		if (!isExistFile(path)) return false;
		return new File(path).isDirectory();
	}
	
	@JavascriptInterface
	public static boolean isFile(String path) {
		if (!isExistFile(path)) return false;
		return new File(path).isFile();
	}
	
	@JavascriptInterface
	public static long getFileLength(String path) {
		if (!isExistFile(path)) return 0;
		return new File(path).length();
	}
	
	@JavascriptInterface
	public static long getFolderLength(String path) {
		if (!isExistFile(path)) return 0;
		return new File(path).length();
	}
	
	@JavascriptInterface
	public static String getExternalStorageDir() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}
	
	@JavascriptInterface
	public static String getPackageDataDir() {
		return context.getExternalFilesDir(null).getAbsolutePath();
	}
	
	@JavascriptInterface
	public static String getPublicDir(String type) {
		return Environment.getExternalStoragePublicDirectory(type).getAbsolutePath();
	}
	
	@JavascriptInterface
	public static String convertUriToFilePath(final Uri uri) {
		String path = null;
		if (DocumentsContract.isDocumentUri(context, uri)) {
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					path = Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			} else if (isDownloadsDocument(uri)) {
				final String id = DocumentsContract.getDocumentId(uri);

				if (!TextUtils.isEmpty(id)) {
					if (id.startsWith("raw:")) {
						return id.replaceFirst("raw:", "");
					}
				}

				final Uri contentUri = ContentUris
					.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				path = getDataColumn(contentUri, null, null);
			} else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = MediaStore.Audio.Media._ID + "=?";
				final String[] selectionArgs = new String[]{
					split[1]
				};

				path = getDataColumn(contentUri, selection, selectionArgs);
			}
		} else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())) {
			path = getDataColumn(uri, null, null);
		} else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {
			path = uri.getPath();
		}

		if (path != null) {
			try {
				return URLDecoder.decode(path, "UTF-8");
			}catch(Exception e){
				return null;
			}
		}
		return null;
	}
	
	@JavascriptInterface
	private static String getDataColumn(Uri uri, String selection, String[] selectionArgs) {
		Cursor cursor = null;

		final String column = MediaStore.Images.Media.DATA;
		final String[] projection = {
			column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} catch (Exception e) {

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return null;
	}
	
	@JavascriptInterface
	private static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}
	
	@JavascriptInterface
	private static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}
	
	@JavascriptInterface
	private static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}
	
	@JavascriptInterface
	public static void saveBitmap(Bitmap bitmap, String destPath) {
		FileOutputStream out = null;
		FileUtils.createNewFile(destPath);
		try {
			Bitmap sb = Bitmap.createScaledBitmap(bitmap, 144, 144, false);
			out = new FileOutputStream(new File(destPath));
			sb.compress(Bitmap.CompressFormat.PNG, 100, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@JavascriptInterface
	public static Bitmap getScaledBitmap(String path, int max) {
		Bitmap src = BitmapFactory.decodeFile(path);

		int width = src.getWidth();
		int height = src.getHeight();
		float rate = 0.0f;

		if (width > height) {
			rate = max / (float) width;
			height = (int) (height * rate);
			width = max;
		} else {
			rate = max / (float) height;
			width = (int) (width * rate);
			height = max;
		}

		return Bitmap.createScaledBitmap(src, width, height, true);
	}
	
	@JavascriptInterface
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int width = options.outWidth;
		final int height = options.outHeight;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
	
	@JavascriptInterface
	public static Bitmap decodeSampleBitmapFromPath(String path, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}
	
	@JavascriptInterface
	public static void resizeBitmapFileRetainRatio(String fromPath, String destPath, int max) {
		if (!isExistFile(fromPath)) return;
		Bitmap bitmap = getScaledBitmap(fromPath, max);
		saveBitmap(bitmap, destPath);
	}
	
	@JavascriptInterface
	public static void resizeBitmapFileToSquare(String fromPath, String destPath, int max) {
		if (!isExistFile(fromPath)) return;
		Bitmap src = BitmapFactory.decodeFile(fromPath);
		Bitmap bitmap = Bitmap.createScaledBitmap(src, max, max, true);
		saveBitmap(bitmap, destPath);
	}
	
	@JavascriptInterface
	public static void resizeBitmapFileToCircle(String fromPath, String destPath) {
		if (!isExistFile(fromPath)) return;
		Bitmap src = BitmapFactory.decodeFile(fromPath);
		Bitmap bitmap = Bitmap.createBitmap(src.getWidth(),
											src.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, src.getWidth(), src.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(src.getWidth() / 2, src.getHeight() / 2,
						  src.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(src, rect, rect, paint);

		saveBitmap(bitmap, destPath);
	}
	
	@JavascriptInterface
	public static void resizeBitmapFileWithRoundedBorder(String fromPath, String destPath, int pixels) {
		if (!isExistFile(fromPath)) return;
		Bitmap src = BitmapFactory.decodeFile(fromPath);
		Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src
											.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, src.getWidth(), src.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(src, rect, rect, paint);

		saveBitmap(bitmap, destPath);
	}
	
	@JavascriptInterface
	public static void cropBitmapFileFromCenter(String fromPath, String destPath, int w, int h) {
		if (!isExistFile(fromPath)) return;
		Bitmap src = BitmapFactory.decodeFile(fromPath);

		int width = src.getWidth();
		int height = src.getHeight();

		if (width < w && height < h)
			return;

		int x = 0;
		int y = 0;

		if (width > w)
			x = (width - w) / 2;

		if (height > h)
			y = (height - h) / 2;

		int cw = w;
		int ch = h;

		if (w > width)
			cw = width;

		if (h > height)
			ch = height;

		Bitmap bitmap = Bitmap.createBitmap(src, x, y, cw, ch);
		saveBitmap(bitmap, destPath);
	}
	
	@JavascriptInterface
	public static void rotateBitmapFile(String fromPath, String destPath, float angle) {
		if (!isExistFile(fromPath)) return;
		Bitmap src = BitmapFactory.decodeFile(fromPath);
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		Bitmap bitmap = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
		saveBitmap(bitmap, destPath);
	}
	
	@JavascriptInterface
	public static void scaleBitmapFile(String fromPath, String destPath, float x, float y) {
		if (!isExistFile(fromPath)) return;
		Bitmap src = BitmapFactory.decodeFile(fromPath);
		Matrix matrix = new Matrix();
		matrix.postScale(x, y);

		int w = src.getWidth();
		int h = src.getHeight();

		Bitmap bitmap = Bitmap.createBitmap(src, 0, 0, w, h, matrix, true);
		saveBitmap(bitmap, destPath);
	}
	
	@JavascriptInterface
	public static void skewBitmapFile(String fromPath, String destPath, float x, float y) {
		if (!isExistFile(fromPath)) return;
		Bitmap src = BitmapFactory.decodeFile(fromPath);
		Matrix matrix = new Matrix();
		matrix.postSkew(x, y);

		int w = src.getWidth();
		int h = src.getHeight();

		Bitmap bitmap = Bitmap.createBitmap(src, 0, 0, w, h, matrix, true);
		saveBitmap(bitmap, destPath);
	}
	
	@JavascriptInterface
	public static void setBitmapFileColorFilter(String fromPath, String destPath, int color) {
		if (!isExistFile(fromPath)) return;
		Bitmap src = BitmapFactory.decodeFile(fromPath);
		Bitmap bitmap = Bitmap.createBitmap(src, 0, 0,
											src.getWidth() - 1, src.getHeight() - 1);
		Paint p = new Paint();
		ColorFilter filter = new LightingColorFilter(color, 1);
		p.setColorFilter(filter);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(bitmap, 0, 0, p);
		saveBitmap(bitmap, destPath);
	}
	
	@JavascriptInterface
	public static void setBitmapFileBrightness(String fromPath, String destPath, float brightness) {
		if (!isExistFile(fromPath)) return;
		Bitmap src = BitmapFactory.decodeFile(fromPath);
		ColorMatrix cm = new ColorMatrix(new float[]
										 {
											 1, 0, 0, 0, brightness,
											 0, 1, 0, 0, brightness,
											 0, 0, 1, 0, brightness,
											 0, 0, 0, 1, 0
										 });

		Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cm));
		canvas.drawBitmap(src, 0, 0, paint);
		saveBitmap(bitmap, destPath);
	}
	
	@JavascriptInterface
	public static void setBitmapFileContrast(String fromPath, String destPath, float contrast) {
		if (!isExistFile(fromPath)) return;
		Bitmap src = BitmapFactory.decodeFile(fromPath);
		ColorMatrix cm = new ColorMatrix(new float[]
										 {
											 contrast, 0, 0, 0, 0,
											 0, contrast, 0, 0, 0,
											 0, 0, contrast, 0, 0,
											 0, 0, 0, 1, 0
										 });

		Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cm));
		canvas.drawBitmap(src, 0, 0, paint);

		saveBitmap(bitmap, destPath);
	}
	
	@JavascriptInterface
	public static int getJpegRotate(String filePath) {
		int rotate = 0;
		try {
			ExifInterface exif = new ExifInterface(filePath);
			int iOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

			switch (iOrientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					rotate = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					rotate = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					rotate = 270;
					break;
				default:
					rotate = 0;
					break;
			}
		}
		catch (IOException e) {
			return 0;
		}

		return rotate;
	}
	
	@JavascriptInterface
	public static File createNewPictureFile(Context context) {
		SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileName = date.format(new Date()) + ".jpg";
		File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + fileName);
		return file;
	}
	
	@JavascriptInterface
	public static String getFileSize(long size) {
		if(size <= 0) return "0";
		final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
}
