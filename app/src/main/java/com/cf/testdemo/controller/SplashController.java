package com.cf.testdemo.controller;

import android.content.Intent;
import android.os.Message;

import com.cf.testdemo.constants.ActionEvents;
import com.cf.testdemo.constants.ControllerState;

import com.cf.testdemo.view.CountriesActivity;
import com.cf.testdemo.view.SplashActivity;

public class SplashController extends AbstractController {

	public SplashController(ControllerState state) {
		super(state);
		// TODO Auto-generated constructor stub
	}

	public CfApplicationController getCfApplicationController() {
		return CfApplicationController.getCfApplicationController();
	}

	public SplashActivity getSplashActivity() {
		return (SplashActivity) getCfApplicationController().getCurrentView();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (getSplashActivity().isFinishing()) {
			return;
		}

		switch (ActionEvents.values()[msg.what]) {
		case SPLASH_END:
			Intent intent = null;
			intent = new Intent(getSplashActivity(), CountriesActivity.class);
			getSplashActivity().startActivity(intent);
			getSplashActivity().finish();
			break;
		default:
			super.handleMessage(msg);
		}
	}

}