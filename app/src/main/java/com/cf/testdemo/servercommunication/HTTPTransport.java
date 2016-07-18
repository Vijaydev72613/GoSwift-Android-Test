package com.cf.testdemo.servercommunication;

import android.os.Environment;
import android.util.Log;

import com.cf.testdemo.checknet.CheckInternet;
import com.cf.testdemo.events.IHttpResponseListener;
import com.cf.testdemo.model.data.RequestData;
import com.cf.testdemo.model.data.ResponseData;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class HTTPTransport {
	private static final String TAG = HTTPTransport.class.getSimpleName();

	private RequestData mRequestData = null;
	private ResponseData mResponseData = null;
	private IHttpResponseListener mIHttpResponseListener = null;

	private HttpClient mHttpClient = null;
	private HttpPost mHttpPost = null;
	private HttpGet mHttpGet = null;
	private HttpResponse mHttpResponse = null;
	private HttpEntity mHttpEntity = null;

	private boolean status = false;
	private String mDataReceived = null;
//	private Timer mTimer;
//	private TimerTask mTimerTask;

	public void reset(){
		mRequestData = null;
		mHttpClient = null;
		mIHttpResponseListener = null;
		mHttpPost = null;
		mHttpGet = null;
		mHttpResponse = null;
		status = false;
		mHttpEntity = null;
//		mTimer = null;
//		mTimerTask = null;
		mDataReceived = new String();
		mResponseData = new ResponseData();
	}

	public HTTPTransport(RequestData httpRequestData) {
		mRequestData = httpRequestData;
	}

	public boolean isInternetOn() {
		return CheckInternet.isInternetOn();
	}

	public boolean doHttpPostRequest(RequestData httpRequestData) {
		reset();
		mRequestData = httpRequestData;
		mIHttpResponseListener = httpRequestData.getIHttpResponseListener();
		mResponseData.setHttpCommandId(mRequestData.mHttpCommandId);
		mResponseData.setUNIQUE_ID(mRequestData.getUNIQUE_ID());

		if(isInternetOn()) {
			try {

//				startTimer();

				mHttpClient = new DefaultHttpClient();
				mHttpPost = new HttpPost(new URI(httpRequestData.getUrl()));

				Log.d(TAG, "doHttpPostRequest() URL ->" + httpRequestData.getUrl());
				Log.d(TAG, "doHttpPostRequest() getUNIQUE_ID ->" + httpRequestData.getUNIQUE_ID());

//				if (httpRequestData.getHttpCommandId() != HttpCommandId.LOGIN_HTTP_COMMAND) {
//					mHttpPost.setHeader("x-client-data","" + httpRequestData.getToken());
//				}

				HttpParams params = new BasicHttpParams();
				params.setParameter("http.protocol.handle-redirects", false);
				mHttpPost.setParams(params);

				if (httpRequestData.getIsSetHeader()) {
					mHttpPost.addHeader("Content-Type", httpRequestData.getHeader());
				}

				mHttpPost.setEntity(httpRequestData.getHttpEntity());
				mHttpResponse = mHttpClient.execute(mHttpPost);

				Log.d(TAG, "doHttpPostRequest() HttpStatus code ->" + mHttpResponse.getStatusLine().getStatusCode());

				if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					mDataReceived = EntityUtils.toString(mHttpResponse.getEntity());
					status = true;
					mResponseData.setData(mDataReceived);
					mResponseData.setStatus(status);
					Log.d(TAG, "doHttpPostRequest()  response -> " + mDataReceived);
				} else if (mHttpResponse.getStatusLine().getStatusCode() == 303) {
					// not login
					status = false;
					mResponseData.setStatus(status);
				} else {
					status = false;
					mResponseData.setStatus(status);
				}

			} catch (ClientProtocolException e) {
				status = false;
				mResponseData.setStatus(status);
			} catch (IOException e) {
				status = false;
				mResponseData.setStatus(status);
			} catch (URISyntaxException e) {
				status = false;
				mResponseData.setStatus(status);
			} catch (Exception e) {
				status = false;
				mResponseData.setStatus(status);
			} finally {
				mHttpClient.getConnectionManager().shutdown();
			}

			if (mIHttpResponseListener != null) {
//				cancelTimer();
				if (status) {
					mIHttpResponseListener.onSuccess(mResponseData);
				} else {
					mIHttpResponseListener.onError(mResponseData);
				}
			}
		}else{
			if (mIHttpResponseListener != null) {
				mIHttpResponseListener.onNetworkError();
			}
		}

		return status;
	}

//	public void initializeTimerTask() {
//		Log.d(TAG,"initializeTimerTask");
//		mTimerTask = new TimerTask() {
//			public void run() {
//				if (mIHttpResponseListener != null) {
//					Log.d(TAG,"initializeTimerTask run");
//					mIHttpResponseListener.onNetworkError();
//					cancelTimer();
//					cancelHttpConnections();
//					reset();
//				}
//			}
//		};
//	}

//	public void startTimer() {
//		if(mTimer == null) {
//			Log.d(TAG,"startTimer");
//			mTimer = new Timer();
//			initializeTimerTask();
//			// 1sec = 1000 msec
//			// 1 min = 1* 60sec
//			// 10min = 10*60 sec
//			// 10min = (10*60)*1000 = 600000
//			mTimer.schedule(mTimerTask, CFMacros.HTTP_CONNECTION_TIME); // 10min in msec
//		}
//	}

//	public void cancelTimer(){
//		Log.d(TAG,"cancelTimer");
//		if (mTimer != null) {
//			mTimer.cancel();
//			mTimer = null;
//		}
//		if (mTimerTask != null) {
//			mTimerTask.cancel();
//			mTimerTask = null;
//		}
//	}

	private void cancelHttpConnections(){
		Log.d(TAG,"cancelHttpConnections");
		if(mHttpClient != null){
			mHttpClient.getConnectionManager().shutdown();
		}
	}

	public boolean doHttpGetRequest(RequestData httpRequestData) {
		reset();
		mRequestData = httpRequestData;
		mResponseData.setHttpCommandId(mRequestData.mHttpCommandId);
		mIHttpResponseListener = httpRequestData.getIHttpResponseListener();
		mResponseData.setUNIQUE_ID(mRequestData.getUNIQUE_ID());

		if(isInternetOn()) {
			try {
//				startTimer();
				mHttpClient = new DefaultHttpClient();
				Log.d(TAG, "doHttpGetRequest() URL ->" + httpRequestData.getUrl());

				mHttpGet = new HttpGet(new URI(mRequestData.getUrl()));

//				if (httpRequestData.getHttpCommandId() != HttpCommandId.LOGIN_HTTP_COMMAND) {
//					mHttpGet.setHeader("x-client-data", "" + httpRequestData.getToken());
//				}

				mHttpResponse = mHttpClient.execute(mHttpGet);

				if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					mHttpEntity = mHttpResponse.getEntity();
					if (mHttpEntity != null) {
						mDataReceived = EntityUtils.toString(mHttpEntity);
						Log.d(TAG, "doHttpGetRequest()  response -> " + mDataReceived);
						status = true;
						mResponseData.setData(mDataReceived);
					}
				} else {
					Log.d(TAG, "doHttpGetRequest()  status -> " +  mHttpResponse.getStatusLine().getStatusCode());
					status = false;
				}
			} catch (Exception e) {
				status = false;
			} finally {
				mHttpClient.getConnectionManager().shutdown();
				mHttpClient = null;
				mHttpGet = null;
				mHttpResponse = null;
			}

			mResponseData.setStatus(status);

			if (mIHttpResponseListener != null) {
//				cancelTimer();
				if (status) {
					mIHttpResponseListener.onSuccess(mResponseData);
				} else {
					mIHttpResponseListener.onError(mResponseData);
				}
			}
		} else {
			if (mIHttpResponseListener != null) {
				mIHttpResponseListener.onNetworkError();
			}
		}
		return status;
	}

	public boolean doHttpDownloadRequest(RequestData httpRequestData) {
		reset();
		mRequestData = httpRequestData;
		mResponseData.setHttpCommandId(mRequestData.mHttpCommandId);
		mIHttpResponseListener = httpRequestData.getIHttpResponseListener();
		mResponseData.setUNIQUE_ID(mRequestData.getUNIQUE_ID());
		HttpURLConnection connection = null;

		if(isInternetOn()) {
			try {
				Log.d(TAG, "doHttpGetRequest() URL -> " + mRequestData.getUrl());

				URL url = new URL(mRequestData.getUrl());
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				status = true;
				mResponseData.setData(saveDB(input,connection.getContentLength()));

			} catch (Exception e) {
				status = false;
			} finally {
				connection.disconnect();
			}

			mResponseData.setStatus(status);

			if (mIHttpResponseListener != null) {
				if (status) {
					mIHttpResponseListener.onSuccess(mResponseData);
				} else {
					mIHttpResponseListener.onError(mResponseData);
				}
			}
		} else {
			if (mIHttpResponseListener != null) {
				mIHttpResponseListener.onNetworkError();
			}
		}
		return status;
	}


	private String saveDB(InputStream inputStream, int totalSize) {
		File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();

		String filename = "GoSwiff.db";
		Log.d("saveDB Local filename:", "" + filename);
		File file = null;
		String filepath = "";
		FileOutputStream fileOutput = null;
		int downloadedSize = 0;

		try {
			file = new File(SDCardRoot, filename);
			if (file.createNewFile()) {
				file.createNewFile();
			}
			fileOutput = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int bufferLength = 0;
			while ((bufferLength = inputStream.read(buffer)) > 0) {
				fileOutput.write(buffer, 0, bufferLength);
				downloadedSize += bufferLength;
			}
			fileOutput.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (downloadedSize == totalSize) {
			filepath = file.getPath();
		}

		Log.d("saveDB filepath:", "" + filepath);

		return filepath;
	}

}
