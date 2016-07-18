package com.cf.testdemo.controller;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.cf.testdemo.constants.ControllerState;
import com.cf.testdemo.constants.PermissionEvents;
import com.cf.testdemo.dbHandler.DBHandler;
import com.cf.testdemo.dbHandler.framework.OnlyOneDBHandler;
import com.cf.testdemo.model.CfApplicationModel;

import java.util.HashMap;

public class CfApplicationController extends Application implements Runnable {

	private String TAG = CfApplicationController.class.getSimpleName();

	public CfApplicationModel mCfApplicationModel;
	public static CfApplicationController mCfApplicationController;

	/** List of Controllers */
	private HashMap<ControllerState, AbstractController> mControllers = new HashMap<ControllerState, AbstractController>();
	private ControllerState mCurrentControllerState;
	private Context mCurrentView;
	private ConnectivityManager m_cConnectivityManager;
	private OnlyOneDBHandler m_cObjDBHandler = null;
	private DBHandler m_cDBHandler;
	private Typeface mFontAwesomeTypeface = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		if (mCfApplicationController == null) {
			Log.d(TAG, "onCreate CfApplicationController ");
			mCfApplicationController = this;
			run();
		}
		super.onCreate();
	}

	public Typeface getFontAwesomeTypeface(){
		if(mFontAwesomeTypeface == null){
			mFontAwesomeTypeface = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf" );
		}
		return mFontAwesomeTypeface;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// Create Controllers for each view and model
		for (ControllerState state : ControllerState.values()) {
			mControllers.put(state, createController(state));
		}

		// init model
		mCfApplicationModel = new CfApplicationModel(this);
	}

	public static synchronized CfApplicationController getCfApplicationController() {
		if (mCfApplicationController == null) {
			Log.d("CfApplicationController",
					"getCfApplicationController call on created");
			mCfApplicationController = new CfApplicationController();
		}
		return mCfApplicationController;
	}

	public CfApplicationModel getCfApplicationModel() {
		return mCfApplicationModel;
	}

	public ControllerState getCurrentControllerState() {
		return mCurrentControllerState;
	}

	public Context getCurrentView() {
		return mCurrentView;
	}

	public AbstractController getController(ControllerState state) {
		return mControllers.get(state);
	}

	public void initCurrentControllerView(ControllerState state, Context context) {
		mCurrentControllerState = state;
		mCurrentView = context;
	}

	/** Allocate controllers */
	public AbstractController createController(ControllerState state) {
		AbstractController controller = null;

		switch (state) {
		case SPLASH_SCREEN:
			controller = new SplashController(state);
			break;
		case COUNTRY_STATE:
			controller = new CountriesController(state);
			break;
		default:
			break;
		}

		return controller;
	}

	public ProgressDialog m_cProgressBar = null;

	public void displayProgressBar(String message) {
		if (m_cProgressBar == null) {
			m_cProgressBar = new ProgressDialog(getCurrentView());
			m_cProgressBar.setCancelable(false);
			if (message != null && !message.trim().equalsIgnoreCase("")) {
				m_cProgressBar.setMessage(message);
			}
			m_cProgressBar.show();
		}
	}

	public void updateProgressBar(String message) {
		if (m_cProgressBar == null) {
			displayProgressBar(message);
		} else {
			if (message != null && !message.trim().equalsIgnoreCase("") && m_cProgressBar.isShowing()) {
				m_cProgressBar.setMessage(message);
			}
			if (!m_cProgressBar.isShowing()) {
				m_cProgressBar.show();
			}
		}
	}

	public void stopProgressBar() {
		if (m_cProgressBar != null && m_cProgressBar.isShowing()) {
			try {
				m_cProgressBar.dismiss();
			} catch (Exception e) {

			}
		}
		m_cProgressBar = null;
	}

	public Activity getCurrentActivity() {
		return (Activity)mCurrentView;
	}

	public boolean checkOnlyPermission(String permission, int permissionEvent) {
		Log.d(TAG, "checkOnlyPermission -> "+permission);
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
			if (ActivityCompat.checkSelfPermission(getCurrentActivity(), permission) == PackageManager.PERMISSION_GRANTED) {
				Log.d(TAG, " checkOnlyPermission permissions has been granted.");
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}


	public boolean checkPermission(String permission, int permissionEvent) {
		Log.d(TAG, "Checking permissions for -> "+permission);

		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
			if (ActivityCompat.checkSelfPermission(getCurrentActivity(), permission) == PackageManager.PERMISSION_GRANTED) {
				Log.d(TAG, "permissions has been granted.");
				return true;
			} else {
				if (ActivityCompat.shouldShowRequestPermissionRationale(getCurrentActivity(),permission)) {
					Log.d(TAG, "permissions has been not granted. shouldShowRequestPermissionRationale");
					ActivityCompat.requestPermissions(getCurrentActivity(), new String[]{permission}, permissionEvent);
				} else {
					Log.d(TAG, "permissions has been not granted. requestPermissions");
					ActivityCompat.requestPermissions(getCurrentActivity(), new String[]{permission}, permissionEvent);
				}
				return false;
			}
		}else{
			return true;
		}
	}

	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (PermissionEvents.values()[requestCode]){
			case REQUEST_CONTACTS:
			case REQUEST_CAMERA:
			case REQUEST_SMS_READ:
			case PHONE_CALL:
			case REQUEST_STORAGE:
			case ACCESS_COARSE_LOCATION:
			case ACCESS_FINE_LOCATION:
				if (!verifyPermissions(grantResults)) {
					Log.d(TAG, "Permissions not granted, operation couldn't be performed.");
					showPermissionSnackBar("Permissions not granted, operation couldn't be performed");
				}
				break;
		}
	}

	public void openAppInfoSetting(){
		Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setData(Uri.parse("package:" + getPackageName()));
		startActivity(intent);
	}

	public void showPermissionSnackBar(String msg) {
		if (getCurrentActivity().getCurrentFocus() != null){
			Snackbar.make(getCurrentActivity().getCurrentFocus(), msg, Snackbar.LENGTH_INDEFINITE)
					.setAction("Settings", new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							openAppInfoSetting();
						}
					})
					.show();
		}else{
			AlertDialog.Builder builder = new AlertDialog.Builder(getCurrentActivity());
			builder.setMessage(msg).setTitle("Note");

			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User clicked OK button
				}
			});

			builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User clicked OK button
					openAppInfoSetting();
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}

	private boolean verifyPermissions(int[] grantResults) {
		// At least one result must be checked.
		if(grantResults.length < 1){
			return false;
		}

		// Verify that each required permission has been granted, otherwise return false.
		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}


	public ConnectivityManager getConnectivityManager() {
		if (null == m_cConnectivityManager) {
			m_cConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		return m_cConnectivityManager;
	}

	public OnlyOneDBHandler openOnlyOneDBHandler() {
		if (null == m_cObjDBHandler) {
			OnlyOneDBHandler
					.DB_PATH(getDatabasePath(OnlyOneDBHandler.DB_NAME())
							.getAbsolutePath());
			m_cObjDBHandler = OnlyOneDBHandler.getInstance(this);
			try {
				m_cObjDBHandler.createDataBase();
			} catch (Exception e) {
				m_cObjDBHandler.close();
				m_cObjDBHandler = null;
			}
		}
		return m_cObjDBHandler;
	}

	public void closeOneDBHandler() {
		if (null != m_cObjDBHandler) {
			m_cObjDBHandler.close();
		}
		m_cObjDBHandler = null;
	}

	public DBHandler getDBHandler() {
		if (null == m_cDBHandler) {
			m_cDBHandler = new DBHandler(this);
		}
		return m_cDBHandler;
	}

	public static void StuffsPrint(String pStuff) {

	}

	public static void ExceptionPrint(String pException) {
		System.out.println(pException);
	}

}