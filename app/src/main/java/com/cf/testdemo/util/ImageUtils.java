package com.cf.testdemo.util;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.TypedValue;

public class ImageUtils {

	public static Bitmap downloadBitmap(String url) {
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {

				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory
							.decodeStream(inputStream);
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or
			// IllegalStateException
			getRequest.abort();

		} finally {
			if (client != null) {
				// client.clearCacheData();
				client.getConnectionManager().shutdown();
			}
		}
		return null;
	}

	public static Bitmap scale(Bitmap bitmap, int width, int height) {
		final int bitmapWidth = bitmap.getWidth();
		final int bitmapHeight = bitmap.getHeight();

		final float scale = Math.min((float) width / (float) bitmapWidth,
				(float) height / (float) bitmapHeight);

		final int scaledWidth = (int) (bitmapWidth * scale);
		final int scaledHeight = (int) (bitmapHeight * scale);

		final Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
				scaledHeight, true);

		return newBitmap;

	}

	// fitToWidth
	public static Bitmap scaleFitToWidth(Context context, Bitmap bitmap,
			int widthInDp) {
		final int bitmapWidth = bitmap.getWidth();
		final int bitmapHeight = bitmap.getHeight();

		final float scale = (float) getPixelsForDip(context, widthInDp)
				/ (float) bitmapWidth;

		final int scaledWidth = (int) (bitmapWidth * scale);
		final int scaledHeight = (int) (bitmapHeight * scale);

		final Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
				scaledHeight, true);

		return newBitmap;

	}

	// fitToWidth
	public static Bitmap scaleFitToHeight(Context context, Bitmap bitmap,
			int heightInDp) {
		if (bitmap == null) {
			return null;
		}
		final int bitmapWidth = bitmap.getWidth();
		final int bitmapHeight = bitmap.getHeight();

		final float scale = (float) getPixelsForDip(context, heightInDp)
				/ (float) bitmapHeight;

		final int scaledWidth = (int) (bitmapWidth * scale);
		final int scaledHeight = (int) (bitmapHeight * scale);

		final Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
				scaledHeight, true);

		return newBitmap;

	}

	public static float getPixelsForDip(Context context, int dipUnit) {
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dipUnit, r.getDisplayMetrics());
		return px;
	}

	public static float getPixelsForSp(Context context, int spUnit) {
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spUnit, r.getDisplayMetrics());
		return px;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}

	public static byte[] getByteArrayFromBitmap(Bitmap bitmap) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		if (bitmap != null)
			bitmap.compress(CompressFormat.JPEG, 75, bos);
		byte[] data = bos.toByteArray();
		return data;
	}

	public static Bitmap getBitmapFromByte(byte[] pictureByteArray) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(pictureByteArray, 0,
				pictureByteArray.length, options);
		options.inJustDecodeBounds = false;
		int samplesize = ImageUtils.calculateInSampleSize(options, 480, 640);
		options.inSampleSize = samplesize;
		Bitmap bitmapPicture = BitmapFactory.decodeByteArray(pictureByteArray,
				0, pictureByteArray.length, options);
		return bitmapPicture;
	}

	public static Bitmap rotate(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
				source.getHeight(), matrix, false);
	}

	public static Bitmap getBitmapFromURI(Uri selectedImageUri, Context context) {
		Bitmap bitmap = null;
		if (Build.VERSION.SDK_INT < 19) {
			String selectedImagePath = getPath(selectedImageUri,context);
			bitmap = BitmapFactory.decodeFile(selectedImagePath);
		} else {
			ParcelFileDescriptor parcelFileDescriptor;
			try {
				parcelFileDescriptor = context.getContentResolver()
						.openFileDescriptor(selectedImageUri, "r");
				FileDescriptor fileDescriptor = parcelFileDescriptor
						.getFileDescriptor();

				bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
				parcelFileDescriptor.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		return bitmap;
	}

	public static String getPath(Uri uri, Context context) {
		if (uri == null) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, projection,
				null, null, null);
		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		return uri.getPath();
	}

}
