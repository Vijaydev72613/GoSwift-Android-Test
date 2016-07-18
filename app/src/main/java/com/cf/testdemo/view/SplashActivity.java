package com.cf.testdemo.view;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.cf.testdemo.R;
import com.cf.testdemo.constants.ActionEvents;
import com.cf.testdemo.constants.ControllerState;
import com.cf.testdemo.constants.PermissionEvents;
import com.cf.testdemo.controller.CfApplicationController;
import com.cf.testdemo.controller.SplashController;
import com.cf.testdemo.events.IViewListener;

public class SplashActivity extends Activity implements IViewListener , OnRequestPermissionsResultCallback{
	private static final String TAG = SplashActivity.class.getSimpleName();

	public void initviewcontroller(ControllerState state) {
		getCfApplicationController().initCurrentControllerView(state, this);
	}

	public CfApplicationController getCfApplicationController() {
		return CfApplicationController.getCfApplicationController();
		//return (CfApplicationController) getApplicationContext();
	}

	public SplashController getSplashController() {
		return (SplashController) getCfApplicationController().getController(
				ControllerState.SPLASH_SCREEN);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);
		Log.d(TAG, "onCreate Splash ");
		initviewcontroller(ControllerState.SPLASH_SCREEN);

		Thread background = new Thread() {
			public void run() {

				try {
					// Thread will sleep for 5 seconds
					sleep(1 * 2000);
					permisssion();
				} catch (Exception e) {

				}
			}
		};

		// start thread
		background.start();
	}

	private void permisssion(){
		if(getCfApplicationController().checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionEvents.REQUEST_STORAGE.ordinal())) {
			getSplashController().sendEmptyMessage(ActionEvents.SPLASH_END.ordinal());
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		getCfApplicationController().onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
}