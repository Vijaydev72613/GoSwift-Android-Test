package com.cf.testdemo.checknet;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;

import com.cf.testdemo.controller.CfApplicationController;

public class CheckInternet {
	public static boolean checkNetwork(ConnectivityManager connectivityManager) {
		boolean checkCondition = false;

		NetworkInfo wifiNetwork = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null) {
			checkCondition = wifiNetwork.isConnectedOrConnecting();
			return wifiNetwork.isConnectedOrConnecting();
		}

		NetworkInfo mobileNetwork = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null) {
			checkCondition = mobileNetwork.isConnectedOrConnecting();
			return mobileNetwork.isConnectedOrConnecting();
		}

		NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
		if (activeNetwork != null) {
			checkCondition = activeNetwork.isConnectedOrConnecting();
			return activeNetwork.isConnectedOrConnecting();
		}
		return checkCondition;
	}

	public static final boolean isInternetOn() {
		// ARE WE CONNECTED TO THE NET
		ConnectivityManager connectivityManagernnec = CfApplicationController.getCfApplicationController().getConnectivityManager();
		NetworkInfo networkInfo0 = connectivityManagernnec.getNetworkInfo(0);
		NetworkInfo networkInfo1 = connectivityManagernnec.getNetworkInfo(1);
		if (networkInfo0 != null
				&& (networkInfo0.getState() == NetworkInfo.State.CONNECTED || networkInfo0
						.getState() == NetworkInfo.State.CONNECTING)) {
			return true;
		} else if (networkInfo1 != null
				&& (networkInfo1.getState() == NetworkInfo.State.CONNECTING || networkInfo1
						.getState() == NetworkInfo.State.CONNECTED)) {
			return true;
		}
		return false;
	}

	public static void setMobileDataEnabled(Context context) {

		Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
		if (Build.VERSION.SDK_INT < 16)
			intent.setClassName("com.android.phone",
					"com.android.phone.Settings");
		try {
			context.startActivity(intent);
		} catch (ActivityNotFoundException e) {

		}
	}

}